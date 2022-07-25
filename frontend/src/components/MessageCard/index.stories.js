import MessageCard from ".";
import MockImage from "@public/assets/images/MockImage.png";

export default {
  title: "@Component/MessageCard",
  component: MessageCard,
};

const Template = (args) => <MessageCard {...args} />;

export const DefaultTemplate = Template.bind({});
DefaultTemplate.args = {
  username: "포코(장현석)",
  date: "2022-07-18T14:50:58.972493",
  text: "메시지 입니다.",
  thumbnail: MockImage,
};
