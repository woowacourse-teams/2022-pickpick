import NotFound from "@src/pages/NotFound";
import { PATH_NAME } from "@src/@constants";
import LayoutContainer from "@src/components/@layouts/LayoutContainer";
import {
  AddChannel,
  Bookmark,
  Reminder,
  Feed,
  SpecificDateFeed,
  Home,
  Certification,
  SearchResult,
} from "./pages";
import PrivateRouter from "@src/components/PrivateRouter";
import PublicRouter from "@src/components/PublicRouter";

const routes = [
  {
    path: "",
    element: (
      <PublicRouter>
        <LayoutContainer>
          <Home />
        </LayoutContainer>
      </PublicRouter>
    ),
  },
  {
    path: PATH_NAME.HOME,
    element: <PrivateRouter />,
    children: [
      {
        path: PATH_NAME.ADD_CHANNEL,
        element: (
          <LayoutContainer>
            <AddChannel />
          </LayoutContainer>
        ),
      },
      {
        path: PATH_NAME.BOOKMARK,
        element: (
          <LayoutContainer>
            <Bookmark />
          </LayoutContainer>
        ),
      },
      {
        path: PATH_NAME.REMINDER,
        element: (
          <LayoutContainer>
            <Reminder />
          </LayoutContainer>
        ),
      },
      {
        path: PATH_NAME.FEED,
        element: (
          <LayoutContainer>
            <Feed />
          </LayoutContainer>
        ),
      },
      {
        path: `${PATH_NAME.FEED}/:channelId`,
        element: (
          <LayoutContainer>
            <Feed />
          </LayoutContainer>
        ),
      },
      {
        path: `${PATH_NAME.FEED}/:channelId/:date`,
        element: (
          <LayoutContainer>
            <SpecificDateFeed />
          </LayoutContainer>
        ),
      },

      {
        path: PATH_NAME.SEARCH_RESULT,
        element: (
          <LayoutContainer>
            <SearchResult />
          </LayoutContainer>
        ),
      },
      {
        path: "*",
        element: (
          <LayoutContainer>
            <NotFound />
          </LayoutContainer>
        ),
      },
    ],
  },
  {
    path: PATH_NAME.CERTIFICATION,
    element: <Certification />,
  },
];

export default routes;
