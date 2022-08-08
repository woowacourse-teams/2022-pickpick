import React from "react";
import DefaultProfileImage from "@public/assets/images/DefaultProfileImage.png";
import * as Styled from "./style";

function ProfileImage({
  // eslint-disable-next-line react/prop-types
  src,
  ...props
}: React.ImgHTMLAttributes<HTMLImageElement>) {
  return (
    <Styled.Container
      src={src || DefaultProfileImage}
      {...props}
      loading="lazy"
    />
  );
}

export default ProfileImage;
