package io.barrowisp.craftastrophe.util;

import io.barrowisp.craftastrophe.ModLogger;
import io.barrowisp.craftastrophe.defines.CustomModAnnotation;
import io.barrowisp.craftastrophe.defines.MethodsNotNull;
import org.jetbrains.annotations.Nullable;

import javax.validation.constraints.NotEmpty;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@MethodsNotNull
public class AnnotationUtils {

    /**
     * <p>Retrieve the given annotation's attributes as a Map, preserving all attribute types.</p>
     * Same can be achieved by using Spring {@code AnnotationUtils} class methods, but for the sake
     * of simplicity this manual approach will do just fine.
     *
     * @return {@code empty} map if no attributes were found.
     */
    public static java.util.Map<String, Object> getAttributes(Annotation annotation) {

        /* Contrary to what one might expect, the elements of an annotation are not attributes,
         * they are actually methods that return the provided value or a default value.
         *
         * Here we are gonna iterate through the annotations' methods and invoke them to get the values.
         * Use annotationType() to get the annotation's class, the object returned by getClass() is just a proxy.
         */
        java.util.Map<String, Object> attributes = new java.util.HashMap<>();
        for (Method method : annotation.annotationType().getDeclaredMethods()) {
            try {
                attributes.put(method.getName(), method.invoke(annotation, (Object[]) null));
            } catch (IllegalAccessException | InvocationTargetException e) {
                ModLogger.error("Unable to get attributes for annotation " + annotation.getClass().getSimpleName(), e);
            }
        }
        return attributes;
    }

    /**
     * <p>Get annotation's method instance and return value in a pair.</p>
     * Since the user can get the value from method instance himself this is
     * done purely for convenience purposes.
     *
     * @param annotation instance of the annotation to get the method from
     * @param attribute complete name <i>(without brackets)</i> of the method to search for
     * @return {@code null} if we were unable to resolve the method
     */
    public static @Nullable javafx.util.Pair<Method, Object> getAttribute(Annotation annotation, @NotEmpty String attribute) {

        try {
            Method object = annotation.annotationType().getDeclaredMethod(attribute);
            return new javafx.util.Pair<>(object, object.invoke(annotation, (Object[]) null));
        }
        catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            String className = annotation.getClass().getSimpleName();
            ModLogger.error("Unable to get method for annotation " + className, e);
            return null;
        }
    }
    @SuppressWarnings("unchecked")
    public static <T> T getAttributeValue(Annotation annotation, String attribute, Class<T> clazz) {

        javafx.util.Pair<Method, Object> data = getAttribute(annotation, attribute);
        Object value = data != null ? data.getValue() : null;
        if (!clazz.isInstance(value)) {
            throw new IllegalStateException(String.format("Expected to find attribute %s(%s) in annotation %s",
                    attribute, clazz.getSimpleName(), annotation.getClass().getSimpleName()));
        }
        return (T) value;
    }

    public static boolean isCustomModAnnotation(Annotation annotation) {
        return annotation.annotationType().getDeclaredAnnotation(CustomModAnnotation.class) != null;
    }

    /**
     * Changes the annotation value for the given key of the given annotation
     * to newValue and return the previous value.
     *
     * @author Balder@stackoverflow
     * @see <a href="https://stackoverflow.com/a/28118436/7803527">Origin code on Stackoverflow</a>
     */
    @SuppressWarnings({"unchecked", "UnusedReturnValue"})
    public static Object changeAnnotationValue(Annotation annotation, String key, Object newValue) {
        Object handler = Proxy.getInvocationHandler(annotation);
        Field f;
        try {
            f = handler.getClass().getDeclaredField("memberValues");
        } catch (NoSuchFieldException | SecurityException e) {
            throw new IllegalStateException(e);
        }
        f.setAccessible(true);
        java.util.Map<String, Object> memberValues;
        try {
            memberValues = (java.util.Map<String, Object>) f.get(handler);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        Object oldValue = memberValues.get(key);
        if (oldValue == null || oldValue.getClass() != newValue.getClass()) {
            throw new IllegalArgumentException();
        }
        memberValues.put(key, newValue);
        return oldValue;
    }
}
