import Button from ".";

export default {
  title: "@shared/Button",
  component: Button,
};

const Template = (args) => <Button {...args} />;

export const DefaultTemplate = Template.bind({});
DefaultTemplate.args = {
  children: "시작하기",
};
