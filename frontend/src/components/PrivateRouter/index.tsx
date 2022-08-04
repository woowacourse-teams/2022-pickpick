import { PATH_NAME, QUERY_KEY, ERROR_MESSAGES } from "@src/@constants";
import { isCertificated } from "@src/api/auth";
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
  const { openFailureSnackbar } = useSnackbar();

  const { isLoading, isError } = useQuery(
    QUERY_KEY.AUTHENTICATION,
    isCertificated,
    {
      useErrorBoundary: false,
      retry: false,
    }
  );

  useEffect(() => {
    if (!isError) return;
    openFailureSnackbar(ERROR_MESSAGES.UNAUTHORIZED);
    navigate(PATH_NAME.HOME);
  }, [isError]);

  if (isLoading || isError) return <Loader />;
  return <>{children}</>;
}

export default PrivateRouter;
