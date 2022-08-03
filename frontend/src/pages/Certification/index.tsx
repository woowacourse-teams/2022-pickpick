import Loader from "@src/components/Loader";
import { PATH_NAME } from "@src/@constants";
import { useLocation, useSearchParams } from "react-router-dom";
import { useQuery } from "react-query";
import useSnackbar from "@src/hooks/useSnackbar";

// 여기서 바로 요청을 get 요청으로 Token 값을 가져와서 쏜다.
// 만약, 요청 성공이면, FEED 로 이동
// 요청 실패면, 실패했다고 스낵바로 알려주고, 메인페이지로 다시 리다이렉트

function Certification() {
  const location = useLocation();
  const [searchParams] = useSearchParams();
  // code = searchParams.get('code');
  return <Loader />;
}

export default Certification;
