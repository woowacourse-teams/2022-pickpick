import Reminder from ".";

export default {
  title: "Pages/Reminder",
  component: Reminder,
};

const Template = (args) => <Reminder {...args} />;

export const DefaultTemplate = Template.bind({});
