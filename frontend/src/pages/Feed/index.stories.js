import Feed from ".";

export default {
  title: "Pages/Feed",
  component: Feed,
};

const Template = (args) => <Feed {...args} />;

export const DefaultTemplate = Template.bind({});
