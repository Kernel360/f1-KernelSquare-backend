DROP TABLE IF EXISTS `notice`;

CREATE TABLE `notice` (
                             `created_date` datetime NOT NULL,
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `notice_token` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
                             `modified_date` datetime DEFAULT NULL,
                             `notice_title` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
                             `notice_content` text COLLATE utf8mb4_unicode_ci NOT NULL,
                             `notice_category` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
