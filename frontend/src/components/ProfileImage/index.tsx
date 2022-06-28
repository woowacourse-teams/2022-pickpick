import React from "react";
import * as Styled from "./style";

function ProfileImage(props: React.ImgHTMLAttributes<HTMLImageElement>) {
  return <Styled.Container {...props} />;
}

export default ProfileImage;
