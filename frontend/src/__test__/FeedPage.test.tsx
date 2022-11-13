import Feed from "@src/pages/Feed";
import { fireEvent, screen, waitFor } from "@testing-library/react";

import { customRender } from "./utils";

describe("피드 페이지 테스트", () => {
  beforeEach(async () => {
    customRender(<Feed />);
  });

  it("사용자는 메시지를 확인 할 수 있다.", async () => {
    await waitFor(() => {
      const messages = screen.getAllByRole("listItem");
      expect(messages.length).toBe(20);
    });
  });

  it("사용자는 해당하는 날짜의 드랍다운을 하나 씩 확인 할 수 있다.", async () => {
    await waitFor(() => {
      const dateDropdown = screen.getAllByText("7월 18일 월요일");
      expect(dateDropdown.length).toBe(1);
    });
  });

  it("사용자는 검색창을 포커스 할 때, 검색 옵션을 확인 할 수 있다.", async () => {
    await waitFor(() => {
      const searchInput =
        screen.getByPlaceholderText("검색 할 키워드를 입력해주세요.");

      fireEvent.focus(searchInput);
      const channelList = screen.getByText(
        "검색에 포함 할 채널을 선택해주세요."
      );
      expect(channelList).toBeVisible();
    });
  });
});
