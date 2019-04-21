import requests

from spider.settings import *

class Download():

    def download(self,target_url):
        response=requests.get(target_url,headers=headers)
        if response.status_code==200:
            print('----download success!! %s'%response.url)
        return response