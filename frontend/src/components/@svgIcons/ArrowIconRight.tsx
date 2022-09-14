import { SVGProps } from "react";

function ArrowIconRight({ width, height, fill }: SVGProps<SVGAElement>) {
  return (
    <svg
      width={width}
      height={height}
      viewBox="0 0 24 24"
      fill="none"
      xmlns="http://www.w3.org/2000/svg"
    >
      <path
        d="M8.59 16.58L13.17 12L8.59 7.41L10 6L16 12L10 18L8.59 16.58Z"
        fill={fill}
      />
    </svg>
  );
}

export default ArrowIconRight;
