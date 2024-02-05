ALTER TABLE `reservation_article`
    ADD COLUMN `start_time` datetime NOT NULL DEFAULT '2099-12-31T00:00:00';

ALTER TABLE `reservation_article`
    MODIFY `start_time` datetime NOT NULL;
