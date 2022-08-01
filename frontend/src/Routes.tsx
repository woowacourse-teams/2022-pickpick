import NotFound from "@src/pages/NotFound";
import { PATH_NAME } from "@src/@constants";
import LayoutContainer from "@src/components/@layouts/LayoutContainer";
import {
  AddChannel,
  Alarm,
  Bookmark,
  Feed,
  SpecificDateFeed,
  Home,
} from "./pages";
import PrivateRouter from "@src/components/PrivateRouter";

const routes = [
  {
    path: PATH_NAME.HOME,
    element: <LayoutContainer />,
    children: [
      {
        path: "",
        element: <Home />,
      },
      {
        path: PATH_NAME.ADD_CHANNEL,
        element: (
          <PrivateRouter>
            <AddChannel />
          </PrivateRouter>
        ),
      },
      {
        path: PATH_NAME.ALARM,
        element: (
          <PrivateRouter>
            <Alarm />
          </PrivateRouter>
        ),
      },
      {
        path: PATH_NAME.BOOKMARK,
        element: (
          <PrivateRouter>
            <Bookmark />
          </PrivateRouter>
        ),
      },
      {
        path: `${PATH_NAME.FEED}/:date`,
        element: (
          <PrivateRouter>
            <SpecificDateFeed />
          </PrivateRouter>
        ),
      },
      {
        path: PATH_NAME.FEED,
        element: (
          <PrivateRouter>
            <Feed />
          </PrivateRouter>
        ),
      },
      {
        path: "*",
        element: <NotFound />,
      },
    ],
  },
];

export default routes;
