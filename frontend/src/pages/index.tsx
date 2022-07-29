import LazyLoading from "@src/components/LazyLoading";
import { lazy, LazyExoticComponent, Suspense } from "react";

interface LoadableType {
  Component: LazyExoticComponent<() => JSX.Element>;
  LoadingStatus?: () => JSX.Element | null;
}

const Loadable =
  ({ Component, LoadingStatus = () => null }: LoadableType) =>
  // eslint-disable-next-line react/display-name
  (props: any) =>
    (
      <Suspense fallback={<LoadingStatus />}>
        <Component {...props} />;
      </Suspense>
    );

export const AddChannel = Loadable({
  Component: lazy(() => import("./AddChannel")),
  LoadingStatus: () => <LazyLoading />,
});

export const Alarm = Loadable({
  Component: lazy(() => import("./Alarm")),
  LoadingStatus: () => <LazyLoading />,
});

export const Bookmark = Loadable({
  Component: lazy(() => import("./Bookmark")),
  LoadingStatus: () => <LazyLoading />,
});

export const Home = Loadable({
  Component: lazy(() => import("./Home")),
  LoadingStatus: () => <LazyLoading />,
});

export const Feed = Loadable({
  Component: lazy(() => import("./Feed")),
  LoadingStatus: () => <LazyLoading />,
});

export const SpecificDateFeed = Loadable({
  Component: lazy(() => import("./SpecificDateFeed")),
  LoadingStatus: () => <LazyLoading />,
});
