import { useRoutes } from "react-router-dom";
import Snackbar from "./components/Snackbar";
import useApiError from "./hooks/useApiError";
import routes from "./Routes";
import { QueryClientProvider } from "react-query";
import getQueryClient from "./queryClient";

function App() {
  const { handleError } = useApiError();
  const element = useRoutes(routes);

  return (
    <QueryClientProvider client={getQueryClient(handleError)}>
      {element}
      <Snackbar />
    </QueryClientProvider>
  );
}

export default App;
