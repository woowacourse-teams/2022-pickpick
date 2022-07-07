import * as Styled from "./style";
import MenuIcon from "@public/assets/icons/MenuIcon.svg";
import StarIconUnfill from "@public/assets/icons/StarIcon-Unfill.svg";
import HomeIconUnfill from "@public/assets/icons/HomeIcon-Unfill.svg";
import AlarmIconInactive from "@public/assets/icons/AlarmIcon-Inactive.svg";
import InfoIcon from "@public/assets/icons/InfoIcon.svg";
import WrapperButton from "@src/components/@shared/WrapperButton";

function Navigation() {
  return (
    <Styled.Container>
      <WrapperButton kind="bigIcon">
        <MenuIcon width="24px" height="24px" fill="#121212" />
      </WrapperButton>
      <WrapperButton kind="bigIcon">
        <StarIconUnfill width="24px" height="24px" fill="#121212" />
      </WrapperButton>
      <WrapperButton kind="bigIcon">
        <HomeIconUnfill width="24px" height="24px" fill="#121212" />
      </WrapperButton>
      <WrapperButton kind="bigIcon">
        <AlarmIconInactive width="24px" height="24px" fill="#121212" />
      </WrapperButton>
      <WrapperButton kind="bigIcon">
        <InfoIcon width="24px" height="24px" fill="#121212" />
      </WrapperButton>
    </Styled.Container>
  );
}

export default Navigation;
