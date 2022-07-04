import { createGlobalStyle } from "styled-components";

import RobotoBold from "../public/assets/fonts/Roboto-Bold.woff";
import RobotoBoldItalic from "../public/assets/fonts/Roboto-BoldItalic.woff";
import RobotoRegular from "../public/assets/fonts/Roboto-Regular.woff";
import RobotoItalic from "../public/assets/fonts/Roboto-Italic.woff";
import RobotoLight from "../public/assets/fonts/Roboto-Light.woff";
import RobotoLightItalic from "../public/assets/fonts/Roboto-LightItalic.woff";

import Twayair from "../public/assets/fonts/Twayair.woff";

const GlobalStyle = createGlobalStyle`
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
`;

export default GlobalStyle;
