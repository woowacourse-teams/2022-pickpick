import {
  ACCESS_TOKEN_KEY,
  QUERY_KEY,
  MESSAGES,
  PATH_NAME,
} from "@src/@constants";
import { deleteCookie, setCookie } from "@src/@utils";
import { useQueryClient } from "react-query";
import { useNavigate } from "react-router-dom";
import useSnackbar from "@src/hooks/useSnackbar";
import useRecentFeedPath from "@src/hooks/useRecentFeedPath";

interface ReturnType {
  login: (token: string, isFirstLogin: boolean) => void;
  logout: () => void;
}

function useAuthentication(): ReturnType {
  const queryClient = useQueryClient();
  const navigate = useNavigate();
  const { openSuccessSnackbar } = useSnackbar();
  const { setRecentFeedPath } = useRecentFeedPath();

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
    setRecentFeedPath("");
    deleteCookie(ACCESS_TOKEN_KEY);
    queryClient.invalidateQueries(QUERY_KEY.AUTHENTICATION);
    navigate(PATH_NAME.HOME);
  };

  return { login, logout };
}

export default useAuthentication;
