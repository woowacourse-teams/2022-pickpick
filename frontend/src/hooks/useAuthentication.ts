import { ACCESS_TOKEN_KEY, QUERY_KEY, MESSAGES } from "@src/@constants";
import { deleteCookie, setCookie } from "@src/@utils";
import { useQueryClient } from "react-query";
import useSnackbar from "./useSnackbar";

function useAuthentication() {
  const queryClient = useQueryClient();
  const { openSuccessSnackbar } = useSnackbar();

  const login = () => {
    setCookie(ACCESS_TOKEN_KEY, "test");
    queryClient.invalidateQueries(QUERY_KEY.AUTHENTICATION);
    openSuccessSnackbar(MESSAGES.LOGIN_SUCCESS);
  };

  const logout = () => {
    deleteCookie(ACCESS_TOKEN_KEY);
    queryClient.invalidateQueries(QUERY_KEY.AUTHENTICATION);
  };

  return { login, logout };
}

export default useAuthentication;
