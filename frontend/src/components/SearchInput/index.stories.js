import SearchInput from ".";

export default {
  title: "@Component/SearchInput",
  component: SearchInput,
};

const Template = (args) => <SearchInput {...args} />;

export const DefaultTemplate = Template.bind({});
DefaultTemplate.args = {
  placeholder: "검색 할 키워드를 입력해주세요.",
};
