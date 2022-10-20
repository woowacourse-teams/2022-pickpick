import Button from ".";

export default {
  title: "@shared/Button",
  component: Button,
};

const Template = (args) => <Button {...args} />;

export const PrimaryTemplate = Template.bind({});
export const SecondaryTemplate = Template.bind({});
export const TertiaryTemplate = Template.bind({});

PrimaryTemplate.args = {
  styleType: "primary",
  children: "시작하기",
};

SecondaryTemplate.args = {
  styleType: "secondary",
  children: "시작하기",
};

TertiaryTemplate.args = {
  styleType: "tertiary",
  children: "시작하기",
};
