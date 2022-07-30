import Header from "@src/components/@layouts/Header";
import Footer from "@src/components/@layouts/Footer";
import * as Styled from "./style";
import Navigation from "../Navigation";
import { useLocation } from "react-router-dom";
import { Outlet } from "react-router-dom";

function LayoutContainer() {
  const { pathname } = useLocation();

  const hasHeader = () => pathname === "/" || pathname === "/addChannel";

  return (
    <div>
      {hasHeader() && <Header />}
      <Styled.Main hasMarginTop={hasHeader()}>
        <Outlet />
      </Styled.Main>
      <Footer />
      <Navigation />
    </div>
  );
}

export default LayoutContainer;
