type SetCookie = (key: string, value: string) => void;

export const setCookie: SetCookie = (key, value) => {
  document.cookie = `${key}=${value};`;
};

type GetCookie = (key: string) => string;

export const getCookie: GetCookie = (key) => {
  const regex = new RegExp(`${key}=([^;]+)`); // key(좌항)에 해당하는 우항을 가져온다. 세미콜론은 제외한다.
  const matches = document.cookie.match(regex);

  return matches ? matches[1] : "";
};

type DeleteCookie = (key: string) => void;

export const deleteCookie: DeleteCookie = (key) => {
  document.cookie = key + "=; Max-Age=0";
};
