import { snackbarState } from "@src/@atoms";
import { useRecoilState } from "recoil";

function useSnackbar() {
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
