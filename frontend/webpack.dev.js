/* eslint-disable no-undef */
/* eslint-disable @typescript-eslint/no-var-requires */

const { merge } = require("webpack-merge");
const common = require("./webpack.common.js");
const { join } = require("path");
const ReactRefreshWebpackPlugin = require("@pmmmwh/react-refresh-webpack-plugin");
const BundleAnalyzerPlugin =
  require("webpack-bundle-analyzer").BundleAnalyzerPlugin;

require("dotenv").config({ path: join(__dirname, "./.env.development") });

module.exports = merge(common, {
  mode: "development",
  devtool: "inline-source-map",
  plugins: [
    new ReactRefreshWebpackPlugin(),
    new BundleAnalyzerPlugin({ openAnalyzer: true }),
  ],
  optimization: {
    minimize: false,
  },
});
