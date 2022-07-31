// SAMPLE TEST

const sum = (arg1, arg2) => {
  return arg1 + arg2;
};

test("adds 1 + 2 to equal 3", () => {
  expect(sum(1, 2)).toBe(3);
});
