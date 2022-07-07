import Header from "@src/components/layouts/Header";
import Footer from "@src/components/layouts/Footer";
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
