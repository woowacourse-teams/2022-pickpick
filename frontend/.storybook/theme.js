import { create } from "@storybook/theming/create";

import logo from "../public/assets/images/pickpick.png";

export default create({
  base: "light",
  colorPrimary: " #FF9900",
  colorSecondary: "#FF9900",

  appBg: "white",
  appContentBg: "white",
  appBorderColor: "grey",
  appBorderRadius: 4,

  textColor: "black",
  textInverseColor: "white",

  barTextColor: "#343434",
  barSelectedColor: "#FF9900",
  barBg: "white",

  inputBg: "white",
  inputBorder: "#FFC56E",
  inputTextColor: "black",
  inputBorderRadius: 4,

  brandTitle: "줍줍 (PickPick)",
  brandUrl: "https://jupjup.site/",
  brandImage: logo,
});
