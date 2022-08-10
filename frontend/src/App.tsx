import { useRoutes } from "react-router-dom";
import Snackbar from "./components/Snackbar";
import useApiError from "./hooks/useApiError";
import routes from "./Routes";
import { QueryClientProvider } from "react-query";
import { useEffect } from "react";
import queryClient from "./queryClient";
import { ReactQueryDevtools } from "react-query/devtools";

function App() {
  const { handleError } = useApiError();
  const element = useRoutes(routes);

  useEffect(() => {
    queryClient.setDefaultOptions({
      queries: {
        refetchOnWindowFocus: false,
        onError: handleError,
      },
      mutations: {
        onError: handleError,
      },
    });
  }, []);

  return (
    <QueryClientProvider client={queryClient}>
      {element}
      <Snackbar />
      <ReactQueryDevtools initialIsOpen={false} position="bottom-left" />
    </QueryClientProvider>
  );
}

export default App;
