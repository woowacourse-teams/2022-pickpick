import ReactDOM from "react-dom/client";
import App from "./App";
import { BrowserRouter } from "react-router-dom";
import ErrorBoundary from "./components/ErrorBoundary";
import UnexpectedError from "./pages/UnexpectedError";
import { RecoilRoot } from "recoil";
import { initMSW } from "./mocks";

initMSW();

const root = ReactDOM.createRoot(document.getElementById("root") as Element);

root.render(
  <BrowserRouter>
    <ErrorBoundary fallback={<UnexpectedError />}>
      <RecoilRoot>
        <App />
      </RecoilRoot>
    </ErrorBoundary>
  </BrowserRouter>
);
