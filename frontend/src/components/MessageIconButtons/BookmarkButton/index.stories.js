import BookmarkButton from ".";

export default {
  title: "@component/BookmarkButton",
  component: BookmarkButton,
  argTypes: {
    onClick: {
      control: false,
    },
  },
};

const Template = (args) => <BookmarkButton {...args} />;

export const DefaultTemplate = Template.bind({});
