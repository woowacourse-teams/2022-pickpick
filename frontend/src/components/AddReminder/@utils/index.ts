import { getDateInformation, getMeridiemTime } from "@src/@utils";

export const generateTimeOptions = () => {
  const meridiems = ["오전", "오후"];
  const AMHours = Array.from({ length: 12 }, (_, index) => index.toString());
  const PMHours = Array.from({ length: 12 }, (_, index) => {
    if (index === 0) return "12";

    return index.toString();
  });
  const minutes = Array.from({ length: 6 }, (_, index) =>
    (index * 10).toString()
  );

  return {
    meridiems,
    AMHours,
    PMHours,
    minutes,
  };
};

export const generateDateOptions = () => {
  const { year, month } = getDateInformation(new Date());
  const { date: lastDate } = getDateInformation(new Date(year, month, 0));

  const years = [year, year + 1, year + 2].map((year) => year.toString());
  const months = Array.from({ length: 12 }, (_, index) =>
    (index + 1).toString()
  );
  const dates = Array.from({ length: lastDate }, (_, index) =>
    (index + 1).toString()
  );

  return {
    years,
    months,
    dates,
  };
};

export const parseTime = (ISODateTime: string) => {
  const [_, fullTime] = ISODateTime.split("T");
  const [hour, minute] = fullTime.split(":");
  const { meridiem: meridiem, hour: meridiemHour } = getMeridiemTime(
    Number(hour)
  );

  return {
    meridiem,
    meridiemHour,
    minute,
  };
};

export const parseDate = (ISODateTime: string) => {
  const [fullDate] = ISODateTime.split("T");
  const [year, month, date] = fullDate.split("-");

  return {
    year,
    month,
    date,
  };
};

export const convertTimeToStepTenMinuteTime = ({
  hour,
  minute,
}: {
  hour: number;
  minute: number;
}) => {
  if (minute > 50) {
    return { parsedHour: hour + 1, parsedMinute: 0 };
  }

  return { parsedHour: hour, parsedMinute: Math.ceil(minute / 10) * 10 };
};

export const invalidMeridiem = (value: string) => {
  return value !== "오전" && value !== "오후";
};
