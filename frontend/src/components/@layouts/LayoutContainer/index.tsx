import { PropsWithChildren } from "react";
import * as Styled from "./style";
import Header from "@src/components/@layouts/Header";
import Navigation from "@src/components/@layouts/Navigation";
import { useLocation } from "react-router-dom";
import { PATH_NAME } from "@src/@constants";

function LayoutContainer({ children }: PropsWithChildren) {
  const { pathname } = useLocation();

  const hasHeader = () => pathname === PATH_NAME.HOME;
  const hasNavBar = () =>
    pathname !== PATH_NAME.HOME && pathname !== PATH_NAME.ADD_CHANNEL;

  return (
    <Styled.Container>
      {hasHeader() && <Header />}
      <Styled.Main hasMarginTop={hasHeader()}>{children}</Styled.Main>
      {hasNavBar() && <Navigation />}
    </Styled.Container>
  );
}

export default LayoutContainer;
