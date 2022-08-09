import { useNavigate } from "react-router-dom";

type ReturnType = () => void;

function usePushPreviousPage(): ReturnType {
  const navigate = useNavigate();

  const pushPreviousPage = () => {
    navigate(-1);
  };

  return pushPreviousPage;
}

export default usePushPreviousPage;
