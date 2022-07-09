const getTimeStandard = (time: number): string => {
  if (time < 12) return `오전 ${time}`;
  if (time === 12) return `오후 ${12}`;

  return `오후 ${time - 12}`;
};

export const parseTime = (date: string): string => {
  const dateInstance = new Date(date);
  const hour = dateInstance.getHours();
  const minute = dateInstance.getMinutes();
  const timeStandard = getTimeStandard(Number(hour));

  return `${timeStandard}:${minute}`;
};
