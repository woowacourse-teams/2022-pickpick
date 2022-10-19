import { useSetRecoilState } from "recoil";

import { snackbarState } from "@src/@atoms";

type openHandler = (message: string) => void;

interface UseSnackbarResult {
  openSuccessSnackbar: openHandler;
  openFailureSnackbar: openHandler;
}

function useSnackbar(): UseSnackbarResult {
  const setState = useSetRecoilState(snackbarState);

  const openSuccessSnackbar = (message: string) => {
    setState({
      isOpened: true,
      message,
      status: "SUCCESS",
    });
  };

  const openFailureSnackbar = (message: string) => {
    setState({
      isOpened: true,
      message,
      status: "FAIL",
    });
  };

  return { openSuccessSnackbar, openFailureSnackbar };
}

export default useSnackbar;
