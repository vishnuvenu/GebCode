package Util;

import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.spockframework.runtime.extension.IMethodInterceptor;
import org.spockframework.runtime.extension.IMethodInvocation;

/**
 * Interceptor for fixture methods (setup, setupSpec, cleanup, cleanupSpec). Does a report before setup or setupSpec,
 * does a report after cleanup or cleanupSpec. If an error occurs, does s report on failure.
 *
 * @author p. hilt
 */
public class ReportFixtureInterceptor implements IMethodInterceptor {

  public void intercept(IMethodInvocation invocation) throws Throwable {
    String name = invocation.getMethod().getName();
    String specName = invocation.getSpec().getName();
    if(!specName.equals("GebSpec") && !specName.equals("SECOSpec")) {
      Boolean takeScreenshots = System.getProperty("takeScreenshots", "true").equals("true");
      if(name.equals("setupSpec") ) {
        if (takeScreenshots) {
          DefaultGroovyMethods.invokeMethod(invocation.getSharedInstance(), "report", " Before setupSpec " + specName);
        }
        try {
          invocation.proceed();
        } catch (Throwable t) {
          DefaultGroovyMethods.invokeMethod(invocation.getSharedInstance(), "report", " On failure setupSpec " + specName);
          throw t;
        }
      } else if (name.equals("cleanupSpec")) {
        try {
          invocation.proceed();
        } catch (Throwable t) {
          DefaultGroovyMethods.invokeMethod(invocation.getSharedInstance(), "report", " On failure cleanupSpec " + specName);
          throw t;
        }
        if (takeScreenshots) {
          DefaultGroovyMethods.invokeMethod(invocation.getSharedInstance(), "report", " After cleanupSpec " + specName);
        }
      } else {
        try {
          invocation.proceed();
        } catch (Throwable t) {
          DefaultGroovyMethods.invokeMethod(invocation.getSharedInstance(), "report", " On failure " + specName);
          throw t;
        }
      }
    } else {
      try {
        invocation.proceed();
      } catch (Throwable t) {
        DefaultGroovyMethods.invokeMethod(invocation.getSharedInstance(), "report", " On failure " + name + " " + specName);
        throw t;
      }
    }
  }
}
