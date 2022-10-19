import DateDropdownToggle from ".";

export default {
  title: "@Component/DateDropdownToggle",
  component: DateDropdownToggle,
};

const Template = (args) => <DateDropdownToggle {...args} />;

export const DefaultTemplate = Template.bind({});
DefaultTemplate.args = {
  text: "Today",
};
