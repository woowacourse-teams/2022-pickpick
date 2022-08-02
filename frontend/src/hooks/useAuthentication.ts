import { ACCESS_TOKEN_KEY, QUERY_KEY } from "@src/@constants";
import { deleteCookie, setCookie } from "@src/@utils";
import { useQueryClient } from "react-query";
import useSnackbar from "./useSnackbar";

function useAuthentication() {
  const queryClient = useQueryClient();
  const openSnackbar = useSnackbar();

  const login = () => {
    setCookie(ACCESS_TOKEN_KEY, "test");
    queryClient.invalidateQueries(QUERY_KEY.AUTHENTICATION);
    openSnackbar("로그인에 성공했습니다!");
  };

  const logout = () => {
    deleteCookie(ACCESS_TOKEN_KEY);
    queryClient.invalidateQueries(QUERY_KEY.AUTHENTICATION);
  };

  return { login, logout };
}

export default useAuthentication;
