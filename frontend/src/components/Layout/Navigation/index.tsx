import * as Styled from "./style";
import MenuIcon from "@public/assets/icons/MenuIcon.svg";
import StarIconUnfill from "@public/assets/icons/StarIcon-Unfill.svg";
import HomeIconUnfill from "@public/assets/icons/HomeIcon-Unfill.svg";
import AlarmIconUnactive from "@public/assets/icons/AlarmIcon-Unactive.svg";
import InfoIcon from "@public/assets/icons/InfoIcon.svg";

function Navigation() {
  return (
    <Styled.Container>
      <MenuIcon with="24px" height="24px" fill="#121212" />
      <StarIconUnfill with="24px" height="24px" fill="#121212" />
      <HomeIconUnfill with="24px" height="24px" fill="#121212" />
      <AlarmIconUnactive with="24px" height="24px" fill="#121212" />
      <InfoIcon with="24px" height="24px" fill="#121212" />
    </Styled.Container>
  );
}

export default Navigation;
