package annotation;

import org.spockframework.runtime.extension.ExtensionAnnotation;
import Util.ReportExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author p. hilt
 */

@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.TYPE} )
@ExtensionAnnotation(ReportExtension.class)
public @interface Report {
}