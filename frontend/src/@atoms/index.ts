import { atom } from "recoil";

interface SnackbarState {
  isOpened: boolean;
  message: string;
}

export const snackbarState = atom<SnackbarState>({
  key: "snackbarState",
  default: {
    isOpened: false,
    message: "",
  },
});
