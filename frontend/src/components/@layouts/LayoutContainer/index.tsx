import Header from "@src/components/@layouts/Header";
import Footer from "@src/components/@layouts/Footer";
import * as Styled from "./style";
import Navigation from "../Navigation";
import { useLocation } from "react-router-dom";
import { Outlet } from "react-router-dom";
import { PATH_NAME } from "@src/@constants";

function LayoutContainer() {
  const { pathname } = useLocation();

  const hasHeader = () => pathname === PATH_NAME.HOME;
  const hasNavBar = () => pathname !== PATH_NAME.HOME;

  return (
    <Styled.Container>
      {hasHeader() && <Header />}
      <Styled.Main hasMarginTop={hasHeader()}>
        <Outlet />
      </Styled.Main>
      <Footer />
      {hasNavBar() && <Navigation />}
    </Styled.Container>
  );
}

export default LayoutContainer;
