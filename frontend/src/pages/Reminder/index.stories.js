import { Main } from "@src/components/@layouts/LayoutContainer/style.ts";

import Reminder from ".";

export default {
  title: "Pages/Reminder",
  component: Reminder,
};

const Template = (args) => (
  <Main>
    <Reminder {...args} />
  </Main>
);

export const DefaultTemplate = Template.bind({});
