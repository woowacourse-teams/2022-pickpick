import { snackbarState } from "@src/@atoms";
import { useRecoilState } from "recoil";

interface ReturnType {
  openSnackbar: (message: string) => void;
}

function useSnackbar(): ReturnType {
  const [_, setState] = useRecoilState(snackbarState);

  const openSnackbar = (message: string) => {
    setState({
      isOpened: true,
      message,
    });
  };
  return { openSnackbar };
}

export default useSnackbar;
