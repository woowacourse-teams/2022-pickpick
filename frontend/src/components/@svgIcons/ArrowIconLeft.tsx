import { SVGProps } from "react";

function ArrowIconLeft({ width, height, fill }: SVGProps<SVGAElement>) {
  return (
    <svg
      width={width}
      height={height}
      viewBox="0 0 24 24"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        d="M15.41 16.58L10.83 12L15.41 7.41L14 6L8 12L14 18L15.41 16.58Z"
        fill={fill}
      />
    </svg>
  );
}

export default ArrowIconLeft;
