export const STORAGE_KIND = {
  LOCAL: "LOCAL",
  SESSION: "SESSION",
} as const;

type Storage = keyof typeof STORAGE_KIND;

interface Props {
  key: string;
  kind: Storage;
}

interface ReturnType<T> {
  getItem: () => T;
  setItem: (item: T) => void;
}

function useWebStorage<T>({ key, kind }: Props): ReturnType<T> {
  const storage = kind === STORAGE_KIND.LOCAL ? localStorage : sessionStorage;

  const getItem = (): T => {
    return JSON.parse(storage.getItem(key) ?? "[]");
  };

  const setItem = (item: T): void => {
    storage.setItem(key, JSON.stringify(item));
  };

  return { getItem, setItem };
}

export default useWebStorage;
