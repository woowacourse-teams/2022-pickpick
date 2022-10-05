import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

import Loader from "@src/components/Loader";

import useGetCertification from "@src/hooks/@query/useGetCertification";
import useGetSearchParam from "@src/hooks/@shared/useGetSearchParam";
import useAuthentication from "@src/hooks/useAuthentication";

import { PATH_NAME } from "@src/@constants";

function Certification() {
  const navigate = useNavigate();
  const slackCode = useGetSearchParam({ key: "code" });
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
