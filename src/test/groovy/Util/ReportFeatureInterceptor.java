package Util;

import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.spockframework.runtime.extension.IMethodInterceptor;
import org.spockframework.runtime.extension.IMethodInvocation;

/**
 * Interceptor for feature methods. Does a report before and after each feature methods. If an error occurs, does a report
 * on failure.
 *
 * @author p. hilt
 */
public class ReportFeatureInterceptor implements IMethodInterceptor {

  public void intercept(IMethodInvocation invocation) throws Throwable {
    Boolean takeScreenshots = System.getProperty("takeScreenshots", "true").equals("true");
    String name = invocation.getFeature().getName();
    if (takeScreenshots) {
      DefaultGroovyMethods.invokeMethod(invocation.getSharedInstance(), "report", "Before " + name);
    }
    try {
      invocation.proceed();
    } catch (Throwable t) {
      DefaultGroovyMethods.invokeMethod(invocation.getSharedInstance(), "report", "On failure " + name);
      throw t;
    }
    if (takeScreenshots) {
      DefaultGroovyMethods.invokeMethod(invocation.getSharedInstance(), "report", "After " + name);
    }
  }
}
