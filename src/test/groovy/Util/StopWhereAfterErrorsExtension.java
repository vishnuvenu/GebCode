package Util;

import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension;
import org.spockframework.runtime.model.FeatureInfo;
import org.spockframework.runtime.model.SpecInfo;
import annotation.StopWhereAfterErrors;

public class StopWhereAfterErrorsExtension extends AbstractAnnotationDrivenExtension<StopWhereAfterErrors> {
  @Override
  public void visitSpecAnnotation(StopWhereAfterErrors reason, SpecInfo spec) {
    for (FeatureInfo feature : spec.getFeatures()) {
      //Add as the first interceptor
      feature.getFeatureMethod().getInterceptors().add(0, new StopWhereAfterErrorsInterceptor(feature, spec));
    }
  }

  /**
   * Visit the annotated features and add an interceptor.
   *
   * @param reason
   * @param feature
   */
  @Override
  public void visitFeatureAnnotation(StopWhereAfterErrors reason, FeatureInfo feature) {
    //Add as the first interceptor
    feature.getFeatureMethod().getInterceptors().add(0, new StopWhereAfterErrorsInterceptor(feature, null));
  }
}
