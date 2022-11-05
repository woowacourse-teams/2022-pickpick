import { screen, waitFor } from "@testing-library/react";
import React from "react";

import Home from "../pages/Home";
import { customRender } from "./utils";

describe("일반 피드 페이지 테스트", () => {
  beforeEach(() => {
    customRender(<Home />);
  });
  it("should", async () => {
    waitFor(() => expect(screen.getByText("hello")).toBeInTheDocument());
  });
});
