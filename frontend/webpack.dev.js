/* eslint-disable no-undef */
/* eslint-disable @typescript-eslint/no-var-requires */

const { merge } = require("webpack-merge");
const common = require("./webpack.common.js");
const { join } = require("path");
const { DefinePlugin } = require("webpack");
const { CleanWebpackPlugin } = require("clean-webpack-plugin");
const ReactRefreshWebpackPlugin = require("@pmmmwh/react-refresh-webpack-plugin");
const BundleAnalyzerPlugin =
  require("webpack-bundle-analyzer").BundleAnalyzerPlugin;

require("dotenv").config({ path: join(__dirname, "./.env.development") });

module.exports = merge(common, {
  mode: "development",
  devtool: "inline-source-map",
  plugins: [
    new CleanWebpackPlugin(),
    new ReactRefreshWebpackPlugin(),
    new DefinePlugin({
      "process.env.API_URL": JSON.stringify(process.env.API_URL),
      "process.env.SLACK_LOGIN_REDIRECT_URL": JSON.stringify(
        process.env.SLACK_LOGIN_REDIRECT_URL
      ),
      "process.env.SLACK_CLIENT_ID": JSON.stringify(
        process.env.SLACK_CLIENT_ID
      ),
      "process.env.SLACK_REGISTER_WORKSPACE_REDIRECT_URL": JSON.stringify(
        process.env.SLACK_REGISTER_WORKSPACE_REDIRECT_URL
      ),
    }),
    new BundleAnalyzerPlugin(),
  ],
});
