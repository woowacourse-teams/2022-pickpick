export const STORAGE_KIND = {
  LOCAL: "LOCAL",
  SESSION: "SESSION",
} as const;

type Storage = keyof typeof STORAGE_KIND;

interface Props {
  key: string;
  kind: Storage;
}

function useWebStorage<T>({ key, kind }: Props) {
  const storage = kind === STORAGE_KIND.LOCAL ? localStorage : sessionStorage;

  function get(): T {
    return JSON.parse(storage.getItem(key) ?? "[]");
  }

  function set(item: T): void {
    storage.setItem(key, JSON.stringify(item));
  }

  return [get, set] as const;
}

export default useWebStorage;
