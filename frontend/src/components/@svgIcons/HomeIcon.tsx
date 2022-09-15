import { SVGProps } from "react";

function HomeIcon({ width, height, fill }: SVGProps<SVGAElement>) {
  return (
    <svg
      width={width}
      height={height}
      viewBox="0 0 24 24"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path d="M12 3L20 9V21H15V14H9V21H4V9L12 3Z" fill={fill} />
    </svg>
  );
}

export default HomeIcon;
