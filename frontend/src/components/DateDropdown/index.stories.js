import DateDropdown from ".";

export default {
  title: "@Component/DateDropdown",
  component: DateDropdown,
  argTypes: {
    channelId: {
      control: false,
    },
    handleOpenCalendar: {
      control: false,
    },
  },
};

const Template = (args) => <DateDropdown {...args} />;

const todayISO = new Date().toISOString();
const yesterdayISO = new Date(
  new Date().setDate(new Date().getDate() - 1)
).toISOString();

export const TodayTemplate = Template.bind({});

export const YesterdayTemplate = Template.bind({});

export const SpecificDateTemplate = Template.bind({});

TodayTemplate.args = {
  postedDate: todayISO,
};

YesterdayTemplate.args = {
  postedDate: yesterdayISO,
};

SpecificDateTemplate.args = {
  postedDate: "2022-01-18T14:48:00.000Z",
};
