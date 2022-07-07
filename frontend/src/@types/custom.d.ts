declare module "*.svg" {
  const content: ({ fill, width, height }: SVGProps) => JSX.Element;
  export default content;
}

declare module "*.woff";
declare module "*.png";
