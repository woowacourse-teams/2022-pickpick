import MessageCardSkeleton from ".";

export default {
  title: "@Component/MessageCardSkeleton",
  component: MessageCardSkeleton,
};

const Template = (args) => <MessageCardSkeleton {...args} />;

export const DefaultTemplate = Template.bind({});
DefaultTemplate.args = {};
