import Header from "@src/components/layouts/Header";
import Footer from "@src/components/layouts/Footer";
import * as Styled from "./style";
import Navigation from "../Navigation";
import { useLocation } from "react-router-dom";

interface Props {
  children: JSX.Element;
}

function LayoutContainer({ children }: Props) {
  const { pathname } = useLocation();

  const hasHeader = () => pathname === "/";

  return (
    <Styled.Container>
      {hasHeader() && <Header />}
      <Styled.Main hasMarginTop={hasHeader()}>{children}</Styled.Main>
      <Footer />
      <Navigation />
    </Styled.Container>
  );
}

export default LayoutContainer;
