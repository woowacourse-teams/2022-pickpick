import ReactDOM from "react-dom/client";
import App from "@src/App";
import { BrowserRouter } from "react-router-dom";
import ErrorBoundary from "@src/components/ErrorBoundary";
import UnexpectedError from "@src/pages/UnexpectedError";
import { RecoilRoot } from "recoil";
import { initMSW } from "@src/mocks";

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
