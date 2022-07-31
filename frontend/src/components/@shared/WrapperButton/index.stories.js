import WrapperButton from ".";
import GithubIcon from "@public/assets/icons/GithubIcon.svg";

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

export const DefaultTemplate = Template.bind({});
DefaultTemplate.args = {
  children: <GithubIcon width="24px" height="24px" fill="#8B8B8B" />,
};
