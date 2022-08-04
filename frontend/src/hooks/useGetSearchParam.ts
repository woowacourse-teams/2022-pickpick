import { useSearchParams } from "react-router-dom";

function useGetSearchParam(key: string) {
  const [searchParams] = useSearchParams();
  return searchParams.get(key) ?? "";
}

export default useGetSearchParam;
