import IconButton from ".";
import RemoveIcon from "@public/assets/icons/RemoveIcon.svg";
import { FlexRow } from "@src/@styles/shared";
import { ButtonText } from "@src/components/MessageCard/style";

export default {
  title: "@shared/IconButton",
  component: IconButton,
};

const Template = (args) => <IconButton {...args} />;

export const DefaultTemplate = Template.bind({});
DefaultTemplate.args = {
  children: (
    <FlexRow justifyContent="center" alignItems="center" gap="5px">
      <ButtonText>삭제</ButtonText>
      <RemoveIcon width="12px" height="12px" fill="#ffffff" />
    </FlexRow>
  ),
  isActive: true,
};
