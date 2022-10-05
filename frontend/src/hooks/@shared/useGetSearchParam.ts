import { useSearchParams } from "react-router-dom";

interface Props {
  key: string;
}
type UseGetSearchParamResult = string;

function useGetSearchParam({ key }: Props): UseGetSearchParamResult {
  const [searchParams] = useSearchParams();

  return searchParams.get(key) ?? "";
}

export default useGetSearchParam;
