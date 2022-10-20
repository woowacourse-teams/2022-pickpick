import Bookmark from ".";

export default {
  title: "Pages/Bookmark",
  component: Bookmark,
};

const Template = (args) => <Bookmark {...args} />;

export const DefaultTemplate = Template.bind({});
