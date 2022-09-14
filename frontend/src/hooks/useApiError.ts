import { ERROR_MESSAGE_BY_CODE, PATH_NAME } from "@src/@constants";
import { CustomError } from "@src/@types/shared";
import { useNavigate } from "react-router-dom";
import useSnackbar from "@src/hooks/useSnackbar";

interface ReturnType {
  handleError: (error: any) => void;
}

function useApiError(): ReturnType {
  const navigate = useNavigate();
  const { openFailureSnackbar } = useSnackbar();

  const handleError = (error: CustomError) => {
    const errorCode = error.response?.data?.code;
    if (errorCode === "INVALID_TOKEN") return;
    const errorMessage =
      ERROR_MESSAGE_BY_CODE[errorCode] ?? ERROR_MESSAGE_BY_CODE.DEFAULT_MESSAGE;

    openFailureSnackbar(errorMessage);

    if (errorCode === "SUBSCRIPTION_NOT_FOUND") {
      navigate(PATH_NAME.ADD_CHANNEL);
    }
  };

  return { handleError };
}

export default useApiError;
