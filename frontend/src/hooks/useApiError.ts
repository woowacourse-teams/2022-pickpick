import { useNavigate } from "react-router-dom";

import useSnackbar from "@src/hooks/useSnackbar";

import { ERROR_CODE } from "@src/@constants/api";
import { API_ERROR_MESSAGE, MESSAGE } from "@src/@constants/message";
import { PATH_NAME } from "@src/@constants/path";
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
      API_ERROR_MESSAGE[errorCode] ?? MESSAGE.DEFAULT_SERVER_ERROR;

    openFailureSnackbar(errorMessage);

    if (errorCode === ERROR_CODE.SUBSCRIPTION_NOT_FOUND) {
      navigate(PATH_NAME.ADD_CHANNEL);
    }
  };

  return { handleError };
}

export default useApiError;
