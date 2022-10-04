import { UseQueryResult, useQuery } from "react-query";

import { QUERY_KEY } from "@src/@constants";
import { CustomError, ResponseSubscribedChannels } from "@src/@types/shared";

import { getSubscribedChannels } from "@src/api/channels";

function useGetSubscribedChannels(): UseQueryResult<
  ResponseSubscribedChannels,
  CustomError
> {
  return useQuery<ResponseSubscribedChannels, CustomError>(
    QUERY_KEY.SUBSCRIBED_CHANNELS,
    getSubscribedChannels
  );
}

export default useGetSubscribedChannels;
