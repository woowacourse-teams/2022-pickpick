import { Main } from "@src/components/@layouts/LayoutContainer/style.ts";

import SearchResult from ".";

export default {
  title: "Pages/SearchResult",
  component: SearchResult,
};

const Template = (args) => (
  <Main>
    <SearchResult {...args} />
  </Main>
);

export const DefaultTemplate = Template.bind({});
