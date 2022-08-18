import { PropsWithChildren } from "react";
import * as Styled from "./style";
import Header from "@src/components/@layouts/Header";
import Footer from "@src/components/@layouts/Footer";
import Navigation from "@src/components/@layouts/Navigation";
import { useLocation } from "react-router-dom";
import { PATH_NAME } from "@src/@constants";

function LayoutContainer({ children }: PropsWithChildren) {
  const { pathname } = useLocation();

  const hasHeader = () => pathname === PATH_NAME.HOME;
  const hasNavBar = () => {
    if (pathname === PATH_NAME.HOME) return false;
    if (pathname === PATH_NAME.ADD_CHANNEL) return false;

    return true;
  };

  return (
    <Styled.Container>
      {hasHeader() && <Header />}
      <Styled.Main hasMarginTop={hasHeader()}>{children}</Styled.Main>
      <Footer />
      {hasNavBar() && <Navigation />}
    </Styled.Container>
  );
}

export default LayoutContainer;
