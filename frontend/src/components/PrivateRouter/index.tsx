import { PATH_NAME, QUERY_KEY } from "@src/@constants";
import { isAuthenticated } from "@src/api/auth";
import { useEffect } from "react";
import { useQuery } from "react-query";
import { useNavigate } from "react-router-dom";

interface Props {
  children: JSX.Element;
}

function PrivateRouter({ children }: Props) {
  const navigate = useNavigate();

  const { isError } = useQuery(QUERY_KEY.AUTHENTICATION, isAuthenticated, {
    suspense: true,
    useErrorBoundary: false,
  });

  useEffect(() => {
    if (!isError) return;
    navigate(PATH_NAME.HOME);
  }, [isError]);

  return <>{children}</>;
}

export default PrivateRouter;
