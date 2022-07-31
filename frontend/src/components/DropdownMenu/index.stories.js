import DropdownMenu from ".";

export default {
  title: "@Component/DropdownMenu",
  component: DropdownMenu,
};

const Template = (args) => <DropdownMenu {...args} />;

export const DefaultTemplate = Template.bind({});

DefaultTemplate.args = {
  date: "어제",
};
