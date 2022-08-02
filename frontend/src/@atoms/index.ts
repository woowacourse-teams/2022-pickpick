import { SNACKBAR_STATUS } from "@src/@constants";
import { SnackbarStatus } from "@src/@types/shared";
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
