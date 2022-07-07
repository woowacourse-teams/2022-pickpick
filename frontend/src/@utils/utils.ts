const FIRST_LINE_MAX_LENGTH = 16;
const SECOND_LINE_MAX_LENGTH = 32;

const sliceText = (text: string): string =>
  text.slice(0, FIRST_LINE_MAX_LENGTH);

export const parseText = (text: string): string => {
  const splittedArray = text.split("\n");

  if (
    splittedArray.length < 2 &&
    splittedArray[0].length <= FIRST_LINE_MAX_LENGTH
  )
    return splittedArray[0];

  if (splittedArray[0].length > SECOND_LINE_MAX_LENGTH)
    return sliceText(splittedArray[0]) + "...";

  if (
    splittedArray.length < 3 &&
    splittedArray[1].length < FIRST_LINE_MAX_LENGTH
  )
    return `${splittedArray[0]}\n${splittedArray[1]}`;

  if (
    splittedArray.length < 3 &&
    splittedArray[1].length > FIRST_LINE_MAX_LENGTH
  )
    return `${splittedArray[0]}\n${sliceText(splittedArray[1])} ...`;

  if (splittedArray[1].length < FIRST_LINE_MAX_LENGTH)
    return `${splittedArray[0]}\n${splittedArray[1]} ...`;

  return `${splittedArray[0]}\n${sliceText(splittedArray[1])} ...`;
};
