import type { Config } from "@jest/types";

const config: Config.InitialOptions = {
  bail: 1, // 테스트 1개 실패 할 시 전체 테스트 종료
  errorOnDeprecated: true,

  rootDir: ".",
  moduleDirectories: ["node_modules", "src", "public"],
  moduleNameMapper: {
    "^@src/(.*)$": "<rootDir>/src/$1",
    "\\.(ttf|woff|woff2|png|svg)$": "<rootDir>/src/mocks/fileMock.ts",
  },

  moduleFileExtensions: ["ts", "tsx", "js", "json", "jsx", "json"],

  transform: {
    "^.+\\.(js|jsx|ts|tsx)?$": "ts-jest",
  },

  testEnvironment: "jsdom",
  testMatch: ["<rootDir>/**/*.test.(js|jsx|ts|tsx)"],

  setupFilesAfterEnv: ["<rootDir>/setupTests.ts"],
};

export default config;
