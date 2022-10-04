import { ReactNode } from "react";

export type FunctionChildren = (...args: any) => ReactNode;

export type StrictPropsWithChildren<P = unknown> = P & {
  children: ReactNode;
};

export type PropsWithFunctionChildren<
  P = unknown,
  F extends FunctionChildren = (...args: any) => ReactNode
> = P & {
  children: F;
};
