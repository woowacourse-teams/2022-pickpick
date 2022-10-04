import { useNavigate } from "react-router-dom";

type UsePushPreviousPageResult = () => void;

function usePushPreviousPage(): UsePushPreviousPageResult {
  const navigate = useNavigate();

  const pushPreviousPage = () => {
    navigate(-1);
  };

  return pushPreviousPage;
}

export default usePushPreviousPage;
