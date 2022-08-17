import { QUERY_KEY } from "@src/@constants";
import { CustomError, ResponseSubscribedChannels } from "@src/@types/shared";
import { getSubscribedChannels } from "@src/api/channels";
import { useQuery } from "react-query";

function useGetSubscribedChannels() {
  return useQuery<ResponseSubscribedChannels, CustomError>(
    QUERY_KEY.SUBSCRIBED_CHANNELS,
    getSubscribedChannels
  );
}

export default useGetSubscribedChannels;
