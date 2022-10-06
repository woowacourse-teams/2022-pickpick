import { useNavigate } from "react-router-dom";

import useSnackbar from "@src/hooks/useSnackbar";

import {
  ERROR_CODE,
  ERROR_MESSAGE_BY_CODE,
  MESSAGE,
  PATH_NAME,
} from "@src/@constants";
import { CustomError } from "@src/@types/shared";

interface UseApiErrorResult {
  handleError: (error: any) => void;
}

function useApiError(): UseApiErrorResult {
  const navigate = useNavigate();
  const { openFailureSnackbar } = useSnackbar();

  const handleError = (error: CustomError) => {
    const errorCode = error.response?.data?.code;

    if (errorCode === ERROR_CODE.INVALID_TOKEN) return;

    const errorMessage =
      ERROR_MESSAGE_BY_CODE[errorCode] ?? MESSAGE.DEFAULT_SERVER_ERROR;

    openFailureSnackbar(errorMessage);

    if (errorCode === ERROR_CODE.SUBSCRIPTION_NOT_FOUND) {
      navigate(PATH_NAME.ADD_CHANNEL);
    }
  };

  return { handleError };
}

export default useApiError;
