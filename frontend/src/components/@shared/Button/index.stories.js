import Button from ".";

export default {
  title: "@shared/Button",
  component: Button,
};

const Template = (args) => <Button {...args} />;

export const DefaultTemplate = Template.bind({});
DefaultTemplate.args = {
  children: "μμνκΈ°",
};
