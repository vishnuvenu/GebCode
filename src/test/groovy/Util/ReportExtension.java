package Util;

import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension;
import org.spockframework.runtime.model.FeatureInfo;
import org.spockframework.runtime.model.MethodInfo;
import org.spockframework.runtime.model.SpecInfo;
import annotation.Report;

/**
 * Annotation driven extension. Takes report before each setup or setupSpec, before and after any feature and after
 * cleanup or cleanupSpec.
 * @author p. hilt
 */
public class ReportExtension extends AbstractAnnotationDrivenExtension<Report> {

  /**
   * Visit spec annotations, get the 'bottom' spec in terms of inheritance and add interceptor for each feature
   * and fixture methods.
   *
   * @param report
   * @param spec
   */
  public void visitSpecAnnotation(Report report, SpecInfo spec) {
    while (spec != null) {
      for (FeatureInfo feature : spec.getFeatures()) {
        feature.addInterceptor(new ReportFeatureInterceptor());
        feature.getFeatureMethod().addInterceptor(new ReportFeatureInterceptor());
      }
      for (MethodInfo fixture : spec.getAllFixtureMethods()) {
        fixture.addInterceptor(new ReportFixtureInterceptor());
      }
      spec = spec.getSubSpec();
    }
  }

}