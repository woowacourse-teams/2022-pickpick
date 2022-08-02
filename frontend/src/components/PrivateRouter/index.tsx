import { PATH_NAME, QUERY_KEY } from "@src/@constants";
import { isAuthenticated } from "@src/api/auth";
import useSnackbar from "@src/hooks/useSnackbar";
import { useEffect } from "react";
import { useQuery } from "react-query";
import { useNavigate } from "react-router-dom";
import Loader from "@src/components/Loader";

interface Props {
  children: JSX.Element;
}

function PrivateRouter({ children }: Props) {
  const navigate = useNavigate();
  const openSnackbar = useSnackbar();

  const { isLoading, isError } = useQuery(
    QUERY_KEY.AUTHENTICATION,
    isAuthenticated,
    {
      useErrorBoundary: false,
      retry: false,
    }
  );

  useEffect(() => {
    if (!isError) return;
    openSnackbar("로그인이 필요한 서비스 입니다.");
    navigate(PATH_NAME.HOME);
  }, [isError]);

  if (isLoading || isError) return <Loader />;
  return <>{children}</>;
}

export default PrivateRouter;
