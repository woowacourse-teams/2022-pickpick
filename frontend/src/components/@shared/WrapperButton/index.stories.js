import GithubIcon from "@src/components/@svgIcons/GithubIcon";

import WrapperButton from ".";

export default {
  title: "@shared/WrapperButton",
  component: WrapperButton,
  argTypes: {
    children: {
      control: false,
    },
  },
};

const Template = (args) => <WrapperButton {...args} />;

export const BigIconButtonTemplate = Template.bind({});
export const SmallIconButtonTemplate = Template.bind({});
export const TextButtonTemplate = Template.bind({});

BigIconButtonTemplate.args = {
  children: <GithubIcon width="24px" height="24px" fill="#8B8B8B" />,
  kind: "bigIcon",
};

SmallIconButtonTemplate.args = {
  children: <GithubIcon width="24px" height="24px" fill="#8B8B8B" />,
  kind: "smallIcon",
};

TextButtonTemplate.args = {
  children: "Text Button",
};
