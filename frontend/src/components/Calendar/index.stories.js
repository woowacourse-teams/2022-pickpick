import Calendar from ".";

export default {
  title: "Component/Calendar",
  component: Calendar,
};

const Template = (args) => <Calendar {...args} />;

export const DefaultTemplate = Template.bind({});

DefaultTemplate.args = {};
