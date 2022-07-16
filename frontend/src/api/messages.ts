import { ResponseMessages } from "@src/@types/shared";
import { fetcher } from ".";

// TODO: 백엔드에 초기 페이지 디폴트값 어떻게 구분할지  물어보기
// query ''
export const getMessages = async ({ pageParam = 0 }) => {
  const { data } = await fetcher.get<ResponseMessages>(
    `/api/messages?messageId=${pageParam}&size=20`
  );

  return data;
};
