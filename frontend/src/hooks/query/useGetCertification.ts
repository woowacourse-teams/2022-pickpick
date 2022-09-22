import { QUERY_KEY } from "@src/@constants";
import { CustomError, ResponseToken } from "@src/@types/shared";
import { slackLogin } from "@src/api/auth";
import { useQuery } from "react-query";

interface Props {
  slackCode: string;
}

function useGetCertification({ slackCode }: Props) {
  return useQuery<ResponseToken, CustomError>(QUERY_KEY.SLACK_LOGIN, () =>
    slackLogin(slackCode)
  );
}

export default useGetCertification;
