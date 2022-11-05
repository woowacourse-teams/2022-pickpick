import "@testing-library/jest-dom";
import "@testing-library/react";
import { cleanup } from "@testing-library/react";

import IntersectionObserverMock from "@src/mocks/IntersectionObserverMock";

import server from "./src/mocks/server";

beforeAll(() => {
  server.listen();
  window.IntersectionObserver = IntersectionObserverMock;
});

afterAll(() => {
  server.close();
  jest.resetAllMocks();
});

afterEach(() => {
  cleanup();
  server.resetHandlers();
});
