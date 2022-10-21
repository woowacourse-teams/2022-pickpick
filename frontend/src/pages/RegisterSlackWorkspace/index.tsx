import { useEffect } from "react";
import { useNavigate } from "react-router-dom";

import Loader from "@src/components/Loader";

import useGetRegisterSlackWorkspace from "@src/hooks/@query/useGetRegisterSlackWorkspace";
import useGetSearchParam from "@src/hooks/@shared/useGetSearchParam";
import useSnackbar from "@src/hooks/useSnackbar";

import { MESSAGE } from "@src/@constants/message";
import { PATH_NAME } from "@src/@constants/path";

function RegisterSlackWorkspace() {
  const navigate = useNavigate();
  const slackCode = useGetSearchParam({ key: "code" });
  const { openSuccessSnackbar } = useSnackbar();
  const { isSuccess } = useGetRegisterSlackWorkspace({
    slackCode,
  });

  useEffect(() => {
    if (!isSuccess) return;

    openSuccessSnackbar(MESSAGE.WORKSPACE_SUCCESS);
    navigate(PATH_NAME.HOME);
  }, [isSuccess]);

  return <Loader />;
}

export default RegisterSlackWorkspace;
