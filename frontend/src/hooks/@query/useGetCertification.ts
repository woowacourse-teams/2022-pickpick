import { UseQueryResult, useQuery } from "react-query";

import { QUERY_KEY } from "@src/@constants";
import { ResponseToken } from "@src/@types/api";
import { CustomError } from "@src/@types/shared";

import { slackLogin } from "@src/api/auth";

interface Props {
  slackCode: string;
}

function useGetCertification({
  slackCode,
}: Props): UseQueryResult<ResponseToken, CustomError> {
  return useQuery<ResponseToken, CustomError>(QUERY_KEY.SLACK_LOGIN, () =>
    slackLogin(slackCode)
  );
}

export default useGetCertification;
