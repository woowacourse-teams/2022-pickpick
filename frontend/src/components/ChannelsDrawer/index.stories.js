import ChannelsDrawer from ".";
import { subscribedChannels } from "@src/mocks/data/channels";

export default {
  title: "Component/ChannelsDrawer",
  component: ChannelsDrawer,
};

const Template = (args) => <ChannelsDrawer {...args} />;

export const DefaultTemplate = Template.bind({});

DefaultTemplate.args = {
  channels: subscribedChannels,
};
