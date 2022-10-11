import { ChangeEvent, ChangeEventHandler, useState } from "react";

interface Props<T> {
  initialValue: T;
  isInvalid?: (value: any) => boolean;
}

interface UseInputResult<T> {
  value: T;
  handleChangeValue: ChangeEventHandler<HTMLInputElement>;
  changeValue: (value: T) => void;
}

function useInput<T>({ initialValue, isInvalid }: Props<T>): UseInputResult<T> {
  const [value, setValue] = useState(initialValue);

  const handleChangeValue = (event: ChangeEvent<HTMLInputElement>) => {
    if (isInvalid && isInvalid(event.target.value)) return;

    setValue(event.target.value as unknown as T);
  };

  const changeValue = (value: T) => {
    setValue(value);
  };

  return {
    value,
    handleChangeValue,
    changeValue,
  };
}

export default useInput;
