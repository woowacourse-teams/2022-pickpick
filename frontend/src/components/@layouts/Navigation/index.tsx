import * as Styled from "./style";
import { useEffect } from "react";
import { PATH_NAME, QUERY_KEY } from "@src/@constants";
import MenuIcon from "@public/assets/icons/MenuIcon.svg";
import StarIconUnfill from "@public/assets/icons/StarIcon-Unfill.svg";
import HomeIconUnfill from "@public/assets/icons/HomeIcon-Unfill.svg";
import AlarmIconInactive from "@public/assets/icons/AlarmIcon-Inactive.svg";
import InfoIcon from "@public/assets/icons/InfoIcon.svg";
import WrapperButton from "@src/components/@shared/WrapperButton";
import Dimmer from "@src/components/@shared/Dimmer";
import Portal from "@src/components/@shared/Portal";
import WrapperLink from "@src/components/@shared/WrapperLink";
import Drawer from "@src/components/Drawer";
import { useLocation } from "react-router-dom";
import { useQuery } from "react-query";
import { getSubscribedChannels } from "@src/api/channels";
import useModal from "@src/hooks/useModal";
import Button from "@src/components/@shared/Button";
import useAuthentication from "@src/hooks/useAuthentication";

function Navigation() {
  const { pathname } = useLocation();
  const { logout } = useAuthentication();

  const { data, refetch } = useQuery(
    QUERY_KEY.SUBSCRIBED_CHANNELS,
    getSubscribedChannels
  );

  const {
    isModalOpened: isMenuDrawerOpened,
    handleCloseModal: handleCloseDrawer,
    handleToggleModal: handleToggleDrawer,
  } = useModal(refetch);

  const {
    isModalOpened: isLogoutButtonOpened,
    handleCloseModal: handleCloseLogoutButton,
    handleToggleModal: handleToggleLogoutButton,
  } = useModal();

  const handleLogout = () => {
    handleCloseLogoutButton();
    logout();
  };

  useEffect(() => {
    handleCloseDrawer();
  }, [pathname]);

  return (
    <Styled.Container>
      <WrapperButton kind="bigIcon" onClick={handleToggleDrawer}>
        <MenuIcon width="24px" height="24px" fill="#121212" />
      </WrapperButton>

      <WrapperLink to={PATH_NAME.BOOKMARK} kind="bigIcon">
        {({ isActive }) => {
          return (
            <StarIconUnfill
              width="24px"
              height="24px"
              fill={isActive ? "#FF9900" : "#121212"}
            />
          );
        }}
      </WrapperLink>

      <WrapperLink to={PATH_NAME.FEED} kind="bigIcon">
        {({ isActive }) => {
          return (
            <HomeIconUnfill
              width="24px"
              height="24px"
              fill={isActive ? "#FF9900" : "#121212"}
            />
          );
        }}
      </WrapperLink>

      <WrapperLink to={PATH_NAME.ALARM} kind="bigIcon">
        {({ isActive }) => {
          return (
            <AlarmIconInactive
              width="24px"
              height="24px"
              fill={isActive ? "#FF9900" : "#121212"}
            />
          );
        }}
      </WrapperLink>

      <WrapperButton kind="bigIcon" onClick={handleToggleLogoutButton}>
        <InfoIcon width="24px" height="24px" fill="#121212" />
      </WrapperButton>

      <Portal isOpened={isMenuDrawerOpened}>
        <>
          <Dimmer hasBackgroundColor={true} onClick={handleCloseDrawer} />
          <Drawer
            channels={data?.channels}
            handleCloseDrawer={handleCloseDrawer}
          />
        </>
      </Portal>

      <Portal isOpened={isLogoutButtonOpened}>
        <>
          <Dimmer hasBackgroundColor={true} onClick={handleCloseLogoutButton} />
          <Styled.LogoutButtonContainer>
            <Button onClick={handleLogout}>로그아웃</Button>
          </Styled.LogoutButtonContainer>
        </>
      </Portal>
    </Styled.Container>
  );
}

export default Navigation;
