import { useTheme } from "styled-components";

import Button from "@src/components/@shared/Button";
import Modal from "@src/components/@shared/Modal";
import WrapperButton from "@src/components/@shared/WrapperButton";
import WrapperLink from "@src/components/@shared/WrapperLink";
import HomeIcon from "@src/components/@svgIcons/HomeIcon";
import InfoIcon from "@src/components/@svgIcons/InfoIcon";
import MenuIcon from "@src/components/@svgIcons/MenuIcon";
import ReminderIconInactive from "@src/components/@svgIcons/ReminderIconInactive";
import StarIcon from "@src/components/@svgIcons/StarIcon";
import ChannelsDrawer from "@src/components/ChannelsDrawer";

import useGetSubscribedChannels from "@src/hooks/@query/useGetSubscribedChannels";
import useModal from "@src/hooks/@shared/useModal";
import useOuterClick from "@src/hooks/@shared/useOuterClick";
import useAuthentication from "@src/hooks/useAuthentication";
import useRecentFeedPath from "@src/hooks/useRecentFeedPath";

import { PATH_NAME } from "@src/@constants";
import { Theme } from "@src/@types/shared";

import * as Styled from "./style";

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

  const [menuIconInnerRef, drawerInnerRef] = useOuterClick({
    callback: handleCloseDrawer,
    requiredInnerRefCount: 2,
  });

  const [logoutButtonInnerRef] = useOuterClick({
    callback: handleCloseLogoutButton,
    requiredInnerRefCount: 1,
  });

  const handleLogout = () => {
    handleCloseLogoutButton();
    logout();
  };

  return (
    <Styled.Container>
      <div ref={menuIconInnerRef}>
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

      <Modal isOpened={isMenuDrawerOpened} handleCloseModal={handleCloseDrawer}>
        <div ref={drawerInnerRef}>
          <ChannelsDrawer
            channels={data?.channels}
            handleCloseDrawer={handleCloseDrawer}
          />
        </div>
      </Modal>

      <Modal
        isOpened={isLogoutButtonOpened}
        handleCloseModal={handleCloseLogoutButton}
      >
        <Styled.LogoutButtonContainer>
          <Button onClick={handleLogout}>로그아웃</Button>
        </Styled.LogoutButtonContainer>
      </Modal>
    </Styled.Container>
  );
}

export default Navigation;
