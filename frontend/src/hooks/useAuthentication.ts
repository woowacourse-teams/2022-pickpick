import {
  ACCESS_TOKEN_KEY,
  QUERY_KEY,
  MESSAGES,
  PATH_NAME,
} from "@src/@constants";
import { deleteCookie, setCookie } from "@src/@utils";
import { useQueryClient } from "react-query";
import { useNavigate } from "react-router-dom";
import useSnackbar from "./useSnackbar";

function useAuthentication() {
  const queryClient = useQueryClient();
  const navigate = useNavigate();
  const { openSuccessSnackbar } = useSnackbar();

  const login = (token: string, isFirstLogin: boolean) => {
    setCookie(ACCESS_TOKEN_KEY, token);
    queryClient.invalidateQueries(QUERY_KEY.AUTHENTICATION);
    openSuccessSnackbar(MESSAGES.LOGIN_SUCCESS);
    if (isFirstLogin) {
      navigate(PATH_NAME.ADD_CHANNEL);
      return;
    }
    navigate(PATH_NAME.FEED);
  };

  const logout = () => {
    deleteCookie(ACCESS_TOKEN_KEY);
    queryClient.invalidateQueries(QUERY_KEY.AUTHENTICATION);
    navigate(PATH_NAME.HOME);
  };

  return { login, logout };
}

export default useAuthentication;
