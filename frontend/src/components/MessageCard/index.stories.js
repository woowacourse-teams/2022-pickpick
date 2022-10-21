import { parseMessageDateFromISO } from "@src/@utils/date";

import MessageCard from ".";
import BookmarkButton from "./MessageIconButtons/BookmarkButton";
import ReminderButton from "./MessageIconButtons/ReminderButton";

export default {
  title: "@Component/MessageCard",
  component: MessageCard,
  argTypes: {
    children: {
      control: false,
    },
  },
};

const Template = (args) => <MessageCard {...args} />;

export const DefaultTemplate = Template.bind({});
export const RemindedMessageTemplate = Template.bind({});
export const BookmarkedMessageTemplate = Template.bind({});

DefaultTemplate.args = {
  username: "호프(김문희)",
  date: parseMessageDateFromISO("2022-07-18T14:50:58.972493"),
  text: "안녕하세요? 저는 우아한테크코스 프론트엔드 4기 호프입니다",
  thumbnail: "https://avatars.githubusercontent.com/u/61469664?v=4",
  children: (
    <>
      <ReminderButton
        isActive={true}
        onClick={() => {
          return null;
        }}
      />
      <BookmarkButton
        isActive={true}
        onClick={() => {
          return null;
        }}
      />
    </>
  ),
};

RemindedMessageTemplate.args = {
  username: "호프(김문희)",
  date: parseMessageDateFromISO("2022-07-18T14:50:58.972493"),
  text: "안녕하세요? 저는 우아한테크코스 프론트엔드 4기 호프입니다",
  thumbnail: "https://avatars.githubusercontent.com/u/61469664?v=4",
  isRemindedMessage: true,
  remindDate: "2022-12-25T14:56:58.972493",
  children: (
    <ReminderButton
      isActive={true}
      onClick={() => {
        return null;
      }}
    />
  ),
};

BookmarkedMessageTemplate.args = {
  username: "호프(김문희)",
  date: parseMessageDateFromISO("2022-07-18T14:50:58.972493"),
  text: "안녕하세요? 저는 우아한테크코스 프론트엔드 4기 호프입니다",
  thumbnail: "https://avatars.githubusercontent.com/u/61469664?v=4",
  remindDate: "2022-12-25T14:56:58.972493",
  children: (
    <BookmarkButton
      isActive={true}
      onClick={() => {
        return null;
      }}
    />
  ),
};
