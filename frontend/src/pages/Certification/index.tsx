import Loader from "@src/components/Loader";
import { ACCESS_TOKEN_KEY, PATH_NAME, QUERY_KEY } from "@src/@constants";
import { useNavigate } from "react-router-dom";
import { useQuery } from "react-query";
import useSnackbar from "@src/hooks/useSnackbar";
import { slackLogin } from "@src/api/auth";
import useGetSearchParam from "@src/hooks/useGetSearchParam";
import { setCookie } from "@src/@utils";
import { ResponseToken } from "@src/@types/shared";
import { useEffect } from "react";

function Certification() {
  const slackCode = useGetSearchParam("code");
  const navigate = useNavigate();
  const { openSuccessSnackbar, openFailureSnackbar } = useSnackbar();

  const { data, isSuccess } = useQuery<ResponseToken>(
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
      openSuccessSnackbar("로그인에 성공했습니다.");
      setCookie(ACCESS_TOKEN_KEY, data?.token ?? "");
      navigate(PATH_NAME.FEED);
    }
  }, [isSuccess, data]);
  return <Loader />;
}

export default Certification;
