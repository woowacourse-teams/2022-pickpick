insert into channel (id, name, slack_id)
values (2, '임시 채널', 'ABC1234');

insert into member (id, slack_id, thumbnail_url, username)
values (46, 'U03MC231', 'https://avatars.slack-edge.com/2022-07-02/3764274541009_4d1fa8d13242781486fa_512.png',
        '써머'),
       (47, 'U03MKWQN',
        'https://secure.gravatar.com/avatar/9303b7a88d498194de16f07405211458.jpg?s=512&d=https%3A%2F%2Fa.slack-edge.com%2Fdf10d%2Fimg%2Favatars%2Fava_0011-512.png',
        '리차드'),
       (48, 'U03MSGD3',
        'https://secure.gravatar.com/avatar/42ca58848dad05653d81635ce9c2c2a3.jpg?s=512&d=https%3A%2F%2Fa.slack-edge.com%2Fdf10d%2Fimg%2Favatars%2Fava_0009-512.png',
        ''),
       (49, 'U03MS2F5', 'https://avatars.slack-edge.com/2022-06-30/3742679834690_946a429a80b00f86efd7_512.png', '봄'),
       (50, 'U03MSY8K', 'https://avatars.slack-edge.com/2022-07-01/3747157066370_dede2d72f57dae7a582a_512.png', ''),
       (51, 'U03MV3PE', 'https://avatars.slack-edge.com/2022-07-02/3775436666928_6e702b06b95d404b21b5_512.png',
        '꼬재'),
       (52, 'U03MLRD2',
        'https://secure.gravatar.com/avatar/c9cfce948145f092c7a85aa0705b2ac0.jpg?s=512&d=https%3A%2F%2Fa.slack-edge.com%2Fdf10d%2Fimg%2Favatars%2Fava_0015-512.png',
        ''),
       (53, 'U03N5VBK', 'https://avatars.slack-edge.com/2022-07-02/3746119703990_2306f90ec53f7fc3c807_512.png',
        '호프'),
       (54, 'U03NKKQ9', 'https://avatars.slack-edge.com/2022-07-03/3751682320325_084e06a3986a3940f25f_512.png', ''),
       (55, 'U03NWS3B', 'https://avatars.slack-edge.com/2022-07-06/3767808931764_9c4a0e6556498b14b48c_512.png', ''),
       (56, 'U03P3DKQ', 'https://avatars.slack-edge.com/2022-07-14/3800274950373_c0055af8cdee56527d13_512.png', '');

insert into message (id, modified_date, posted_date, text, member_id, channel_id, slack_message_id)
values (1, '2022-07-12 17:21:55', '2022-07-12 17:21:55', '과거다~~~ hi', 47, 2, 1),
       (3, '2022-07-12 21:02:06', '2022-07-12 21:02:06', '미래다~~ *리차드(전형중)*  [오후 6:44]
스레드에 답글을 남김:
*<https://some.url.com/archives/C02U/p1657619082621909?thread_ts=1657596790.119479&amp;cid=C02U|@channel…>*
인스턴스 유형이 `t4g.micro`로 변경되었습니다. 이후 새로 생성할 때 참고하세요!', 47, 2, 2),
       (4, '2022-07-12 21:03:06', '2022-07-12 21:03:06', '미래다~~ *리차드(전형중*  [오후 6:44]
스레드에 답글을 남김:
*<https://some.url.com/archives/C02U/p1657619082621909?thread_ts=1657596790.119479&amp;cid=C02U|@channel…>*
인스턴스 유형이 `t4g.micro`로 변경되었습니다. 이후 새로 생성할 때 참고하세요!', 47, 2, 3),
       (5, '2022-07-12 21:04:06', '2022-07-12 21:04:06', '미래다~~ *리차드(전형중*  [오후 6:44]
스레드에 답글을 남김:
*<https://some.url.com/archives/C02U/p1657619082621909?thread_ts=1657596790.119479&amp;cid=C02U|@channel…>*
인스턴스 유형이 `t4g.micro`로 변경되었습니다. 이후 새로 생성할 때 참고하세요!', 47, 2, 4),
       (6, '2022-07-12 21:05:06', '2022-07-12 21:05:06', '미래다~~ *리차드(전형중*  [오후 6:44]
스레드에 답글을 남김:
*<https://some.url.com/archives/C02U/p1657619082621909?thread_ts=1657596790.119479&amp;cid=C02U|@channel…>*
인스턴스 유형이 `t4g.micro`로 변경되었습니다. 이후 새로 생성할 때 참고하세요!', 47, 2, 5),
       (7, '2022-07-12 21:06:06', '2022-07-12 21:06:06', '미래다~~ *리차드(전형중*  [오후 6:44]
스레드에 답글을 남김:
*<https://some.url.com/archives/C02U/p1657619082621909?thread_ts=1657596790.119479&amp;cid=C02U|@channel…>*
인스턴스 유형이 `t4g.micro`로 변경되었습니다. 이후 새로 생성할 때 참고하세요!', 47, 2, 6),
       (8, '2022-07-12 21:02:06', '2022-07-12 21:07:06', '미래다~~ *리차드(전형중*  [오후 6:44]
스레드에 답글을 남김:
*<https://some.url.com/archives/C02U/p1657619082621909?thread_ts=1657596790.119479&amp;cid=C02U|@channel…>*
인스턴스 유형이 `t4g.micro`로 변경되었습니다. 이후 새로 생성할 때 참고하세요!', 47, 2, 7),
       (222, '2022-07-12 21:01:56', '2022-07-12 21:01:56', '*리차드(전형중*  [오후 6:44]
스레드에 답글을 남김: wow
*<https://some.url.com/archives/C02U/p1657619082621909?thread_ts=1657596790.119479&amp;cid=C02U|@channel…>*
인스턴스 유형이 `t4g.micro`로 변경되었습니다. 이후 새로 생성할 때 참고하세요!', 47, 2, 8);
