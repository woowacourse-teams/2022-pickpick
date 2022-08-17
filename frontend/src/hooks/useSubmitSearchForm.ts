import {
  ChangeEventHandler,
  ChangeEvent,
  FormEventHandler,
  FormEvent,
  useState,
} from "react";
import { useNavigate } from "react-router-dom";
import useSnackbar from "@src/hooks/useSnackbar";
import { PATH_NAME } from "@src/@constants";

interface Props {
  keyword: string;
}
interface ReturnType {
  searchKeyword: string;
  handleChangeSearchKeyword: ChangeEventHandler<HTMLInputElement>;
  handleSubmitSearchKeyword: (selectedChannelIds: number[]) => FormEventHandler;
}

function useSubmitSearchForm({ keyword }: Props): ReturnType {
  const navigate = useNavigate();
  const { openFailureSnackbar } = useSnackbar();
  const [searchKeyword, setSearchKeyword] = useState(keyword);

  const handleChangeSearchKeyword = ({
    target,
  }: ChangeEvent<HTMLInputElement>) => {
    setSearchKeyword(target.value);
  };

  const handleSubmitSearchKeyword =
    (selectedChannelIds: number[]) => (event: FormEvent) => {
      event.preventDefault();

      if (!selectedChannelIds.length) {
        openFailureSnackbar("채널을 하나 이상 선택 후 검색 버튼을 눌러주세요.");

        return;
      }

      if (!searchKeyword.trim().length) {
        openFailureSnackbar(
          "검색할 키워드를 입력하신 후 검색 버튼을 눌러주세요."
        );

        return;
      }

      navigate(
        `${
          PATH_NAME.SEARCH_RESULT
        }?keyword=${searchKeyword}&channelIds=${selectedChannelIds.join(",")}`
      );
    };

  return {
    searchKeyword,
    handleChangeSearchKeyword,
    handleSubmitSearchKeyword,
  };
}

export default useSubmitSearchForm;
