import DateDropdownMenu from ".";

export default {
  title: "@Component/DateDropdownMenu",
  component: DateDropdownMenu,
};

const Template = (args) => <DateDropdownMenu {...args} />;

export const DefaultTemplate = Template.bind({});

DefaultTemplate.args = {
  date: "어제",
};
