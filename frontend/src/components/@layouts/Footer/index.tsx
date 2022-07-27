import GithubIcon from "@public/assets/icons/GithubIcon.svg";
import YoutubeIcon from "@public/assets/icons/YoutubeIcon.svg";
import WrapperButton from "@src/components/@shared/WrapperButton";
import * as Styled from "./style";

function Footer() {
  return (
    <Styled.Container>
      <Styled.Description>Contact: rybshk@gmail.com</Styled.Description>
      <Styled.Description>©2022 pickpick.</Styled.Description>
      <Styled.IconContainer>
        <WrapperButton kind="smallIcon">
          <GithubIcon width="24px" height="24px" fill="#8B8B8B" />
        </WrapperButton>
        <WrapperButton kind="smallIcon">
          <YoutubeIcon width="24px" height="24px" fill="#8B8B8B" />
        </WrapperButton>
      </Styled.IconContainer>
    </Styled.Container>
  );
}

export default Footer;
