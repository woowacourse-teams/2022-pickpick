import { SVGProps } from "react";

function MenuIcon({ width, height, fill }: SVGProps<SVGAElement>) {
  return (
    <svg
      width={width}
      height={height}
      viewBox="0 0 24 24"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path d="M3 6H21V8H3V6ZM3 11H21V13H3V11ZM3 16H21V18H3V16Z" fill={fill} />
    </svg>
  );
}

export default MenuIcon;
