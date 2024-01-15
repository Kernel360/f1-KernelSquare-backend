CREATE TABLE `reservation`
(
    `id`                     BIGINT   NOT NULL,
    `reservation_article_id` BIGINT   NOT NULL,
    `member_id`              BIGINT   NULL,
    `start_time`             DATETIME NOT NULL,
    `end_time`               DATETIME NOT NULL,
    `finished`               BOOLEAN DEFAULT '0',
    `created_date` datetime NOT NULL,
    `modified_date` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE `reservation_article`
(
    `id`        BIGINT       NOT NULL,
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
    `id`                     BIGINT      NOT NULL,
    `reservation_article_id` BIGINT      NOT NULL,
    `content`                VARCHAR(30) NULL,
    `created_date` datetime NOT NULL,
    `modified_date` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO reservation (id, reservation_article_id, member_id, start_time, end_time,finished,created_date,modified_date)
VALUES (1, 1, 1, '2023-12-12T09:00:00', '2023-12-12T11:00:00', 0,'2023-12-12T11:00:00','2023-12-12T11:00:00');

INSERT INTO reservation (id, reservation_article_id, member_id, start_time, end_time,finished,created_date,modified_date)
VALUES (2, 1, null, '2023-12-12T13:00:00', '2023-12-12T14:00:00', 0,'2023-12-12T11:00:00','2023-12-12T11:00:00');

INSERT INTO reservation_article (id, member_id, title, content,created_date,modified_date)
VALUES (1, 1, 'title', 'No content','2023-12-12T11:00:00','2023-12-12T11:00:00');

INSERT INTO reservation_article (id, member_id, title, content,created_date,modified_date)
VALUES (2, 2, 'title2', 'No content2','2023-12-12T11:00:00','2023-12-12T11:00:00');