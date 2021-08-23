package ru.mts.teta.annotation;

import ru.mts.teta.validator.TitleCaseChecker;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = TitleCaseChecker.class)
public @interface TitleCase {

    TitleLanguage lang() default TitleLanguage.ANY;

    String message() default "Заголовок курса написан с ошибками";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
