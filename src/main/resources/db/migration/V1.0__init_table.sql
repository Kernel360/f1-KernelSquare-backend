DROP TABLE IF EXISTS `answer`;
DROP TABLE IF EXISTS `answer_rank`;
DROP TABLE IF EXISTS `authority`;
DROP TABLE IF EXISTS `level`;
DROP TABLE IF EXISTS `member`;
DROP TABLE IF EXISTS `member_answer_vote`;
DROP TABLE IF EXISTS `member_authority`;
DROP TABLE IF EXISTS `member_tech_stack`;
DROP TABLE IF EXISTS `question`;
DROP TABLE IF EXISTS `tech_stack`;
DROP TABLE IF EXISTS `question_tech_stack`;
DROP TABLE IF EXISTS `social_login`;

CREATE TABLE `answer` (
    `created_date` datetime NOT NULL,
    `id` bigint NOT NULL AUTO_INCREMENT,
    `member_id` bigint DEFAULT NULL,
    `modified_date` datetime DEFAULT NULL,
    `question_id` bigint DEFAULT NULL,
    `rank_id` smallint DEFAULT NULL,
    `vote_count` smallint DEFAULT NULL,
    `content` text COLLATE utf8mb4_unicode_ci,
    `image_url` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `answer_rank` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `image_url` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `authority` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `authority_type` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `level` (
    `created_date` datetime NOT NULL,
    `id` bigint NOT NULL AUTO_INCREMENT,
    `level_upper_limit` bigint NOT NULL,
    `modified_date` datetime DEFAULT NULL,
    `name` smallint NOT NULL,
    `image_url` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_lrjnw0jty1fs19q56u0us8d0n` (`name`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `member` (
    `created_date` datetime NOT NULL,
    `experience` bigint NOT NULL,
    `id` bigint NOT NULL AUTO_INCREMENT,
    `level_id` bigint DEFAULT NULL,
    `modified_date` datetime DEFAULT NULL,
    `email` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
    `image_url` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `introduction` varchar(300) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `nickname` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_mbmcqelty0fbrvxp1q58dn57t` (`email`),
    UNIQUE KEY `UK_hh9kg6jti4n1eoiertn2k6qsc` (`nickname`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `member_answer_vote` (
    `status` smallint NOT NULL,
    `answer_id` bigint DEFAULT NULL,
    `created_date` datetime NOT NULL,
    `id` bigint NOT NULL AUTO_INCREMENT,
    `member_id` bigint DEFAULT NULL,
    `modified_date` datetime DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `member_authority` (
    `authority_id` bigint DEFAULT NULL,
    `id` bigint NOT NULL AUTO_INCREMENT,
    `member_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `member_tech_stack` (
    `created_date` datetime NOT NULL,
    `id` bigint NOT NULL AUTO_INCREMENT,
    `member_id` bigint DEFAULT NULL,
    `modified_date` datetime DEFAULT NULL,
    `tech_stack_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `question` (
    `closed_status` tinyint NOT NULL,
    `created_date` datetime NOT NULL,
    `id` bigint NOT NULL AUTO_INCREMENT,
    `member_id` bigint DEFAULT NULL,
    `modified_date` datetime DEFAULT NULL,
    `view_count` bigint DEFAULT NULL,
    `content` text COLLATE utf8mb4_unicode_ci NOT NULL,
    `image_url` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `title` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `tech_stack` (
    `created_date` datetime NOT NULL,
    `id` bigint NOT NULL AUTO_INCREMENT,
    `modified_date` datetime DEFAULT NULL,
    `skill` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_mt0upcxutmofqca4kbkswqxq` (`skill`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `question_tech_stack` (
    `created_date` datetime NOT NULL,
    `id` bigint NOT NULL AUTO_INCREMENT,
    `modified_date` datetime DEFAULT NULL,
    `question_id` bigint DEFAULT NULL,
    `tech_stack_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `social_login` (
    `created_date` datetime NOT NULL,
    `id` bigint NOT NULL AUTO_INCREMENT,
    `modified_date` datetime DEFAULT NULL,
    `email` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
    `social_provider` varchar(40) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;