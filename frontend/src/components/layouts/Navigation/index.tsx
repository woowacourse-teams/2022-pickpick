import * as Styled from "./style";
import { useState, useEffect } from "react";
import MenuIcon from "@public/assets/icons/MenuIcon.svg";
import StarIconUnfill from "@public/assets/icons/StarIcon-Unfill.svg";
import HomeIconUnfill from "@public/assets/icons/HomeIcon-Unfill.svg";
import AlarmIconInactive from "@public/assets/icons/AlarmIcon-Inactive.svg";
import InfoIcon from "@public/assets/icons/InfoIcon.svg";
import WrapperButton from "@src/components/@shared/WrapperButton";
import Dimmer from "@src/components/@shared/Dimmer";
import Portal from "@src/components/@shared/Portal";
import Drawer from "@src/components/Drawer";

function Navigation() {
  const [isMenuDrawerOpened, setIsMenuDrawerOpened] = useState(false);

  const handleCloseDrawer = () => {
    setIsMenuDrawerOpened(false);
  };

  const handleToggleDrawer = () => {
    setIsMenuDrawerOpened((prev) => !prev);
  };

  useEffect(() => {
    if (isMenuDrawerOpened) {
      document.body.style.overflowY = "hidden";
      return;
    }
    document.body.style.overflowY = "auto";
  }, [isMenuDrawerOpened]);

  return (
    <Styled.Container>
      <WrapperButton kind="bigIcon" onClick={handleToggleDrawer}>
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

      <Portal isOpened={isMenuDrawerOpened}>
        <>
          <Dimmer hasBackgroundColor={true} onClick={handleCloseDrawer} />
          <Drawer />
        </>
      </Portal>
    </Styled.Container>
  );
}

export default Navigation;
