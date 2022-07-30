import Drawer from ".";
import { subscribedChannels } from "@src/mocks/data/channels";

export default {
  title: "Component/Drawer",
  component: Drawer,
};

const Template = (args) => <Drawer {...args} />;

export const DefaultTemplate = Template.bind({});

DefaultTemplate.args = {
  channels: subscribedChannels,
};
