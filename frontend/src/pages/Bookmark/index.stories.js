import { Main } from "@src/components/@layouts/LayoutContainer/style.ts";

import Bookmark from ".";

export default {
  title: "Pages/Bookmark",
  component: Bookmark,
};

const Template = (args) => (
  <Main>
    <Bookmark {...args} />
  </Main>
);

export const DefaultTemplate = Template.bind({});
