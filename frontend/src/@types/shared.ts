import { SNACKBAR_STATUS, THEME_KIND } from "@src/@constants";
import { API_ERROR_MESSAGE } from "@src/@constants/message";
import { LIGHT_MODE_THEME } from "@src/@styles/theme";

export type Theme = typeof LIGHT_MODE_THEME;

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
