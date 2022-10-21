import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

import Loader from "@src/components/Loader";

import useGetRegisterSlackWorkspace from "@src/hooks/@query/useGetRegisterSlackWorkspace";
import useGetSearchParam from "@src/hooks/@shared/useGetSearchParam";

import { PATH_NAME } from "@src/@constants/path";

function RegisterSlackWorkspace() {
  const navigate = useNavigate();
  const slackCode = useGetSearchParam({ key: "code" });
  const { isError } = useGetRegisterSlackWorkspace({
    slackCode,
  });

  useEffect(() => {
    if (!isError) return;

    navigate(PATH_NAME.HOME);
  }, [isError]);

  return <Loader />;
}

export default RegisterSlackWorkspace;
