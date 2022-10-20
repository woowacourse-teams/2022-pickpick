import Home from ".";

export default {
  title: "Pages/Home",
  component: Home,
};

const Template = (args) => <Home {...args} />;

export const DefaultTemplate = Template.bind({});
