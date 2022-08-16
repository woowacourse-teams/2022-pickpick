import Loader from "@src/components/Loader";
import { PATH_NAME, QUERY_KEY } from "@src/@constants";
import { useNavigate } from "react-router-dom";
import { useQuery } from "react-query";
import { slackLogin } from "@src/api/auth";
import { ResponseToken, CustomError } from "@src/@types/shared";
import { useEffect } from "react";
import useGetSearchParam from "@src/hooks/useGetSearchParam";
import useAuthentication from "@src/hooks/useAuthentication";

function Certification() {
  const slackCode = useGetSearchParam("code");
  const navigate = useNavigate();
  const { login } = useAuthentication();

  const { isSuccess, isError, data } = useQuery<ResponseToken, CustomError>(
    QUERY_KEY.SLACK_LOGIN,
    () => slackLogin(slackCode),
    {
      retry: false,
    }
  );

  useEffect(() => {
    if (isSuccess && data) {
      login(data.token, data.isFirstLogin);
    }
  }, [isSuccess, data]);

  useEffect(() => {
    if (!isError) return;

    navigate(PATH_NAME.HOME);
  }, [isError]);

  return <Loader />;
}

export default Certification;
