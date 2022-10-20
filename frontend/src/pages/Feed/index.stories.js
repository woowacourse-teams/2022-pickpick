import { Main } from "@src/components/@layouts/LayoutContainer/style.ts";

import Feed from ".";

export default {
  title: "Pages/Feed",
  component: Feed,
};

const Template = (args) => (
  <Main>
    <Feed {...args} />
  </Main>
);

export const DefaultTemplate = Template.bind({});
