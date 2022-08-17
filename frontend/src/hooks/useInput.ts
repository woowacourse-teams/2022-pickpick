import { ChangeEvent, useState } from "react";

interface Props {
  initialValue: string;
  invalidation?: (value: string) => boolean;
}

function useInput({ initialValue, invalidation }: Props) {
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
