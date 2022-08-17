import ReminderButton from ".";

export default {
  title: "@component/ReminderButton",
  component: ReminderButton,
  argTypes: {
    onClick: {
      control: false,
    },
  },
};

const Template = (args) => <ReminderButton {...args} />;

export const DefaultTemplate = Template.bind({});
