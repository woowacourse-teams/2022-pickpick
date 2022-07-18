import ReactDOM from "react-dom/client";
import App from "./App";
import { BrowserRouter } from "react-router-dom";
if (process.env.NODE_ENV === "development") {
  // eslint-disable-next-line @typescript-eslint/no-var-requires
  const { worker } = require("./mocks/browser");
  worker.start();
}

const root = ReactDOM.createRoot(document.getElementById("root") as Element);

root.render(
  <BrowserRouter>
    <App />
  </BrowserRouter>
);
