package Util

import geb.report.CompositeReporter
import geb.report.ReportState
import geb.report.Reporter

class GebReporter extends CompositeReporter {
  int _gebReporterCounter = 1

  GebReporter(Reporter... reporters) {
    super(reporters)
  }

  @Override
  void writeReport(ReportState reportState) {
    Boolean takeScreenshots = System.getProperty("takeScreenshots", "true") == "true"
    Boolean onfailure = reportState.label.contains('On failure')
    if (onfailure || takeScreenshots) {
      def numberFormat = "%03d"
      def newLabel = "${String.format(numberFormat, _gebReporterCounter)}_${reportState.label}"
      def newReportState = new ReportState(reportState.browser, newLabel, reportState.outputDir)
      _gebReporterCounter++
      super.writeReport(newReportState)
    }
  }
}
