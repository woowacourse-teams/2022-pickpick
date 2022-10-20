import AddReminder from ".";

export default {
  title: "@component/AddReminder",
  component: AddReminder,
  argTypes: {
    messageId: {
      control: false,
    },
    handleCloseReminderModal: {
      control: false,
    },
    remindDate: {
      control: false,
    },
    refetchFeed: {
      control: false,
    },
  },
};

const Template = (args) => {
  return <AddReminder {...args} />;
};

export const DefaultTemplate = Template.bind({
  messageId: "1",
  remindDate: "2022-09-22T22:50:00",
  handleCloseReminderModal: () => {
    return null;
  },
  refetchFeed: () => {
    return null;
  },
});
