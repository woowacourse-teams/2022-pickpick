import SpecificDateFeed from "@src/pages/SpecificDateFeed";
import { fireEvent, screen, waitFor } from "@testing-library/react";

import { customRender } from "./utils";

jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useParams: () => ({
    channelId: "main",
    date: "2022-07-16T23:59:59",
  }),
}));

describe("특정 날짜 피드 페이지 테스트", () => {
  beforeEach(async () => {
    customRender(<SpecificDateFeed />);
  });

  it("사용자는 해당 날짜의 메시지를 확인 할 수 있다.", async () => {
    await waitFor(() => {
      const selectedDate = screen.getByText("7월 16일 토요일");
      expect(selectedDate).toBeVisible();
    });
  });

  it("사용자는 상향 스크롤을 통해 선택된 날짜보다 미래의 메시지를 확인 할 수 있다.", async () => {
    fireEvent.scroll(window, { target: { scrollY: -100 } });
    await waitFor(() => {
      const futureDate = screen.getByText("7월 18일 월요일");
      expect(futureDate).toBeVisible();
    });
  });
});
