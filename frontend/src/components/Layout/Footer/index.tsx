import GithubIcon from "@public/assets/icons/GithubIcon.svg";
import YoutubeIcon from "@public/assets/icons/YoutubeIcon.svg";
import * as Styled from "./style";

function Footer() {
  return (
    <Styled.Container>
      <Styled.Description>Contact: rybshk@gmail.com</Styled.Description>
      <Styled.Description>©2022 pickpick.</Styled.Description>
      <Styled.IconContainer>
        <GithubIcon />
        <YoutubeIcon />
      </Styled.IconContainer>
    </Styled.Container>
  );
}

export default Footer;
