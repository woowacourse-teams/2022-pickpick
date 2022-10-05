import { ChangeEvent, ChangeEventHandler, useState } from "react";

import { Hours } from "@src/@utils";

interface Props {
  initialValue: string;
  invalidation?: (value: Hours) => boolean;
}

interface UseInputResult {
  value: string;
  handleChangeValue: ChangeEventHandler<HTMLInputElement>;
  changeValue: (value: string) => void;
}

function useInput({ initialValue, invalidation }: Props): UseInputResult {
  const [value, setValue] = useState(initialValue);

  const handleChangeValue = (event: ChangeEvent<HTMLInputElement>) => {
    if (invalidation && invalidation(Number(event.target.value) as Hours))
      return;

    setValue(event.target.value);
  };

  const changeValue = (value: string) => {
    setValue(value);
  };

  return {
    value,
    handleChangeValue,
    changeValue,
  };
}

export default useInput;
