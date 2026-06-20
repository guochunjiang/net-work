import pymysql
import sys
with open('mysql_debug.log', 'w') as f:
    f.write('starting...\n')
    try:
        conn = pymysql.connect(host='192.168.0.115', port=3306, user='root', password='123456', connect_timeout=5)
        f.write('CONNECTED\n')
        conn.close()
    except Exception as e:
        f.write(f'ERROR: {e}\n')
    f.write('done\n')
