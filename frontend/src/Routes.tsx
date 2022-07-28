import AddChannel from "@src/pages/AddChannel";
import Alarm from "@src/pages/Alarm";
import Bookmark from "@src/pages/Bookmark";
import Feed from "@src/pages/Feed";
import SpecificDateFeed from "@src/pages/SpecificDateFeed";
import Home from "@src/pages/Home";
import NotFound from "@src/pages/NotFound";
import { PATH_NAME } from "@src/@constants";

import LayoutContainer from "@src/components/@layouts/LayoutContainer";

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
