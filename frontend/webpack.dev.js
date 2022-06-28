/* eslint-disable no-undef */
/* eslint-disable @typescript-eslint/no-var-requires */

const { merge } = require("webpack-merge");
const common = require("./webpack.common.js");

const { CleanWebpackPlugin } = require("clean-webpack-plugin");
const ReactRefreshWebpackPlugin = require("@pmmmwh/react-refresh-webpack-plugin");

module.exports = merge(common, {
  mode: "development",
  devtool: "inline-source-map",
  //   devServer: {
  //     static: "./dist",
  //   },
  plugins: [new CleanWebpackPlugin(), new ReactRefreshWebpackPlugin()],
});
