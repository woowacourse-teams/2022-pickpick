insert into channel (id, name, slack_id)
values (5, '임시 채널', 'ABC1234');

insert into member (id, slack_id, thumbnail_url, username)
values (1, 'U03MC231', 'https://summer.png', '써머');

insert into message (id, modified_date, posted_date, text, member_id, channel_id, slack_message_id)
values (1, '2022-07-12 14:21:55', '2022-07-12 14:21:55', 'Sample Text', 1, 5, 'ABC1231');
