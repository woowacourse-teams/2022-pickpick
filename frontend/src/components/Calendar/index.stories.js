import Calendar from ".";

export default {
  title: "Component/Calendar",
  component: Calendar,
  argTypes: {
    channelId: {
      control: false,
    },
    handleCloseCalendar: {
      control: false,
    },
  },
};

const Template = (args) => <Calendar {...args} />;

export const DefaultTemplate = Template.bind({});

DefaultTemplate.args = {};
