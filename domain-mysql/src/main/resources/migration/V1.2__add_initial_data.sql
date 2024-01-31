DROP TABLE IF EXISTS `reservation`;
DROP TABLE IF EXISTS `reservation_article`;
DROP TABLE IF EXISTS `hashtag`;

CREATE TABLE `reservation`
(
    `id`                     BIGINT   NOT NULL AUTO_INCREMENT,
    `reservation_article_id` BIGINT   NOT NULL,
    `member_id`              BIGINT   NULL,
    `chat_room_id`           BIGINT   NOT NULL,
    `start_time`             datetime NOT NULL,
    `end_time`               datetime NOT NULL,
    `finished`               tinyint DEFAULT FALSE,
    `created_date` datetime NOT NULL,
    `modified_date` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `reservation_article`
(
    `id`        BIGINT       NOT NULL AUTO_INCREMENT,
    `member_id` BIGINT       NOT NULL,
    `title`     VARCHAR(200) NOT NULL,
    `content`   TEXT         NULL,
    `created_date` datetime NOT NULL,
    `modified_date` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `hashtag`
(
    `id`                     BIGINT      NOT NULL AUTO_INCREMENT,
    `reservation_article_id` BIGINT      NOT NULL,
    `content`                VARCHAR(30) NULL,
    `created_date` datetime NOT NULL,
    `modified_date` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO reservation (id, reservation_article_id, member_id, chat_room_id,start_time, end_time,finished,created_date,modified_date)
VALUES (1, 1, 1, 1, '2023-12-12T09:00:00', '2023-12-12T11:00:00', false, '2023-12-12T11:00:00','2023-12-12T11:00:00');

INSERT INTO reservation (id, reservation_article_id, member_id, chat_room_id, start_time, end_time,finished,created_date,modified_date)
VALUES (2, 1, null, 2, '2023-12-12T13:00:00', '2023-12-12T14:00:00', false , '2023-12-12T11:00:00','2023-12-12T11:00:00');

INSERT INTO reservation_article (id, member_id, title, content,created_date,modified_date)
VALUES (1, 1, 'title', 'No content','2023-12-12T11:00:00','2023-12-12T11:00:00');

INSERT INTO hashtag (id, reservation_article_id, content,created_date,modified_date)
VALUES (1, 1, '면접 준비', '2023-12-12T11:00:00','2023-12-12T11:00:00');

INSERT INTO hashtag (id, reservation_article_id, content,created_date,modified_date)
VALUES (2, 1, '트러블 슈팅', '2023-12-12T11:00:00','2023-12-12T11:00:00');