insert into channel (id, name, slack_id)
values (5, '임시 채널', 'ABC1234');

insert into member (id, slack_id, thumbnail_url, username, first_login)
values (1, 'U03MC231', 'https://summer.png', '써머', false),
       (2, 'U03MC232', 'https://yeonlog.png', '연로그', false),
       (3, 'U03MC233', 'https://bom.png', '봄', false);

insert into message (id, modified_date, posted_date, text, member_id, channel_id, slack_message_id)
values (1, '2022-07-12 14:21:55', '2022-07-12 14:21:55', 'Sample Text', 1, 5, 'ABC1231'),
       (2, '2022-07-12 15:21:55', '2022-07-12 15:21:55', 'Sample Text', 1, 5, 'ABC1232'),
       (3, '2022-07-12 16:21:55', '2022-07-12 16:21:55', 'Sample Text', 1, 5, 'ABC1233'),
       (4, '2022-07-12 17:21:55', '2022-07-12 17:21:55', '호 Sample Text', 1, 5, 'ABC1234'),
       (5, '2022-07-13 18:21:55', '2022-07-13 18:21:55', '호 Sample Text', 1, 5, 'ABC1235'),
       (6, '2022-07-13 19:21:55', '2022-07-13 19:21:55', '호 Sample Text', 1, 5, 'ABC1236'),
       (7, '2022-07-13 20:21:55', '2022-07-13 20:21:55', '호 Sample Text', 1, 5, 'ABC1237'),
       (8, '2022-07-13 21:21:55', '2022-07-13 21:21:55', 'Sample Text A', 1, 5, 'ABC1238'),
       (9, '2022-07-13 22:21:55', '2022-07-13 22:21:55', 'Sample Text A', 1, 5, 'ABC1239'),
       (10, '2022-07-14 13:21:55', '2022-07-14 13:21:55', 'Sample Text A', 1, 5, 'ABC12310'),
       (11, '2022-07-14 14:21:55', '2022-07-14 14:21:55', 'Sample Text A', 1, 5, 'ABC12311'),
       (12, '2022-07-14 15:21:55', '2022-07-14 15:21:55', 'Sample Text A', 1, 5, 'ABC12312'),
       (13, '2022-07-14 16:21:55', '2022-07-14 16:21:55', 'Sample Text A', 1, 5, 'ABC12313'),
       (14, '2022-07-14 17:21:55', '2022-07-14 17:21:55', 'jupjup Sample Text A', 1, 5, 'ABC12314'),
       (15, '2022-07-15 13:21:55', '2022-07-15 13:21:55', 'jupjup Sample Text A', 1, 5, 'ABC12315'),
       (16, '2022-07-15 14:21:55', '2022-07-15 14:21:55', 'jupjup Sample Text A', 1, 5, 'ABC12316'),
       (17, '2022-07-15 15:21:55', '2022-07-15 15:21:55', 'jupjup Sample Text A', 1, 5, 'ABC12317'),
       (18, '2022-07-15 16:21:55', '2022-07-15 16:21:55', 'jupjup Sample Text A', 1, 5, 'ABC12318'),
       (19, '2022-07-16 13:21:55', '2022-07-16 13:21:55', 'Sample Text A', 1, 5, 'ABC12319'),
       (20, '2022-07-16 14:21:55', '2022-07-16 14:21:55', 'Sample Text A', 1, 5, 'ABC12320'),
       (21, '2022-07-16 15:21:55', '2022-07-16 15:21:55', 'Sample Text A', 1, 5, 'ABC12321'),
       (22, '2022-07-16 17:21:55', '2022-07-16 17:21:55', 'Sample Text A', 1, 5, 'ABC12322'),
       (23, '2022-07-17 13:21:55', '2022-07-17 13:21:55', '줍줍 Sample Text A', 1, 5, 'ABC12323');

insert into reminder (id, member_id, message_id, remind_date)
values (1, 2, 1, '2022-08-12 14:20:00'),
       (2, 1, 2, '2022-08-12 15:20:00'),
       (3, 1, 3, '2022-08-12 16:20:00'),
       (4, 1, 4, '2022-08-12 17:20:00'),
       (5, 1, 5, '2022-08-13 18:30:00'),
       (6, 1, 6, '2022-08-13 19:30:00'),
       (7, 1, 7, '2022-08-13 20:30:00'),
       (8, 1, 8, '2022-08-13 21:30:00'),
       (9, 1, 9, '2022-08-13 22:30:00'),
       (10, 1, 10, '2022-08-17 14:20:00'),
       (11, 1, 11, '2022-08-17 15:20:00'),
       (12, 1, 12, '2022-08-17 17:20:00'),
       (13, 1, 13, '2022-08-18 13:20:00'),
       (14, 1, 14, '2022-08-18 14:20:00'),
       (15, 1, 15, '2022-08-18 15:20:00'),
       (16, 1, 16, '2022-08-18 16:20:00'),
       (17, 1, 17, '2022-08-19 13:30:00'),
       (18, 1, 18, '2022-08-19 14:30:00'),
       (19, 1, 19, '2022-08-19 15:30:00'),
       (20, 1, 20, '2022-08-19 16:30:00'),
       (21, 1, 21, '2022-08-20 13:30:00'),
       (22, 1, 22, '2022-08-20 14:30:00'),
       (23, 1, 23, '2022-08-20 15:30:00'),
       (24, 1, 23, '2021-08-08 15:30:00'),
       (25, 3, 2, '2023-08-08 15:30:00'),
       (26, 3, 3, '2023-08-08 15:30:00'),
       (27, 3, 4, '2023-08-08 15:30:00'),
       (28, 3, 5, '2023-08-08 15:30:00'),
       (29, 3, 6, '2023-07-08 14:30:00'),
       (30, 3, 7, '2023-07-08 14:30:00');

