import { CONVERTER_SUFFIX, DATE, DAY, TIME } from "@src/@constants/date";
import { Range } from "@src/@types/utils";

export type Meridiem = "오전" | "오후";

export type Hours = Range<0, 24>;

export type MeridiemHours = Range<1, 13>;

export type GetMeridiemTime = (time: Hours) => {
  meridiem: Meridiem;
  hour: MeridiemHours;
};

export const getMeridiemTime: GetMeridiemTime = (time) => {
  if (time < TIME.NOON)
    return { meridiem: TIME.AM, hour: time as MeridiemHours };

  if (time === TIME.NOON)
    return { meridiem: TIME.PM, hour: TIME.NOON as MeridiemHours };

  return {
    meridiem: TIME.PM,
    hour: (time - TIME.NOON) as MeridiemHours,
  };
};

type ParseMeridiemTime = (date: string) => string;

export const parseMeridiemTime: ParseMeridiemTime = (date) => {
  const dateInstance = new Date(date);
  const hour = dateInstance.getHours() as Range<0, 24>;
  const minute = dateInstance.getMinutes();
  const { meridiem, hour: parsedHour } = getMeridiemTime(hour);

  return `${meridiem} ${parsedHour}:${minute.toString().padStart(2, "0")}`;
};

/**
 * TODO: 함수 분리
 */

export const ISOConverter = (date: string, time?: string): string => {
  const today = new Date();

  if (date === DATE.TODAY) {
    return `${today.toISOString().split("T")[0]}${CONVERTER_SUFFIX}`;
  }

  if (date === DATE.YESTERDAY) {
    const yesterday = new Date(today.setDate(today.getDate() - 1));

    return `${yesterday.toISOString().split("T")[0]}${CONVERTER_SUFFIX}`;
  }

  const [year, month, day] = date.split("-");

  if (time) {
    const [hour, minute] = time.split(":");

    return `${year}-${month.padStart(2, "0")}-${day.padStart(
      2,
      "0"
    )}${`T${hour.padStart(2, "0")}:${minute.padStart(2, "0")}:00`}`;
  }

  return `${year}-${month.padStart(2, "0")}-${day.padStart(
    2,
    "0"
  )}${CONVERTER_SUFFIX}`;
};

type GetDateInformation = (givenDate: Date) => {
  year: number;
  month: number;
  date: number;
  day: string;
  hour: number;
  minute: number;
};

export const getDateInformation: GetDateInformation = (givenDate) => {
  const year = givenDate.getFullYear();
  const month = givenDate.getMonth() + 1;
  const date = givenDate.getDate();
  const day = DAY[givenDate.getDay()];
  const hour = givenDate.getHours();
  const minute = givenDate.getMinutes();

  return { year, month, date, day, hour, minute };
};

type GetMessagesDate = (postedDate: string) => string;

export const getMessagesDate: GetMessagesDate = (postedDate) => {
  const givenDate = getDateInformation(new Date(postedDate));
  const today = getDateInformation(new Date());

  if (
    givenDate.year === today.year &&
    givenDate.month === today.month &&
    givenDate.date === today.date
  )
    return DATE.TODAY;

  if (
    givenDate.year === today.year &&
    givenDate.month === today.month &&
    givenDate.date === today.date - 1
  )
    return DATE.YESTERDAY;

  return `${givenDate.month}월 ${givenDate.date}일 ${givenDate.day}`;
};

type ParsedOptionText = ({
  needZeroPaddingStart,
  optionText,
}: {
  needZeroPaddingStart: boolean;
  optionText: string;
}) => string;

export const parsedOptionText: ParsedOptionText = ({
  needZeroPaddingStart,
  optionText,
}) => {
  return needZeroPaddingStart ? optionText.padStart(2, "0") : optionText;
};

type GenerateTimeOptions = () => Record<
  "meridiems" | "AMHours" | "PMHours" | "minutes",
  string[]
>;

// {
//   // meridiems: ["오전", "오후"];
//   // AMHours: ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"];
//   // PMHours: ["12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"];
//   // minutes: ["00", "10", "20", "30", "40", "50"];

// };

export const generateTimeOptions: GenerateTimeOptions = () => {
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

type GenerateDateOptions = () => Record<"years" | "months" | "dates", string[]>;

export const generateDateOptions: GenerateDateOptions = () => {
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

type ParseTime = (ISODateTime: string) => {
  meridiem: Meridiem;
  meridiemHour: MeridiemHours;
  minute: string;
};

export const parseTime: ParseTime = (ISODateTime) => {
  const [_, fullTime] = ISODateTime.split("T");
  const [hour, minute] = fullTime.split(":");
  const { meridiem: meridiem, hour: meridiemHour } = getMeridiemTime(
    Number(hour) as Hours
  );

  return {
    meridiem,
    meridiemHour,
    minute,
  };
};

type ParseDate = (
  ISODateTime: string
) => Record<"year" | "month" | "date", string>;

export const parseDate: ParseDate = (ISODateTime) => {
  const [fullDate] = ISODateTime.split("T");
  const [year, month, date] = fullDate.split("-");

  return {
    year,
    month,
    date,
  };
};

type ConvertTimeToStepTenMinuteTime = ({
  hour,
  minute,
}: {
  hour: number;
  minute: number;
}) => Record<"parsedHour" | "parsedMinute", number>;

export const convertTimeToStepTenMinuteTime: ConvertTimeToStepTenMinuteTime = ({
  hour,
  minute,
}) => {
  if (minute > 50) {
    return { parsedHour: hour + 1, parsedMinute: 0 };
  }

  return { parsedHour: hour, parsedMinute: Math.ceil(minute / 10) * 10 };
};

type InvalidMeridiem = (value: Meridiem) => boolean;

export const invalidMeridiem: InvalidMeridiem = (value) => {
  return value !== "오전" && value !== "오후";
};

interface IsInvalidateDateTimeProps {
  checkedYear: number;
  checkedMonth: number;
  checkedDate: number;
  checkedHour: number;
  checkedMinute: number;
  year: number;
  month: number;
  date: number;
  hour: number;
  minute: number;
}

export const isInvalidateDateTime = ({
  checkedYear,
  checkedMonth,
  checkedDate,
  checkedHour,
  checkedMinute,
  year,
  month,
  date,
  hour,
  minute,
}: IsInvalidateDateTimeProps) => {
  if (checkedYear < year) return true;
  if (checkedYear <= year && checkedMonth < month) return true;
  if (checkedYear <= year && checkedMonth <= month && checkedDate < date)
    return true;

  if (
    checkedYear <= year &&
    checkedMonth <= month &&
    checkedDate <= date &&
    checkedHour < hour
  )
    return true;

  if (
    checkedYear <= year &&
    checkedMonth <= month &&
    checkedDate <= date &&
    checkedHour <= hour &&
    checkedMinute <= minute
  )
    return true;

  return false;
};

export const convertMeridiemHourToStandardHour = (
  meridiem: string,
  meridiemHour: number
): number => {
  if (meridiem === "오후") {
    return meridiemHour === 12 ? 12 : meridiemHour + 12;
  }

  return meridiemHour;
};

interface GetReplaceDateTimeProps {
  checkedYear: string;
  checkedMonth: string;
  checkedDate: string;
  checkedMeridiem: string;
  checkedHour: string;
  checkedMinute: string;
}

export const getReplaceDateTime = ({
  checkedYear,
  checkedMonth,
  checkedDate,
  checkedMeridiem,
  checkedHour,
  checkedMinute,
}: GetReplaceDateTimeProps) => {
  const replaceCheckedYear = Number(checkedYear.replace("년", ""));
  const replaceCheckedMonth = Number(checkedMonth.replace("월", ""));
  const replaceCheckedDate = Number(checkedDate.replace("일", ""));

  const replaceCheckedHour = convertMeridiemHourToStandardHour(
    checkedMeridiem,
    Number(checkedHour.replace("시", ""))
  );
  const replaceCheckedMinute = Number(checkedMinute.replace("분", ""));

  return {
    replaceCheckedYear,
    replaceCheckedMonth,
    replaceCheckedDate,
    replaceCheckedHour,
    replaceCheckedMinute,
  };
};
