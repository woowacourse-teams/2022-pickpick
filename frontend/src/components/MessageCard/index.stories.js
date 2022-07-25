import MessageCard from ".";
import MockImage from "@public/assets/images/MockImage.png";

export default {
  title: "@shared/MessageCard",
  component: MessageCard,
};

const Template = (args) => <MessageCard {...args} />;

export const DefaultTemplate = Template.bind({});
DefaultTemplate.args = {
  username: "포코(장현석)",
  date: "오후 2:23",
  text: `어쩌구저쩌
어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구
 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구 어쩌구저쩌구`,
  thumbnail: MockImage,
};
