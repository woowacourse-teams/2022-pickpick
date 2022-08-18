import * as Styled from "./style";
import { PATH_NAME } from "@src/@constants";
import MenuIcon from "@public/assets/icons/MenuIcon.svg";
import StarIcon from "@public/assets/icons/StarIcon.svg";
import HomeIcon from "@public/assets/icons/HomeIcon.svg";
import ReminderIconInactive from "@public/assets/icons/ReminderIcon-Inactive.svg";
import InfoIcon from "@public/assets/icons/InfoIcon.svg";
import WrapperButton from "@src/components/@shared/WrapperButton";
import Dimmer from "@src/components/@shared/Dimmer";
import Portal from "@src/components/@shared/Portal";
import WrapperLink from "@src/components/@shared/WrapperLink";
import Drawer from "@src/components/Drawer";
import useModal from "@src/hooks/useModal";
import Button from "@src/components/@shared/Button";
import useAuthentication from "@src/hooks/useAuthentication";
import { useTheme } from "styled-components";
import { Theme } from "@src/@types/shared";
import useGetSubscribedChannels from "@src/hooks/useGetSubscribedChannels";
import useRecentFeedPath from "@src/hooks/useRecentFeedPath";
import useOuterClick from "@src/hooks/useOuterClick";

function Navigation() {
  const { logout } = useAuthentication();
  const theme = useTheme() as Theme;
  const { getRecentFeedPath } = useRecentFeedPath();
  const { data, refetch } = useGetSubscribedChannels();

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

  const { innerRef: drawerInnerRef } = useOuterClick(handleCloseDrawer);
  const { innerRef: logoutButtonInnerRef } = useOuterClick(
    handleCloseLogoutButton
  );

  const handleLogout = () => {
    handleCloseLogoutButton();
    logout();
  };

  return (
    <Styled.Container>
      <div ref={drawerInnerRef}>
        <WrapperButton kind="bigIcon" onClick={handleToggleDrawer}>
          <MenuIcon
            width="24px"
            height="24px"
            fill={theme.COLOR.TEXT.DEFAULT}
          />
        </WrapperButton>
      </div>

      <WrapperLink to={PATH_NAME.BOOKMARK} kind="bigIcon">
        {({ isActive }) => {
          return (
            <StarIcon
              width="24px"
              height="24px"
              fill={
                isActive
                  ? theme.COLOR.PRIMARY.DEFAULT
                  : theme.COLOR.TEXT.DEFAULT
              }
            />
          );
        }}
      </WrapperLink>

      <WrapperLink to={getRecentFeedPath() ?? PATH_NAME.FEED} kind="bigIcon">
        {({ isActive }) => {
          return (
            <HomeIcon
              width="24px"
              height="24px"
              fill={
                isActive
                  ? theme.COLOR.PRIMARY.DEFAULT
                  : theme.COLOR.TEXT.DEFAULT
              }
            />
          );
        }}
      </WrapperLink>

      <WrapperLink to={PATH_NAME.REMINDER} kind="bigIcon">
        {({ isActive }) => {
          return (
            <ReminderIconInactive
              width="24px"
              height="24px"
              fill={
                isActive
                  ? theme.COLOR.PRIMARY.DEFAULT
                  : theme.COLOR.TEXT.DEFAULT
              }
            />
          );
        }}
      </WrapperLink>

      <div ref={logoutButtonInnerRef}>
        <WrapperButton kind="bigIcon" onClick={handleToggleLogoutButton}>
          <InfoIcon
            width="24px"
            height="24px"
            fill={theme.COLOR.TEXT.DEFAULT}
          />
        </WrapperButton>
      </div>

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
