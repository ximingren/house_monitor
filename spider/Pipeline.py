import pymysql


class Pipeline():
    def __init__(self):
        self.client = pymysql.connect(host='localhost', user='root', password='admin', db='house')

    def insert_one(self, data):
        try:
            with self.client.cursor() as cursor:
                qmarks = ','.join(['%s'] * len(data))
                cols = ','.join(data.keys())
                insert_sql = "INSERT INTO `zufang`(%s) VALUES (%s)" % (cols, qmarks)
                cursor.execute(insert_sql, list(data.values()))
        except Exception as e:
            print(e,data['url'])
        else:
            self.client.commit()
            print(data)