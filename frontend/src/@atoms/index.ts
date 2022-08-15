import { SNACKBAR_STATUS, THEME_KIND } from "@src/@constants";
import { SnackbarStatus, ThemeKind } from "@src/@types/shared";
import { atom } from "recoil";

interface SnackbarState {
  isOpened: boolean;
  message: string;
  status: SnackbarStatus;
}

export const snackbarState = atom<SnackbarState>({
  key: "snackbarState",
  default: {
    isOpened: false,
    message: "",
    status: SNACKBAR_STATUS.SUCCESS,
  },
});

export const themeState = atom<ThemeKind>({
  key: "themeState",
  default: THEME_KIND.LIGHT,
});
