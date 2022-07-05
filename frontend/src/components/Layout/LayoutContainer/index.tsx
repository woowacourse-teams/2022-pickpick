import Header from "@src/components/Layout/Header";
import Footer from "@src/components/Layout/Footer";
import * as Styled from "./style";
import Navigation from "../Navigation";

interface Props {
  children: JSX.Element;
}

function LayoutContainer({ children }: Props) {
  return (
    <Styled.Container>
      <Header />
      <Styled.Main>{children}</Styled.Main>
      <Footer />
      <Navigation />
    </Styled.Container>
  );
}

export default LayoutContainer;
