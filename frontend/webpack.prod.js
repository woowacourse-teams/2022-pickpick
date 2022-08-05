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
      "process.env.API_URL": JSON.stringify(process.env.API_URL),
      "process.env.SLACK_REDIRECT_URL": JSON.stringify(
        process.env.SLACK_REDIRECT_URL
      ),
    }),
  ],
});
