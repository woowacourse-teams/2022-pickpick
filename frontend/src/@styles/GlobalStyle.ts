import { createGlobalStyle, css } from "styled-components";

import RobotoBoldTtf from "@public/assets/fonts/Roboto-Bold.ttf";
import RobotoBoldWoff from "@public/assets/fonts/Roboto-Bold.woff";
import RobotoBoldWoff2 from "@public/assets/fonts/Roboto-Bold.woff2";
import RobotoLightTtf from "@public/assets/fonts/Roboto-Light.ttf";
import RobotoLightWoff from "@public/assets/fonts/Roboto-Light.woff";
import RobotoLightWoff2 from "@public/assets/fonts/Roboto-Light.woff2";
import RobotoRegularTtf from "@public/assets/fonts/Roboto-Regular.ttf";
import RobotoRegularWoff from "@public/assets/fonts/Roboto-Regular.woff";
import RobotoRegularWoff2 from "@public/assets/fonts/Roboto-Regular.woff2";
import TwayairTtf from "@public/assets/fonts/tway_air.ttf";
import TwayairWoff from "@public/assets/fonts/tway_air.woff";
import TwayairWoff2 from "@public/assets/fonts/tway_air.woff2";

import { Theme } from "@src/@types/shared";

const GlobalStyle = createGlobalStyle<{ theme: Theme }>`
  @font-face {
    font-family: 'Roboto';
    font-weight: 300;
    font-display: swap;
    src: url(${RobotoLightWoff2}) format('woff2'), url(${RobotoLightWoff}) format('woff'), url(${RobotoLightTtf}) format('truetype');
  }

  @font-face {
    font-family: 'Roboto';
    font-weight: 400;
    font-display: swap;
    src: url(${RobotoRegularWoff2}) format('woff2'), url(${RobotoRegularWoff}) format('woff'), url(${RobotoRegularTtf}) format('truetype');
  }
  
  @font-face {
    font-family: 'Roboto';
    font-weight: 600;
    font-display: swap;
    src: url(${RobotoBoldWoff2}) format('woff2'), url(${RobotoBoldWoff}) format('woff'), url(${RobotoBoldTtf}) format('truetype');
  }

  @font-face {
    font-family: 'Twayair';
    font-display: swap;
    src: url(${TwayairWoff2}) format('woff2'), url(${TwayairWoff}) format('woff'), url(${TwayairTtf}) format('truetype');
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
