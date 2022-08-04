import Loader from "@src/components/Loader";
import { PATH_NAME, QUERY_KEY } from "@src/@constants";
import { useNavigate } from "react-router-dom";
import { useQuery } from "react-query";
import useSnackbar from "@src/hooks/useSnackbar";
import { slackLogin } from "@src/api/auth";
import useGetSearchParam from "@src/hooks/useGetSearchParam";

function Certification() {
  const slackCode = useGetSearchParam("code");
  const navigate = useNavigate();
  const { openSuccessSnackbar, openFailureSnackbar } = useSnackbar();

  const data = useQuery(QUERY_KEY.SLACK_LOGIN, () => slackLogin(slackCode), {
    retry: false,
    onSuccess: () => {
      openSuccessSnackbar("로그인에 성공했습니다.");
      navigate(PATH_NAME.FEED);
    },
    onError: () => {
      openFailureSnackbar("문제가 발생했습니다.");
      navigate(PATH_NAME.HOME);
    },
  });
  return <Loader />;
}

export default Certification;
