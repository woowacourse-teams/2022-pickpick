import { rest } from "msw";

import { subscribedChannels } from "@src/mocks/data/channels";

import Navigation from ".";

export default {
  title: "@layouts/Navigation",
  component: Navigation,
};

const Template = (args) => <Navigation {...args} />;

export const DefaultTemplate = Template.bind({});
