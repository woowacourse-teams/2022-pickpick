import { useQueryClient } from "react-query";
import { useNavigate } from "react-router-dom";

import useRecentFeedPath from "@src/hooks/useRecentFeedPath";
import useSnackbar from "@src/hooks/useSnackbar";

import { ACCESS_TOKEN_KEY, QUERY_KEY } from "@src/@constants/api";
import { MESSAGE } from "@src/@constants/message";
import { PATH_NAME } from "@src/@constants/path";
import { deleteCookie, setCookie } from "@src/@utils";

interface UseAuthenticationResult {
  login: (token: string, isFirstLogin: boolean) => void;
  logout: VoidFunction;
}

function useAuthentication(): UseAuthenticationResult {
  const queryClient = useQueryClient();
  const navigate = useNavigate();
  const { openSuccessSnackbar } = useSnackbar();
  const { setRecentFeedPath } = useRecentFeedPath();

  const login = (token: string, isFirstLogin: boolean) => {
    setCookie(ACCESS_TOKEN_KEY, token);
    queryClient.invalidateQueries(QUERY_KEY.AUTHENTICATION);
    openSuccessSnackbar(MESSAGE.LOGIN_SUCCESS);

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
