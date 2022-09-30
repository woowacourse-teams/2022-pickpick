import { Link } from "react-router-dom";

import Button from "@src/components/@shared/Button";

import { PATH_NAME } from "@src/@constants";
import { FlexColumn } from "@src/@styles/shared";

import * as Styled from "./style";

function NotFound() {
  return (
    <FlexColumn gap="30px" margin="30vh 0" alignItems="center">
      <Styled.Title>잘못된 경로 입니다.</Styled.Title>

      <Styled.Description>
        피드로 돌아가시려면 아래 버튼을 클릭해주세요.
      </Styled.Description>

      <Link to={PATH_NAME.FEED}>
        <Button>피드로 가기</Button>
      </Link>
    </FlexColumn>
  );
}

export default NotFound;
