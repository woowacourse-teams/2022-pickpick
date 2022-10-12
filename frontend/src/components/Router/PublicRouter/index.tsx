import { useEffect } from "react";
import { useQuery } from "react-query";
import { useNavigate } from "react-router-dom";

import Loader from "@src/components/Loader";

import { QUERY_KEY } from "@src/@constants/api";
import { PATH_NAME } from "@src/@constants/path";
import { StrictPropsWithChildren } from "@src/@types/utils";

import { isCertificated } from "@src/api/auth";

function PublicRouter({ children }: StrictPropsWithChildren) {
  const navigate = useNavigate();
  const { isLoading, isSuccess } = useQuery(
    QUERY_KEY.AUTHENTICATION,
    isCertificated,
    {
      retry: false,
      onError: () => null,
    }
  );

  useEffect(() => {
    if (isSuccess) navigate(PATH_NAME.FEED);
  }, [isSuccess]);

  if (isLoading || isSuccess) return <Loader />;

  return <> {children}</>;
}

export default PublicRouter;
