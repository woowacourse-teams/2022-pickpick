import { QueryClient, QueryCache } from "react-query";

const getQueryClient = (
  globalErrorHandler: (error: any) => void
): QueryClient =>
  new QueryClient({
    defaultOptions: {
      queries: {
        refetchOnWindowFocus: false,
      },
    },
    queryCache: new QueryCache({
      onError: globalErrorHandler,
    }),
  });

export default getQueryClient;
