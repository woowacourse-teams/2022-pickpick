import DropdownToggle from ".";

export default {
  title: "@shared/DropdownToggle",
  component: DropdownToggle,
};

const Template = (args) => <DropdownToggle {...args} />;

export const DefaultTemplate = Template.bind({});
DefaultTemplate.args = {
  text: "Today",
};
