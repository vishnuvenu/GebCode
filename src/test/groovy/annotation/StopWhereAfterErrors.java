package annotation;

import org.spockframework.runtime.extension.ExtensionAnnotation;
import Util.StopWhereAfterErrorsExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@ExtensionAnnotation(StopWhereAfterErrorsExtension.class)
public @interface StopWhereAfterErrors {
}
