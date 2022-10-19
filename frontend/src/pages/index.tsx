import { LazyExoticComponent, Suspense, lazy } from "react";

import Loader from "@src/components/Loader";

interface LoadableType {
  Component: LazyExoticComponent<() => JSX.Element>;
}

const Loadable =
  ({ Component }: LoadableType) =>
  // eslint-disable-next-line react/display-name
  (props: any) =>
    (
      <Suspense fallback={<Loader />}>
        <Component {...props} />
      </Suspense>
    );

export const AddChannel = Loadable({
  Component: lazy(() => import("./AddChannel")),
});

export const Bookmark = Loadable({
  Component: lazy(() => import("./Bookmark")),
});

export const Reminder = Loadable({
  Component: lazy(() => import("./Reminder")),
});

export const Home = Loadable({
  Component: lazy(() => import("./Home")),
});

export const Feed = Loadable({
  Component: lazy(() => import("./Feed")),
});

export const SpecificDateFeed = Loadable({
  Component: lazy(() => import("./SpecificDateFeed")),
});

export const Certification = Loadable({
  Component: lazy(() => import("./Certification")),
});

export const SearchResult = Loadable({
  Component: lazy(() => import("./SearchResult")),
});

export const RegisterSlackWorkspace = Loadable({
  Component: lazy(() => import("./RegisterSlackWorkspace")),
});
