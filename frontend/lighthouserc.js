/* eslint-disable no-undef */

module.exports = {
  ci: {
    collect: {
      numberOfRuns: 1,
    },
    upload: {
      target: "filesystem",
      outputDir: "./lhci_reports",
      reportFilenamePattern: "%%PATHNAME%%-%%DATETIME%%-report.%%EXTENSION%%",
    },
  },
};
