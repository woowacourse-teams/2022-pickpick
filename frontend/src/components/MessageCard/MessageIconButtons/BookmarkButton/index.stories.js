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

export const ActiveTemplate = Template.bind({});
export const InActiveTemplate = Template.bind({});

ActiveTemplate.args = {
  isActive: true,
};

InActiveTemplate.args = {
  isActive: false,
};
