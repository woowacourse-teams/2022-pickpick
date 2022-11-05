import "@testing-library/jest-dom";
import "@testing-library/react";
import { cleanup } from "@testing-library/react";

import server from "./src/mocks/server";

beforeAll(() => {
  server.listen();
});

afterAll(() => {
  server.close();
  jest.resetAllMocks();
});

afterEach(() => {
  cleanup();
  server.resetHandlers();
});
