import Loader from "@src/components/Loader";
import { PATH_NAME, QUERY_KEY } from "@src/@constants";
import { useNavigate } from "react-router-dom";
import { useQuery } from "react-query";
import useSnackbar from "@src/hooks/useSnackbar";
import { slackLogin } from "@src/api/auth";
import useGetSearchParam from "@src/hooks/useGetSearchParam";
import { ResponseToken } from "@src/@types/shared";
import useAuthentication from "@src/hooks/useAuthentication";
import { useEffect } from "react";

function Certification() {
  const slackCode = useGetSearchParam("code");
  const { login } = useAuthentication();
  const navigate = useNavigate();
  const { openFailureSnackbar } = useSnackbar();

  const { isSuccess, data } = useQuery<ResponseToken>(
    QUERY_KEY.SLACK_LOGIN,
    () => slackLogin(slackCode),
    {
      retry: false,
      onError: () => {
        openFailureSnackbar("문제가 발생했습니다.");
        navigate(PATH_NAME.HOME);
      },
    }
  );

  useEffect(() => {
    if (isSuccess && data) {
      login(data.token, data.isFirstLogin);
    }
  }, [isSuccess, data]);
  return <Loader />;
}

export default Certification;
