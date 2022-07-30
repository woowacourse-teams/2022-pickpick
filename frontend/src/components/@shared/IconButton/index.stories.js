import IconButton from ".";
import StarIconUnfill from "@public/assets/icons/StarIcon-Unfill.svg";
import AlarmIconActive from "@public/assets/icons/AlarmIcon-Active.svg";
import { FlexRow } from "@src/@styles/shared";
import { ButtonText } from "@src/components/MessageCard/style";

export default {
  title: "@shared/IconButton",
  component: IconButton,
  argTypes: {
    children: {
      control: false,
    },
  },
};

const Template = (args) => <IconButton {...args} />;

export const AlarmTemplate = Template.bind({});

export const BookmarkTemplate = Template.bind({});

AlarmTemplate.args = {
  children: (
    <FlexRow justifyContent="center" alignItems="center" gap="5px">
      <ButtonText>알람설정</ButtonText>
      <AlarmIconActive width="12px" height="12px" fill="#ffffff" />
    </FlexRow>
  ),
  icon: "alarm",
  isActive: true,
};

BookmarkTemplate.args = {
  children: (
    <FlexRow justifyContent="center" alignItems="center" gap="5px">
      <ButtonText>즐겨찾기</ButtonText>
      <StarIconUnfill width="12px" height="13.3px" fill="#ffffff" />
    </FlexRow>
  ),
  icon: "star",
  isActive: true,
};
