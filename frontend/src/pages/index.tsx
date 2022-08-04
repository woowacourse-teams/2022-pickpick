import Loader from "@src/components/Loader";
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
        <Component {...props} />
      </Suspense>
    );

export const AddChannel = Loadable({
  Component: lazy(() => import("./AddChannel")),
  LoadingStatus: () => <Loader />,
});

export const Alarm = Loadable({
  Component: lazy(() => import("./Alarm")),
  LoadingStatus: () => <Loader />,
});

export const Bookmark = Loadable({
  Component: lazy(() => import("./Bookmark")),
  LoadingStatus: () => <Loader />,
});

export const Home = Loadable({
  Component: lazy(() => import("./Home")),
  LoadingStatus: () => <Loader />,
});

export const Feed = Loadable({
  Component: lazy(() => import("./Feed")),
  LoadingStatus: () => <Loader />,
});

export const SpecificDateFeed = Loadable({
  Component: lazy(() => import("./SpecificDateFeed")),
  LoadingStatus: () => <Loader />,
});

export const Certification = Loadable({
  Component: lazy(() => import("./Certification")),
  LoadingStatus: () => <Loader />,
});
