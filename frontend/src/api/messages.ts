export const getMessages = () =>
  fetch("/api/messages").then((response) => response.json());
