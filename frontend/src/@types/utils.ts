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

/**
 * @see https://stackoverflow.com/a/70307091
 */
export type Enumerate<
  N extends number,
  Acc extends number[] = []
> = Acc["length"] extends N
  ? Acc[number]
  : Enumerate<N, [...Acc, Acc["length"]]>;

export type Range<F extends number, T extends number> = Exclude<
  Enumerate<T>,
  Enumerate<F>
>;

export type ValueOf<T> = T[keyof T];
