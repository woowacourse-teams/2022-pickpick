import { SVGProps } from "react";

function PlusIcon({ width, height, fill }: SVGProps<SVGAElement>) {
  return (
    <svg
      width={width}
      height={height}
      viewBox="0 0 24 24"
      fill={fill}
      xmlns="http://www.w3.org/2000/svg"
    >
      <path d="M19 13H13V19H11V13H5V11H11V5H13V11H19V13Z" fill="current" />
    </svg>
  );
}

export default PlusIcon;
