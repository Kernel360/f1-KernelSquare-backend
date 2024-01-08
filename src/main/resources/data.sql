INSERT INTO member (created_date, experience, level_id, modified_date, email, image_url, nickname, password,
                    introduction)
VALUES ('2023-12-19T09:00:00', 1, 1, '2023-12-19T09:00:00', 'email@example.com', 'example/image.jpg', 'nickname', 'password',
        'introduction');

INSERT INTO question (closed_status, created_date, id, member_id, modified_date, view_count, content, image_url, title)
VALUES (1, '2023-12-19T09:00:00', 1, 1, '2023-12-21T09:00:00', 20, 'content', 'example/image.jpg', 'question title');

INSERT INTO tech_stack (created_date, modified_date, skill)
VALUES ('2023-12-19T09:00:00', '2023-12-19T09:00:00', 'JavaScript');

INSERT INTO tech_stack (created_date, modified_date, skill)
VALUES ('2023-12-19T09:00:00', '2023-12-19T09:00:00', 'Python');

INSERT INTO level (created_date, modified_date, image_url, name)
VALUES ('2023-12-12T09:00:00', '2023-12-12T09:00:00', 'level/level1to2.png', 1);

INSERT INTO authority (authority_type)
VALUES ('ROLE_USER');