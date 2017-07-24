package Util;

import org.spockframework.runtime.extension.IMethodInterceptor;
import org.spockframework.runtime.extension.IMethodInvocation;
import org.spockframework.runtime.model.FeatureInfo;
import org.spockframework.runtime.model.SpecInfo;

import java.util.List;

public class StopWhereAfterErrorsInterceptor implements IMethodInterceptor{
  private FeatureInfo feature;
  private SpecInfo spec;
  private boolean isTerminated;

  public StopWhereAfterErrorsInterceptor(FeatureInfo aFeature, SpecInfo aSpec) {
    feature = aFeature;
    spec = aSpec;
    isTerminated = false;
  }

  public void intercept(IMethodInvocation invocation) throws Throwable {
    try {
      if (!isTerminated){
        invocation.proceed();
      }
    } catch (Throwable t) {
      isTerminated = true;
      if (spec != null) {
        ignoreRemainingFeatures();
      }
      throw t;
    }
  }

  private void ignoreRemainingFeatures() {
    List<FeatureInfo> features = spec.getFeatures();
    for (int i = features.indexOf(feature); i < features.size(); i++) {
      features.get(i).setSkipped(true);
    }
  }
}
