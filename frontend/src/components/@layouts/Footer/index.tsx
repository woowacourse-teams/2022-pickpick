import GithubIcon from "@public/assets/icons/GithubIcon.svg";
import WrapperButton from "@src/components/@shared/WrapperButton";
import * as Styled from "./style";

function Footer() {
  return (
    <Styled.Container>
      <Styled.Description>©2022 pickpick.</Styled.Description>
      <Styled.IconContainer>
        <a
          href="https://github.com/woowacourse-teams/2022-pickpick"
          target="_blank"
          rel="noreferrer"
        >
          <WrapperButton kind="smallIcon">
            <GithubIcon width="24px" height="24px" fill="#8B8B8B" />
          </WrapperButton>
        </a>
      </Styled.IconContainer>
    </Styled.Container>
  );
}

export default Footer;
