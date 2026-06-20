DROP TABLE IF EXISTS sys_user;

CREATE TABLE sys_user (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    username    VARCHAR(50)  NOT NULL,
    password    VARCHAR(100) NOT NULL,
    nickname    VARCHAR(50)  DEFAULT NULL,
    email       VARCHAR(100) DEFAULT NULL,
    phone       VARCHAR(20)  DEFAULT NULL,
    status      INT          DEFAULT 1,
    avatar      VARCHAR(500) DEFAULT NULL,
    create_time TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX IF NOT EXISTS uk_username ON sys_user(username);
CREATE INDEX IF NOT EXISTS idx_status ON sys_user(status);
