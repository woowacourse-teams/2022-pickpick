INSERT INTO `channel` (`id`, `name`, `slack_id`)
VALUES (1, '백엔드-잡담', 'C03NNNFNY01'),
       (2, '팀-공지', 'C03N6B801S7'),
       (3, 'test-4기-잡담', 'C03MEKY23L7'),
       (4, 'test-4기-공지사항', 'C03N7SSTUMP'),
       (5, 'test-be-4기-공지', 'C03MV690W2X'),
       (6, 'test-fe-4기-공지', 'C03MEKQJBAB');

insert into member (id, slack_id, thumbnail_url, username, first_login)
values (2, 'U03MC231', 'https://avatars.slack-edge.com/2022-07-02/3764274541009_4d1fa8d13242781486fa_512.png', '써머',
        false);
