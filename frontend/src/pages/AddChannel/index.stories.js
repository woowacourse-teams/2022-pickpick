import { Main } from "@src/components/@layouts/LayoutContainer/style.ts";

import AddChannel from ".";

export default {
  title: "Pages/AddChannel",
  component: AddChannel,
};

const Template = (args) => (
  <Main>
    <AddChannel {...args} />
  </Main>
);

export const DefaultTemplate = Template.bind({});
