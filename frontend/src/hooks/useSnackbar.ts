import { snackbarState } from "@src/@atoms";
import { useRecoilState } from "recoil";

type openHandler = (message: string) => void;

interface ReturnType {
  openSuccessSnackbar: openHandler;
  openFailureSnackbar: openHandler;
}

function useSnackbar(): ReturnType {
  const [_, setState] = useRecoilState(snackbarState);

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
