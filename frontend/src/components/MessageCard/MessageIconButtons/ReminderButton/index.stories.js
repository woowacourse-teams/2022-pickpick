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

export const ActiveTemplate = Template.bind({});
export const InActiveTemplate = Template.bind({});

ActiveTemplate.args = {
  isActive: true,
};

InActiveTemplate.args = {
  isActive: false,
};
