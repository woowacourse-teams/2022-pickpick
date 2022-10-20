import SearchForm from ".";

export default {
  title: "@component/SearchForm",
  component: SearchForm,
};

const Template = (args) => {
  return <SearchForm {...args} />;
};

export const DefaultTemplate = Template.bind({});

DefaultTemplate.args = {
  currentChannelIds: [],
};
