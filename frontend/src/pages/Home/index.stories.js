import { Main } from "@src/components/@layouts/LayoutContainer/style.ts";

import Home from ".";

export default {
  title: "Pages/Home",
  component: Home,
};

const Template = (args) => (
  <Main>
    <Home {...args} />
  </Main>
);

export const DefaultTemplate = Template.bind({});
