import { Main } from "@src/components/@layouts/LayoutContainer/style.ts";

import UnexpectedError from ".";

export default {
  title: "Pages/UnexpectedError",
  component: UnexpectedError,
};

const Template = (args) => (
  <Main>
    <UnexpectedError {...args} />
  </Main>
);

export const DefaultTemplate = Template.bind({});
