import { CONVERTER_SUFFIX } from "@src/@constants";
import {
  bookmarkFeedResponseData,
  mainFeedResponseData,
  refinedBookmarkFeedResponseData,
  refinedFeedResponseData,
} from "@src/mocks/data/testResponseData";
import {
  extractResponseBookmarks,
  extractResponseMessages,
  getTimeStandard,
  ISOConverter,
  parseTime,
} from "./index";

describe("24시간제의 시간이 입력되면 오전/오후 prefix를 붙여 변환된 시간을 반환하는지 확인한다.", () => {
  test("11이 입력됐을 경우 오전 prefix를 붙여 '오전 11'을 반환한다.", () => {
    const inputTime = 11;

    expect(getTimeStandard(inputTime)).toBe("오전 11");
  });

  test("23이 입력됐을 경우 오후 prefix를 붙여 '오후 11'을 반환한다.", () => {
    const inputTime = 23;

    expect(getTimeStandard(inputTime)).toBe("오후 11");
  });
});

describe("서버에서 받아온 ISO 형식의 date 값을 '오전/오후 00:00' 형식의 시간으로 변환된 값을 반환하는지 확인한다. ", () => {
  test("'2022-07-18T14:50:58.972493'이 입력됐을 경우 '오후 2:50'을 반환한다.", () => {
    const inputTime = "2022-07-18T14:50:58.972493";

    expect(parseTime(inputTime)).toBe("오후 2:50");
  });

  test("'2022-07-18T09:33:58.972493'이 입력됐을 경우 '오전 9:33'을 반환한다.", () => {
    const inputTime = "2022-07-18T09:33:58.972493";

    expect(parseTime(inputTime)).toBe("오전 9:33");
  });
});

describe("서버에서 받아온 메인 피드 데이터에서 필요한 메시지를 잘 추출해 새로운 배열을 만들어 주는지 확인한다.", () => {
  test("서버에서 받아온 데이터가 존재할 경우 정제된 배열을 반환한다.", () => {
    expect(extractResponseMessages(mainFeedResponseData)).toEqual(
      refinedFeedResponseData
    );
  });

  test("서버에서 받아온 데이터가 존재하지 않을 경우 빈 배열을 반환한다.", () => {
    const emptyResponseData = { pageParams: [], pages: [] };

    expect(extractResponseMessages(emptyResponseData)).toEqual([]);
  });
});

describe("서버에서 받아온 북마크 피드 데이터에서 필요한 메시지를 잘 추출해 새로운 배열을 만들어 주는지 확인한다.", () => {
  test("서버에서 받아온 데이터가 존재할 경우 정제된 배열을 반환한다.", () => {
    expect(extractResponseBookmarks(bookmarkFeedResponseData)).toEqual(
      refinedBookmarkFeedResponseData
    );
  });

  test("서버에서 받아온 데이터가 존재하지 않을 경우 빈 배열을 반환한다.", () => {
    const emptyResponseData = { pageParams: [], pages: [] };

    expect(extractResponseBookmarks(emptyResponseData)).toEqual([]);
  });
});

describe("date로 들어오는 '어제', '오늘', '특정날짜(2022-8-12)'를 ISO 형식의 date 타입으로 변경 해주는지 확인한다.", () => {
  test("어제가 date일 경우 현재 시간 기준 어제의 날짜를 ISO 형식의 date 값을 반환한다. (2022-8-12 기준 2022-08-11T23:59:59)", () => {
    const inputDate = "어제";
    const today = new Date();
    const yesterday = new Date(today.setDate(today.getDate() - 1));

    expect(ISOConverter(inputDate)).toBe(
      `${yesterday.toISOString().split("T")[0]}${CONVERTER_SUFFIX}`
    );
  });

  test("오늘이 date일 경우 현재 시간 기준 오늘의 날짜를 ISO 형식의 date 값을 반환한다. (2022-8-12 기준 2022-08-12T23:59:59)", () => {
    const inputDate = "오늘";
    const today = new Date();

    expect(ISOConverter(inputDate)).toBe(
      `${today.toISOString().split("T")[0]}${CONVERTER_SUFFIX}`
    );
  });

  test("특정 날짜(2022-8-12)가 date일 경우 특정 날짜를 ISO 형식의 date 값을 반환한다. (2022-08-12T23:59:59)", () => {
    const inputDate = "2022-8-12";

    expect(ISOConverter(inputDate)).toBe("2022-08-12T23:59:59");
  });
});
