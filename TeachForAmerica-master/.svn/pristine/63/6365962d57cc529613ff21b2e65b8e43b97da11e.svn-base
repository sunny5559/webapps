package org.tfa.mtld.web.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * annotation interface Validators where application developer can add
 * validators. ValidatorAspect class validate method is called whenever
 * Validators annotation is encountered at controller layer.
 * 
 * @author arun.rathore
 */

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Validators {

	/**
	 * The validators that should be run before the service method is invoked.
	 */
	String[] validators() default {};

}
