package org.n52.testbed.routing.persistence;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Target({FIELD, METHOD, PARAMETER, TYPE, ANNOTATION_TYPE})
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Qualifier("mongo")
@Component
public @interface MongoConverter {
}
