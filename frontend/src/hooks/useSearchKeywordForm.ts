import { ChangeEvent, FormEvent, useState } from "react";
import { useNavigate } from "react-router-dom";
import useSnackbar from "@src/hooks/useSnackbar";
import { PATH_NAME } from "@src/@constants";

interface Props {
  selectedChannelIds: number[];
}

interface ReturnType {
  searchKeyword: string;
  handleChangeSearchKeyword: (event: ChangeEvent<HTMLInputElement>) => void;
  handleSubmitSearchKeyword: (event: FormEvent) => void;
}

function useSearchKeywordForm({ selectedChannelIds }: Props): ReturnType {
  const navigate = useNavigate();
  const { openFailureSnackbar } = useSnackbar();
  const [searchKeyword, setSearchKeyword] = useState("");

  const handleChangeSearchKeyword = (event: ChangeEvent<HTMLInputElement>) => {
    setSearchKeyword(event.target.value);
  };

  const handleSubmitSearchKeyword = (event: FormEvent) => {
    event.preventDefault();

    if (!selectedChannelIds.length) {
      openFailureSnackbar("채널을 하나 이상 선택 후 검색 버튼을 눌러주세요.");

      return;
    }

    if (!searchKeyword.length) {
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

export default useSearchKeywordForm;
