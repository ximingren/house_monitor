import queue
from lxml import etree
from spider.Download import Download
from spider.Pipeline import Pipeline


class LianjiaSpider():
    def __init__(self):
        self.start_url = ('https://gz.lianjia.com/zufang/tianhe/pg{}',
                          'https://gz.lianjia.com/zufang/yuexiu/pg{}',
                          'https://gz.lianjia.com/zufang/liwan/pg{}',
                          'https://gz.lianjia.com/zufang/haizhu/pg{}',
                          'https://gz.lianjia.com/zufang/baiyun/pg{}',
                          'https://gz.lianjia.com/zufang/huangpu/pg{}',
                          'https://gz.lianjia.com/zufang/conghua/pg{}',
                          'https://gz.lianjia.com/zufang/zengcheng/pg{}',
                          'https://gz.lianjia.com/zufang/huadu/pg{}',
                          'https://gz.lianjia.com/zufang/nansha/pg{}')
        self.domain_url = 'https://gz.lianjia.com'
        self.queue = queue.Queue()

    def start_request(self):
        for start_url in self.start_url:
            for i in range(101):
                self.queue.put({'url': start_url.format(i), 'callback': self.parse,
                                'meta': {'district': start_url.split('/')[-2]}})

    def parse(self, response):
        selector = etree.HTML(response.text)
        block_list = selector.xpath("//div[@class='content__list--item--main']")
        for block in block_list:
            url = block.xpath("string(./p[contains(@class,'content__list--item--title')]/a/@href)")
            title = block.xpath("string(./p[contains(@class,'content__list--item--title')]/a/.)").strip()
            description = block.xpath("string(./p[contains(@class,'content__list--item--des')]/.)").replace('\n',
                                                                                                            '').replace(
                '\t', '').replace(' ', '').split('/')
            position = ''.join(filter(lambda x: x if '-' in x else '', description))
            area = ''.join(filter(lambda x: x if '㎡' in x else '', description))
            layout = ''.join(filter(lambda x: x if '卫' in x else '', description))
            height = ''.join(filter(lambda x: x if '层' in x else '', description))
            direction = ''.join(
                filter(lambda x: x if ('南' in x or '东' in x or '西' in x or '北' in x) else '', description))
            rent_money = block.xpath("string(.//span[@class='content__list--item-price']/em/.)")
            data = {}
            data['district'] = response.meta['district']
            data['url'] = self.domain_url + url
            data['title'] = title
            data['position'] = position
            data['area'] = area
            data['layout'] = layout
            data['height'] = height
            data['direction'] = direction
            data['rent_money'] = rent_money
            yield data

    def main(self):
        self.start_request()
        while True:
            try:
                target = self.queue.get()
                download = Download()
                response = download.download(target['url'])
                response.meta = target['meta']
                pipeline = Pipeline()
                for i in target['callback'](response):
                    pipeline.insert_one(i)
            except Exception as e:
                print(e)
                break


if __name__ == '__main__':
    spider = LianjiaSpider()
    spider.main()
