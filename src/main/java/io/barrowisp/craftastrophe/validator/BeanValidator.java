package io.barrowisp.craftastrophe.validator;

import io.barrowisp.craftastrophe.ModLogger;
import io.barrowisp.craftastrophe.common.Defines;
import io.barrowisp.craftastrophe.util.AnnotationUtils;
import io.barrowisp.craftastrophe.util.StringUtils;

import javafx.util.Pair;
import org.apache.logging.log4j.Level;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.annotation.Annotation;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BeanValidator {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final Validator validator = factory.getValidator();

    private static final char REGEX_KEY = '$';

    /**
     * This pair is used to store regex data used to parse annotation violation messages.
     * <ul>
     *     <li>Key - A compiled representation of the parsing regex.</li>
     *     <li>Value - Regex group that contains the value we want to capture.</li>
     * </ul>
     */
    private static final javafx.util.Pair<Pattern, Integer> PARSE_REGEX = new Pair<Pattern, Integer>(
            Pattern.compile(String.format("(?:\\%s)([a-zA-Z0-9_-]*)", REGEX_KEY)), 1);

    /* Make the constructor private to disable instantiation */
    private BeanValidator() {
        throw new UnsupportedOperationException();
    }

    /**
     * @param <T> class of the object being validated
     * @return object being validated <i>(for convenience)</i>
     * @throws javax.validation.UnexpectedTypeException No validator could be found for constraint
     * given to object being validated. This happens when the object data type doesn't match the
     * data type the validator was intended to validate.
     */
    public static <T> T validate(T object) {

        java.util.Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);
        for (ConstraintViolation violation : constraintViolations)
        {
            Object value = violation.getInvalidValue();
            Object field = violation.getPropertyPath();
            String message = violation.getMessage();

            Annotation annotation = violation.getConstraintDescriptor().getAnnotation();
            java.util.Map<String, Object> attributes = AnnotationUtils.getAttributes(annotation);
            Level level = AnnotationUtils.getLogLevel(annotation, Level.ERROR);

            /* Parse our annotation violation message and replace all words marked with the regex key
             * with an annotation attribute value that holds the same name.
             */
            Matcher matcher = PARSE_REGEX.getKey().matcher(message);
            while (matcher.find())
            {
                String group = matcher.group(PARSE_REGEX.getValue());
                Object oReplacement = group.equals("value") ? value : attributes.get(group);
                String sReplacement = StringUtils.smartQuote(oReplacement);
                message = message.replace(REGEX_KEY + group, sReplacement);
            }
            ModLogger.debug("Field \"%s\" has invalid value \"%s\"", field, value);

            if (message.contains("\n")) {
                for (String log : message.split(Defines.NEW_LINE))
                    ModLogger.get().printf(level, log);
            }
            else ModLogger.get().printf(level, message);
        }
        return object;
    }
}
