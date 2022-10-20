import { parseMessageDateFromISO } from "@src/@utils/date";

import MessageCard from ".";

export default {
  title: "@Component/MessageCard",
  component: MessageCard,
};

const Template = (args) => <MessageCard {...args} />;

export const DefaultTemplate = Template.bind({});
DefaultTemplate.args = {
  username: "호프(김문희)",
  date: parseMessageDateFromISO("2022-07-18T14:50:58.972493"),
  text: "안녕하세요? 저는 우아한테크코스 프론트엔드 4기 호프입니다",
  thumbnail: "https://avatars.githubusercontent.com/u/61469664?v=4",
};
