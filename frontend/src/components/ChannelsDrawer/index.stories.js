import { subscribedChannels } from "@src/mocks/data/channels";

import ChannelsDrawer from ".";

export default {
  title: "Component/ChannelsDrawer",
  component: ChannelsDrawer,
  argTypes: {
    handleCloseDrawer: {
      control: false,
    },
  },
};

const Template = (args) => <ChannelsDrawer {...args} />;

export const DefaultTemplate = Template.bind({});

DefaultTemplate.args = {
  channels: subscribedChannels,
};
