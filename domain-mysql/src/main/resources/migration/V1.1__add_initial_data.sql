INSERT INTO member (created_date, experience, level_id, modified_date, email, image_url, nickname, password,
                    introduction)
VALUES ('2023-12-19T09:00:00', 0, 1, '2023-12-19T09:00:00', 'duck@example.com', 'member/1e47ea4d-a2cb-4de7-b7e4-a0f7bd1fa24cduck.png',
        '자바덕', '$2a$10$OiSze5PH.QQ1uH7xWp.trOAbmv6rbi6foM7HsyDrVxn8RtZDMfWs.', 'introduction');

INSERT INTO authority (authority_type)
VALUES ('ROLE_USER');

INSERT INTO authority (authority_type)
VALUES ('ROLE_MENTOR');

INSERT INTO authority (authority_type)
VALUES ('ROLE_ADMIN');

INSERT INTO member_authority (member_id, authority_id)
VALUES (1, 1);

INSERT INTO member_authority (member_id, authority_id)
VALUES (1, 2);

INSERT INTO member_authority (member_id, authority_id)
VALUES (1, 3);

INSERT INTO question (closed_status, created_date, id, member_id, modified_date, view_count, content, image_url, title)
VALUES (1, '2023-12-19T09:00:00', 1, 1, '2023-12-21T09:00:00', 20, 'No content', 'question/d1c27379-2d08-4a6a-9d97-368124a50900thumb.jpg', '시간복잡도 사진');

INSERT INTO tech_stack (created_date, modified_date, skill)
VALUES ('2023-12-19T09:00:00', '2023-12-19T09:00:00', 'JavaScript');

INSERT INTO tech_stack (created_date, modified_date, skill)
VALUES ('2023-12-19T09:00:00', '2023-12-19T09:00:00', 'Python');

INSERT INTO question_tech_stack (created_date, modified_date, question_id, tech_stack_id)
VALUES ('2023-12-19T09:00:00', '2023-12-19T09:00:00', 1, 2);

INSERT INTO level (created_date, modified_date, image_url, name, level_upper_limit)
VALUES ('2023-12-12T09:00:00', '2023-12-12T09:00:00', 'level/level1to2.png', 1, 200);

INSERT INTO level (created_date, modified_date, image_url, name, level_upper_limit)
VALUES ('2023-12-12T09:00:00', '2023-12-12T09:00:00', 'level/level1to2.png', 2, 500);

INSERT INTO level (created_date, modified_date, image_url, name, level_upper_limit)
VALUES ('2023-12-12T09:00:00', '2023-12-12T09:00:00', 'level/level3to4.png', 3, 1800);

INSERT INTO level (created_date, modified_date, image_url, name, level_upper_limit)
VALUES ('2023-12-12T09:00:00', '2023-12-12T09:00:00', 'level/level3to4.png', 4, 2500);

INSERT INTO level (created_date, modified_date, image_url, name, level_upper_limit)
VALUES ('2023-12-12T09:00:00', '2023-12-12T09:00:00', 'level/level5.png', 5, 5000);

INSERT INTO level (created_date, modified_date, image_url, name, level_upper_limit)
VALUES ('2023-12-12T09:00:00', '2023-12-12T09:00:00', 'level/level6.png', 6, 10000);

INSERT INTO level (created_date, modified_date, image_url, name, level_upper_limit)
VALUES ('2023-12-12T09:00:00', '2023-12-12T09:00:00', 'level/level7.png', 7, 20000);

INSERT INTO level (created_date, modified_date, image_url, name, level_upper_limit)
VALUES ('2023-12-12T09:00:00', '2023-12-12T09:00:00', 'level/level8.png', 8, 45000);

INSERT INTO level (created_date, modified_date, image_url, name, level_upper_limit)
VALUES ('2023-12-12T09:00:00', '2023-12-12T09:00:00', 'level/level9.png', 9, 100000);

INSERT INTO level (created_date, modified_date, image_url, name, level_upper_limit)
VALUES ('2023-12-12T09:00:00', '2023-12-12T09:00:00', 'level/level10.png', 10, 9223372036854775807);
