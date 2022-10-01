import { subscribedChannels } from "@src/mocks/data/channels";

import ChannelsDrawer from ".";

export default {
  title: "Component/ChannelsDrawer",
  component: ChannelsDrawer,
};

const Template = (args) => <ChannelsDrawer {...args} />;

export const DefaultTemplate = Template.bind({});

DefaultTemplate.args = {
  channels: subscribedChannels,
};
