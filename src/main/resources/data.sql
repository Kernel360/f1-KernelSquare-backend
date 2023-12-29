INSERT INTO member (created_date, experience, level_id, modified_date, email, image_url, nickname, password, introduction)
VALUES ('2023-12-19', 1, 1, '2023-12-19', 'email@example.com', 'http://example.com/image.jpg', 'nickname', 'password', 'introduction');

INSERT INTO question (closed_status, created_date, id, member_id, modified_date, view_count, content, image_url, title)
VALUES (1, '2023-12-19', 1, 1, '2023-12-21', 20, 'content', 'http://example.com/image.jpg', 'question title');

INSERT INTO tech_stack (created_date, modified_date, skill)
VALUES ('2023-12-19', '2023-12-19', 'JavaScript');

INSERT INTO tech_stack (created_date, modified_date, skill)
VALUES ('2023-12-19', '2023-12-19', 'Python');

INSERT INTO level (created_date, modified_date, image_url, name)
VALUES ('2023-12-12', '2023-12-12', 'example.jpg', 9)
