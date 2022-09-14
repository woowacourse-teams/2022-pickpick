import { createGlobalStyle, css } from "styled-components";

import RobotoBold from "@public/assets/fonts/Roboto-Bold.woff";
import RobotoBoldItalic from "@public/assets/fonts/Roboto-BoldItalic.woff";
import RobotoRegular from "@public/assets/fonts/Roboto-Regular.woff";
import RobotoItalic from "@public/assets/fonts/Roboto-Italic.woff";
import RobotoLight from "@public/assets/fonts/Roboto-Light.woff";
import RobotoLightItalic from "@public/assets/fonts/Roboto-LightItalic.woff";
import Twayair from "@public/assets/fonts/Twayair.woff";

import { Theme } from "@src/@types/shared";

const GlobalStyle = createGlobalStyle<{ theme: Theme }>`
  @font-face {
    font-family: 'Roboto';
    src: url(${RobotoBold}) format('woff'),
         url(${RobotoBoldItalic}) format('woff'),
         url(${RobotoItalic}) format('woff'),
         url(${RobotoLight}) format('woff'),
         url(${RobotoLightItalic}) format('woff'),
         url(${RobotoRegular}) format('woff');
  }

  @font-face {
    font-family: 'Twayair';
    src: url(${Twayair}) format('woff');
  }

  *,
  *::before,
  *::after {
    box-sizing: border-box;
    letter-spacing: -0.4px;

    ${({ theme }) => css`
      font-family: ${theme.FONT.PRIMARY};
      color: ${theme.COLOR.TEXT.DEFAULT};
    `}
  }

  body,
  h1,
  h2,
  h3,
  h4,
  p,
  figure,
  blockquote,
  dl,
  dd,
  ul,
  ol {
    margin: 0;
    padding: 0;
  }

  ul{
    list-style: none;
  }

  a {
    text-decoration: none;
    color: inherit;
  }

  input,
  button,
  textarea,
  select {
    font: inherit;
  }

  body {
    ${({ theme }: { theme: Theme }) => css`
      background-color: ${theme.COLOR.BACKGROUND.PRIMARY};
    `}
  }
`;

export default GlobalStyle;
