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

export const CreateNewReminderTemplate = Template.bind({
  messageId: "1",
  remindDate: "2022-09-22T22:50:00",
  handleCloseReminderModal: () => {
    return null;
  },
  refetchFeed: () => {
    return null;
  },
});

export const ModifyReminderTemplate = Template.bind({});

ModifyReminderTemplate.args = {
  messageId: "1",
  remindDate: "2022-12-25T22:10:00",
  handleCloseReminderModal: () => {
    return null;
  },
  refetchFeed: () => {
    return null;
  },
};
