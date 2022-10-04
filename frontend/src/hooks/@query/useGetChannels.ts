import { UseQueryResult, useQuery } from "react-query";

import { QUERY_KEY } from "@src/@constants";
import { CustomError, ResponseChannels } from "@src/@types/shared";

import { getChannels } from "@src/api/channels";

function useGetChannels(): UseQueryResult<ResponseChannels, CustomError> {
  return useQuery<ResponseChannels, CustomError>(
    QUERY_KEY.ALL_CHANNELS,
    getChannels
  );
}

export default useGetChannels;
