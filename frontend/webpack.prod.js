/* eslint-disable no-undef */
/* eslint-disable @typescript-eslint/no-var-requires */

const { merge } = require("webpack-merge");
const common = require("./webpack.common.js");
const { join } = require("path");
const { ESBuildMinifyPlugin } = require("esbuild-loader");
const { DefinePlugin } = require("webpack");

require("dotenv").config({ path: join(__dirname, "./.env.production") });

module.exports = merge(common, {
  mode: "production",
  plugins: [
    new DefinePlugin({
      "process.env.SLACK_LOGIN_URL": JSON.stringify(
        process.env.SLACK_LOGIN_URL
      ),
      "process.env.SLACK_REGISTER_WORKSPACE_URL": JSON.stringify(
        process.env.SLACK_REGISTER_WORKSPACE_URL
      ),
    }),
  ],
  optimization: {
    minimizer: [
      new ESBuildMinifyPlugin({
        target: "esnext",
      }),
    ],
  },
});
