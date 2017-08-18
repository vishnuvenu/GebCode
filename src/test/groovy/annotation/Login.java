package annotation;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Login {
  String userCredentials() default "default";
  String username() default "";
  String password() default "";
  String emulateUsername() default "";
  String emulateOfficeid() default "";
  String appBaseURL() default "";
}