import ProfileImage from ".";

export default {
  title: "@Component/ProfileImage",
  component: ProfileImage,
};

const Template = (args) => <ProfileImage {...args} />;

export const DefaultTemplate = Template.bind({});
DefaultTemplate.args = {
  src: "https://avatars.githubusercontent.com/u/61469664?v=4",
};
