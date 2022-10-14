import { SNACKBAR_STATUS, THEME_KIND } from "@src/@constants";
import { API_ERROR_MESSAGE } from "@src/@constants/message";

export type Theme = {
  COLOR: {
    PRIMARY: { DEFAULT: string };
    SECONDARY: { DEFAULT: string };
    TEXT: {
      DEFAULT: string;
      DISABLED: string;
      PLACEHOLDER: string;
      LIGHT_BLUE: string;
      WHITE: string;
    };
    BACKGROUND: {
      PRIMARY: string;
      SECONDARY: string;
      TERTIARY: string;
    };
    WRAPPER: {
      DEFAULT: string;
      DISABLED: string;
    };
    CONTAINER: {
      DEFAULT: string;
      WHITE: string;
      LIGHT_RED: string;
      LIGHT_BLUE: string;
      LIGHT_ORANGE: string;
      GRADIENT_ORANGE: string;
    };
    BORDER: string;
    DIMMER: string;
    STAR_ICON_FILL: string;
  };
  FONT: {
    PRIMARY: "Roboto";
    SECONDARY: "Twayair";
  };
  FONT_SIZE: {
    TITLE: string;
    SUBTITLE: string;
    X_LARGE_BODY: string;
    LARGE_BODY: string;
    BODY: string;
    PLACEHOLDER: string;
    CAPTION: string;
  };
};

export type ThemeKind = keyof typeof THEME_KIND;

export interface StyledDefaultProps {
  theme: Theme;
}

export type SnackbarStatus = keyof typeof SNACKBAR_STATUS;

export type CustomError = {
  response: {
    data: {
      code: keyof typeof API_ERROR_MESSAGE;
      message: string;
    };
  };
};
