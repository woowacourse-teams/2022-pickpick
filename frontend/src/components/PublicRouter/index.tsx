import { PropsWithChildren } from "react";
import { PATH_NAME, QUERY_KEY } from "@src/@constants";
import { isCertificated } from "@src/api/auth";
import { useEffect } from "react";
import { useQuery } from "react-query";
import { useNavigate } from "react-router-dom";
import Loader from "@src/components/Loader";

function PublicRouter({ children }: PropsWithChildren) {
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
