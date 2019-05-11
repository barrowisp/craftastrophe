package io.barrowisp.craftastrophe.validator;

import io.barrowisp.craftastrophe.ModLogger;
import io.barrowisp.craftastrophe.util.AnnotationUtils;
import io.barrowisp.craftastrophe.util.StringUtils;

import javafx.util.Pair;
import org.apache.logging.log4j.Level;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class BeanValidator {

    private static final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private static final ExecutableValidator exeValidator = factory.getValidator().forExecutables();
    private static final Validator validator = factory.getValidator();

    private static final char REGEX_KEY = '$';

    /**
     * This pair is used to store regex data used to parse annotation violation messages.
     * <ul>
     *     <li>Key - A compiled representation of the parsing regex.</li>
     *     <li>Value - Regex group that contains the value we want to capture.</li>
     * </ul>
     */
    private static final javafx.util.Pair<Pattern, Integer> PARSE_REGEX = new Pair<>(
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

        for (ConstraintViolation violation : validator.validate(object)) {
            processViolation(violation);
        }
        return object;
    }

    /**
     * <p>Create, initialize and <b>validate</b> a new instance of a mod item.</p>
     * A mod item is only recognized if it extends it's parent base class.
     *
     * @param parentClass mod item's parent class
     * @param itemClass mod item's main class
     * @param params constructor initialization parameters
     * @return newly constructed and validated item instance
     * @throws IllegalArgumentException when no item constructor with the
     * supplied parameters could be found
     */
    @SuppressWarnings("unchecked")
    public static <T> T constructItem(Class<? super T> parentClass, Class<T> itemClass, Object...params) {

        /* Bean constraint validation doesn't seem to process parent constructors
         * so we have to manually validate their parameters first
         */
        Constructor<T> constructor = getConstructor(parentClass, params);
        java.util.Set<ConstraintViolation<T>> parentViolations = validateConstructorParams(constructor, params);

        constructor = getConstructor(itemClass, params);
        java.util.Set<ConstraintViolation<T>> childViolations = validateConstructorParams(constructor, params);

        /* In case both child and parent constructor produced contraint violations
         * on the same method parameters we need to filter the child constructor
         * violations to exclude the duplicates so we don't do double prints.
         */
        java.util.Set<Object> violationValues = new java.util.HashSet<>();
        parentViolations.forEach(v -> violationValues.add(v.getInvalidValue()));
        for (ConstraintViolation v : childViolations) {
            if (!violationValues.contains(v.getInvalidValue()))
                parentViolations.add(v);
        }
        /* Both parent and child violations will be processed here
         */
        for (ConstraintViolation violation : parentViolations) {
            processViolation(violation);
        }
        return construct(constructor, params);
    }

    /**
     * This is a helper method to create, initialize and <b>validate</b> a new instance of the
     * constructor's declaring class, with the specified initialization parameters.
     *
     * @param params initialization parameters
     * @return newly constructed and validated object
     */
    private static <T> T construct(Constructor<T> constructor, Object...params) {

        try {
            return validate(constructor.newInstance(params));
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * <p>This method gets called whenever new violatiosn are returned by validators.</p>
     * It will parse the constraint message and print it with the appropriate level.
     */
    private static void processViolation(ConstraintViolation violation) {

        Object value = violation.getInvalidValue();
        Object field = violation.getPropertyPath();
        String message = violation.getMessage();
        Level level = Level.ERROR;

        Annotation annotation = violation.getConstraintDescriptor().getAnnotation();
        if (AnnotationUtils.isCustomModAnnotation(annotation))
        {
            java.util.Map<String, Object> attributes = AnnotationUtils.getAttributes(annotation);
            String sLevel = AnnotationUtils.getAttributeValue(annotation, "level", String.class);
            level = Level.toLevel(sLevel, Level.ERROR);

            /* Parse our annotation violation message and replace all words marked with the regex key
             * with an annotation attribute value that holds the same name.
             */
            Matcher matcher = PARSE_REGEX.getKey().matcher(message);
            while (matcher.find()) {
                String group = matcher.group(PARSE_REGEX.getValue());
                Object oReplacement = group.equals("value") ? value : attributes.get(group);
                String sReplacement = StringUtils.smartQuote(oReplacement);
                message = message.replace(REGEX_KEY + group, sReplacement);
            }
        }
        /* Print the violation message to console with the appropriate level.
         * Also print an exception stack trace as a debug log
         */
        ModLogger.get().printf(level, message);
        ModLogger.debug(message, new Exception(String.format("Field '%s' with value '%s' has violated " +
                "annotation constrains of %s", field, value, annotation.annotationType().getName())));
    }

    /**
     * Find the constructor object for a declared class constructor with the specified parameter list.
     * The order of parameters in the argument array has to match the constructor parameter order.
     *
     * @param clazz Class to get the declared constructor from
     * @param params list of constructor parameters
     */
    private static <T> Constructor getConstructor(Class<T> clazz, Object...params) {

        Class[] constrParams = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            constrParams[i] = params[i].getClass();
        }
        try {
            /* Use #getDeclaredConstructor method to get our constructor
             * instead of #getConstructor in case the constructor is not visible
             */
            return clazz.getDeclaredConstructor(constrParams);
        }
        catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static <T> java.util.Set<ConstraintViolation<T>> validateConstructorParams(Constructor<T> constr, Object...params) {
        return exeValidator.validateConstructorParameters(constr, params);
    }
}
