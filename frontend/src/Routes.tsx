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
        element: <AddChannel />,
      },
      {
        path: PATH_NAME.ALARM,
        element: <Alarm />,
      },
      {
        path: PATH_NAME.BOOKMARK,
        element: <Bookmark />,
      },
      {
        path: `${PATH_NAME.FEED}/:date`,
        element: <SpecificDateFeed />,
      },
      {
        path: PATH_NAME.FEED,
        element: <Feed />,
      },
      {
        path: "*",
        element: <NotFound />,
      },
    ],
  },
];

export default routes;
