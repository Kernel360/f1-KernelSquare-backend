DROP TABLE IF EXISTS `chat_room`;

CREATE TABLE `chat_room` (
    `created_date` datetime NOT NULL,
    `id` bigint NOT NULL AUTO_INCREMENT,
    `room_key` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `modified_date` datetime DEFAULT NULL,
    `room_name` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `active` tinyint DEFAULT 0,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
