import UnexpectedError from ".";

export default {
  title: "Pages/UnexpectedError",
  component: UnexpectedError,
};

const Template = (args) => <UnexpectedError {...args} />;

export const DefaultTemplate = Template.bind({});
