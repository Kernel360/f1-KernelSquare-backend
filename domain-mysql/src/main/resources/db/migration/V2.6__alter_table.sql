ALTER TABLE `member`
    MODIFY `image_url` VARCHAR(1000) COLLATE utf8mb4_unicode_ci;

ALTER TABLE `question`
    MODIFY `image_url` VARCHAR(1000) COLLATE utf8mb4_unicode_ci;

ALTER TABLE `answer`
    MODIFY `image_url` VARCHAR(1000) COLLATE utf8mb4_unicode_ci;

ALTER TABLE `answer_rank`
    MODIFY `image_url` VARCHAR(1000) COLLATE utf8mb4_unicode_ci;

ALTER TABLE `level`
    MODIFY `image_url` VARCHAR(1000) COLLATE utf8mb4_unicode_ci;
