import { ChangeEvent, useState, ChangeEventHandler } from "react";

interface Props {
  initialValue: string;
  invalidation?: (value: string) => boolean;
}

interface ReturnType {
  value: string;
  handleChangeValue: ChangeEventHandler<HTMLInputElement>;
  changeValue: (value: string) => void;
}

function useInput({ initialValue, invalidation }: Props): ReturnType {
  const [value, setValue] = useState(initialValue);

  const handleChangeValue = (event: ChangeEvent<HTMLInputElement>) => {
    if (invalidation && invalidation(event.target.value)) return;

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
