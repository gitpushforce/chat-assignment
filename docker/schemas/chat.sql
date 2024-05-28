CREATE TABLE IF NOT EXISTS USER_MST (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    notification_push BOOLEAN NOT NULL,
    notification_email BOOLEAN NOT NULL,
    update_date TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_date timestamp NOT NULL default CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS CHAT_TBL (
    id BIGINT AUTO_INCREMENT,
    IS_GROUP BOOLEAN,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS CHAT_PARTICIPANTS_TBL (
    chat_id BIGINT,
    user_id BIGINT,
    PRIMARY KEY (chat_id, user_id),
    FOREIGN KEY (chat_id) REFERENCES CHAT_TBL(id),
    FOREIGN KEY (user_id) REFERENCES USER_MST(id)
);

CREATE TABLE IF NOT EXISTS MESSAGE_TBL (
    id BIGINT NOT NULL AUTO_INCREMENT,
    sender_id BIGINT,
    content VARCHAR(255),
    read_flg BOOLEAN NOT NULL default FALSE,
    chat_id BIGINT,
    update_date TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    created_date timestamp NOT NULL default CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (sender_id) REFERENCES USER_MST(id),
    FOREIGN KEY (chat_id) REFERENCES CHAT_TBL(id)
);

CREATE TABLE IF NOT EXISTS READ_MESSAGE_TBL (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    message_id BIGINT NOT NULL,
    reader_id BIGINT NOT NULL,
    read_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (message_id) REFERENCES MESSAGE_TBL(id) ON DELETE CASCADE,
    FOREIGN KEY (reader_id) REFERENCES USER_MST(id) ON DELETE CASCADE
);

INSERT INTO USER_MST (name, email, notification_push, notification_email)
VALUES ('太郎', 'taro@mail.com', TRUE, FALSE);
INSERT INTO USER_MST (name, email, notification_push, notification_email)
VALUES ('Mike', 'mike@mail.com', FALSE, TRUE);
INSERT INTO USER_MST (name, email, notification_push, notification_email)
VALUES ('孫悟空', 'goku@mail.com', TRUE, FALSE);
INSERT INTO USER_MST (name, email, notification_push, notification_email)
VALUES ('田中', 'tanaka@mail.com', FALSE, TRUE);
INSERT INTO USER_MST (name, email, notification_push, notification_email)
VALUES ('山田', 'yamada@mail.com', TRUE, FALSE);