import {
  ERROR_MESSAGE_BY_CODE,
  SNACKBAR_STATUS,
  THEME_KIND,
} from "@src/@constants";
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
      code: keyof typeof ERROR_MESSAGE_BY_CODE;
      message: string;
    };
  };
};
