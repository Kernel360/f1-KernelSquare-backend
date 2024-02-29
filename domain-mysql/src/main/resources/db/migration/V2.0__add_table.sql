DROP TABLE IF EXISTS `coding_meeting`;
DROP TABLE IF EXISTS `coding_meeting_hashtag`;
DROP TABLE IF EXISTS `coding_meeting_location`;
DROP TABLE IF EXISTS `coding_meeting_comment`;

CREATE TABLE `coding_meeting` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `member_id` bigint NOT NULL,
    `coding_meeting_title` varchar(100) COLLATE utf8mb4_unicode_520_ci NOT NULL,
    `coding_meeting_token` varchar(50) COLLATE utf8mb4_unicode_520_ci NOT NULL,
    `coding_meeting_start_time` datetime NOT NULL,
    `coding_meeting_end_time` datetime NOT NULL,
    `coding_meeting_member_upper_limit` bigint NOT NULL,
    `coding_meeting_content` text COLLATE utf8mb4_unicode_520_ci NOT NULL,
    `coding_meeting_closed` tinyint NOT NULL,
    `created_date` datetime NOT NULL,
    `modified_date` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

CREATE TABLE `coding_meeting_comment` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `coding_meeting_id` bigint NOT NULL,
    `member_id` bigint NOT NULL,
    `coding_meeting_comment_token` varchar(50) COLLATE utf8mb4_unicode_520_ci NOT NULL,
    `coding_meeting_comment_content` varchar(100) COLLATE utf8mb4_unicode_520_ci NOT NULL,
    `created_date` datetime NOT NULL,
    `modified_date` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

CREATE TABLE `coding_meeting_hashtag` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `coding_meeting_id` bigint NOT NULL,
    `coding_meeting_hashtag_content` varchar(30) COLLATE utf8mb4_unicode_520_ci DEFAULT NULL,
    `created_date` datetime NOT NULL,
    `modified_date` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;

CREATE TABLE `coding_meeting_location` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `coding_meeting_id` bigint NOT NULL,
    `coding_meeting_location_longitude` varchar(50) COLLATE utf8mb4_unicode_520_ci NOT NULL,
    `coding_meeting_location_latitude` varchar(50) COLLATE utf8mb4_unicode_520_ci NOT NULL,
    `coding_meeting_location_item_id` varchar(50) COLLATE utf8mb4_unicode_520_ci NOT NULL,
    `coding_meeting_location_place_name` varchar(200) COLLATE utf8mb4_unicode_520_ci NOT NULL,
    `created_date` datetime NOT NULL,
    `modified_date` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;
