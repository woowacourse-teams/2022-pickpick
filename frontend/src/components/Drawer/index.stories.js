import Drawer from ".";

export default {
  title: "Component/Drawer",
  component: Drawer,
};

const Template = (args) => <Drawer {...args} />;

export const DefaultTemplate = Template.bind({});

DefaultTemplate.args = {};
