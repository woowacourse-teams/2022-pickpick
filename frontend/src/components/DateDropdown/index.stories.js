import DateDropdown from ".";

export default {
  title: "@Component/DateDropdown",
  component: DateDropdown,
};

const Template = (args) => <DateDropdown {...args} />;

export const DefaultTemplate = Template.bind({});

DefaultTemplate.args = {
  postedDate: "2022-07-18T14:50:58.972493",
};
