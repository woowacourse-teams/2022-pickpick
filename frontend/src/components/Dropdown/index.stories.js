import Dropdown from ".";

export default {
  title: "@shared/Dropdown",
  component: Dropdown,
};

const Template = (args) => <Dropdown {...args} />;

export const DefaultTemplate = Template.bind({});

DefaultTemplate.args = {};
