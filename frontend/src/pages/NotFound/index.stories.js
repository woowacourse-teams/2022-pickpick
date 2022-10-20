import { Main } from "@src/components/@layouts/LayoutContainer/style.ts";

import NotFound from ".";

export default {
  title: "Pages/NotFound",
  component: NotFound,
};

const Template = (args) => (
  <Main>
    <NotFound {...args} />
  </Main>
);

export const DefaultTemplate = Template.bind({});
