#!/usr/bin/env python
# -*- coding: utf-8 -*-
import pymysql

sql = """
CREATE DATABASE IF NOT EXISTS network_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE network_db;
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'Primary key',
    username    VARCHAR(50)  NOT NULL COMMENT 'Username',
    password    VARCHAR(100) NOT NULL COMMENT 'Password',
    nickname    VARCHAR(50)  DEFAULT NULL COMMENT 'Nickname',
    email       VARCHAR(100) DEFAULT NULL COMMENT 'Email',
    phone       VARCHAR(20)  DEFAULT NULL COMMENT 'Phone number',
    status      TINYINT      DEFAULT 1 COMMENT 'Status: 0-disabled, 1-enabled',
    avatar      VARCHAR(500) DEFAULT NULL COMMENT 'Avatar URL',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='System user table';
INSERT INTO sys_user (username, password, nickname, email, phone, status) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', 'Administrator', 'admin@network.com', '13800138000', 1),
('test', 'e10adc3949ba59abbe56e057f20f883e', 'Test User', 'test@network.com', '13900139000', 1);
"""

conn = pymysql.connect(
    host='192.168.0.115', port=3306,
    user='root', password='123456',
    connect_timeout=10
)
with open('init_mysql.log', 'w') as f:
    try:
        cur = conn.cursor()
        for stmt in sql.split(';'):
            stmt = stmt.strip()
            if stmt:
                cur.execute(stmt)
        cur.close()
        conn.commit()
        f.write('SUCCESS: Database initialized\n')
    except Exception as e:
        f.write(f'ERROR: {e}\n')
    finally:
        conn.close()
