import Header from ".";

export default {
  title: "@layouts/Header",
  component: Header,
};

const Template = (args) => <Header {...args} />;

export const DefaultTemplate = Template.bind({});
