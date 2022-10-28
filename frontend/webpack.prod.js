/* eslint-disable no-undef */
/* eslint-disable @typescript-eslint/no-var-requires */

const { merge } = require("webpack-merge");
const { DefinePlugin } = require("webpack");
const common = require("./webpack.common.js");
const { join } = require("path");

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
});
