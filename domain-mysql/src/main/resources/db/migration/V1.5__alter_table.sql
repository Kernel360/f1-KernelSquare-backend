ALTER TABLE `chat_room`
    ADD COLUMN `expiration_date` datetime NOT NULL DEFAULT '2099-12-31T00:00:00';

ALTER TABLE `chat_room`
    MODIFY `expiration_date` datetime NOT NULL;