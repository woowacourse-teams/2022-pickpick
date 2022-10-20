import NotFound from ".";

export default {
  title: "Pages/NotFound",
  component: NotFound,
};

const Template = (args) => <NotFound {...args} />;

export const DefaultTemplate = Template.bind({});
