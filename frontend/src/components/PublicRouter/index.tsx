import { PATH_NAME, QUERY_KEY } from "@src/@constants";
import { isAuthenticated } from "@src/api/auth";
import { useEffect } from "react";
import { useQuery } from "react-query";
import { useNavigate } from "react-router-dom";

interface Props {
  children: JSX.Element;
}

function PublicRouter({ children }: Props) {
  const navigate = useNavigate();

  const { isLoading, isSuccess } = useQuery(
    QUERY_KEY.AUTHENTICATION,
    isAuthenticated,
    {
      useErrorBoundary: false,
      retry: false,
    }
  );

  useEffect(() => {
    if (isSuccess) navigate(PATH_NAME.FEED);
  }, [isSuccess]);

  if (isLoading) return null;
  return <> {children}</>;
}

export default PublicRouter;
