import { atom } from "recoil";

interface SnackbarState {
  isOpened: boolean;
  message: string;
  status: "SUCCESS" | "FAIL";
}

export const snackbarState = atom<SnackbarState>({
  key: "snackbarState",
  default: {
    isOpened: false,
    message: "",
    status: "SUCCESS",
  },
});
