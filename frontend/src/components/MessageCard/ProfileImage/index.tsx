import React from "react";
import DefaultProfileImage from "@public/assets/images/DefaultProfileImage.png";
import * as Styled from "./style";

interface Props extends React.ImgHTMLAttributes<HTMLImageElement> {
  src?: string;
}

function ProfileImage({ src, ...props }: Props) {
  return (
    <Styled.Container
      {...props}
      src={src || DefaultProfileImage}
      loading="lazy"
    />
  );
}

export default ProfileImage;
