import { MERIDIEM, TIME_UNIT } from "@src/@constants/date";
import { Range, ValueOf } from "@src/@types/utils";

export type Meridiem = ValueOf<typeof MERIDIEM>;

export type StandardHours = Range<0, 24>;

export type MeridiemHours = Range<1, 13>;

export type TimeUnit = ValueOf<typeof TIME_UNIT>;
