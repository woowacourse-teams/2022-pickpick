import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

import Loader from "@src/components/Loader";

import useGetCertification from "@src/hooks/query/useGetCertification";
import useAuthentication from "@src/hooks/useAuthentication";
import useGetSearchParam from "@src/hooks/useGetSearchParam";

import { PATH_NAME } from "@src/@constants";

function Certification() {
  const navigate = useNavigate();
  const slackCode = useGetSearchParam("code");
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
