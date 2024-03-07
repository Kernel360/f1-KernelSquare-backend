ALTER TABLE `answer_rank`
    ADD COLUMN `name` smallint NOT NULL;

ALTER TABLE `answer_rank`
    ADD CONSTRAINT `UK_answer_rank_name` UNIQUE (`name`);

INSERT INTO answer_rank (name, image_url)
VALUES (1, 'rank/gold_medal.png');

INSERT INTO answer_rank (name, image_url)
VALUES (2, 'rank/silver_medal.png');

INSERT INTO answer_rank (name, image_url)
VALUES (3, 'rank/bronze_medal.png');