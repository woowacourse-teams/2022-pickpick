import Loader from "@src/components/Loader";
import { PATH_NAME } from "@src/@constants";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";
import useGetSearchParam from "@src/hooks/useGetSearchParam";
import useAuthentication from "@src/hooks/useAuthentication";
import useGetCertification from "@src/hooks/query/useGetCertification";

function Certification() {
  const slackCode = useGetSearchParam("code");
  const navigate = useNavigate();
  const { login } = useAuthentication();
  const { isSuccess, isError, data } = useGetCertification({ slackCode });

  useEffect(() => {
    if (isSuccess && data) {
      login(data.token, data.isFirstLogin);
    }
  }, [isSuccess, data]);

  useEffect(() => {
    if (!isError) return;

    navigate(PATH_NAME.HOME);
  }, [isError]);

  return <Loader />;
}

export default Certification;
