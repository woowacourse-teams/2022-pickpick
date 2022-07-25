import { useRoutes } from "react-router-dom";
import Snackbar from "./components/Snackbar";
import routes from "./Routes";

function App() {
  const element = useRoutes(routes);
  return (
    <>
      {element}
      <Snackbar />
    </>
  );
}

export default App;
