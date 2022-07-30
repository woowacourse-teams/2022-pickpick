import ProfileImage from ".";
import MockImage from "@public/assets/images/MockImage.png";

export default {
  title: "@Component/ProfileImage",
  component: ProfileImage,
};

const Template = (args) => <ProfileImage {...args} />;

export const DefaultTemplate = Template.bind({});
DefaultTemplate.args = {
  src: MockImage,
};
