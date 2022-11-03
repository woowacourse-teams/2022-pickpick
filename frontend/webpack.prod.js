/* eslint-disable no-undef */
/* eslint-disable @typescript-eslint/no-var-requires */

const { merge } = require("webpack-merge");
const common = require("./webpack.common.js");
const { join } = require("path");
const { ESBuildMinifyPlugin } = require("esbuild-loader");

require("dotenv").config({ path: join(__dirname, "./.env.production") });

module.exports = merge(common, {
  mode: "production",
  plugins: [],
  optimization: {
    minimizer: [
      new ESBuildMinifyPlugin({
        target: "esnext",
      }),
    ],
  },
});
