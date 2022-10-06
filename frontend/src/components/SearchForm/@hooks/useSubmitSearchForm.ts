import {
  ChangeEvent,
  ChangeEventHandler,
  FormEvent,
  FormEventHandler,
  useState,
} from "react";
import { useNavigate } from "react-router-dom";

import useSnackbar from "@src/hooks/useSnackbar";

import { MESSAGE, PATH_NAME } from "@src/@constants";
import { SEARCH_PARAMS } from "@src/@constants/api";

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
        openFailureSnackbar(MESSAGE.INVALID_SEARCH_CHANNELS);

        return;
      }

      if (!searchKeyword.trim().length) {
        openFailureSnackbar(MESSAGE.INVALID_SEARCH_KEYWORD);

        return;
      }

      navigate(
        `${PATH_NAME.SEARCH_RESULT}?${
          SEARCH_PARAMS.SEARCH_KEYWORD
        }=${searchKeyword}&${
          SEARCH_PARAMS.SEARCH_CHANNEL_IDS
        }=${selectedChannelIds.join(",")}`
      );
    };

  return {
    searchKeyword,
    handleChangeSearchKeyword,
    handleSubmitSearchKeyword,
  };
}

export default useSubmitSearchForm;
