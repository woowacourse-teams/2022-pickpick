import { Main } from "@src/components/@layouts/LayoutContainer/style.ts";

import SpecificDateFeed from ".";

export default {
  title: "Pages/SpecificDateFeed",
  component: SpecificDateFeed,
};

const Template = (args) => (
  <Main>
    <SpecificDateFeed {...args} />
  </Main>
);

export const DefaultTemplate = Template.bind({});
