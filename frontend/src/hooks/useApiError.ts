import { ERROR_MESSAGE_BY_CODE } from "@src/@constants";
import { CustomError } from "@src/@types/shared";
import useSnackbar from "./useSnackbar";

function useApiError() {
  const { openFailureSnackbar } = useSnackbar();

  const handleError = (error: CustomError) => {
    const errorMessage =
      ERROR_MESSAGE_BY_CODE[error.code] ??
      ERROR_MESSAGE_BY_CODE.DEFAULT_MESSAGE;

    openFailureSnackbar(errorMessage);
  };

  return { handleError };
}

export default useApiError;
