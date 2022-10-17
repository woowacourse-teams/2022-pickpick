import { UseQueryResult, useQuery } from "react-query";

import { QUERY_KEY } from "@src/@constants/api";
import { ResponseToken } from "@src/@types/api";
import { CustomError } from "@src/@types/shared";

import { registerSlackWorkspace } from "@src/api/auth";

interface Props {
  slackCode: string;
}

function useGetRegisterSlackWorkspace({
  slackCode,
}: Props): UseQueryResult<ResponseToken, CustomError> {
  return useQuery<ResponseToken, CustomError>(QUERY_KEY.SLACK_LOGIN, () =>
    registerSlackWorkspace(slackCode)
  );
}

export default useGetRegisterSlackWorkspace;
