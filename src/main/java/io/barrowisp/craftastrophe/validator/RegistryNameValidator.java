package io.barrowisp.craftastrophe.validator;

import io.barrowisp.craftastrophe.defines.ForgeRegIdentifier;
import io.barrowisp.craftastrophe.util.AnnotationUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RegistryNameValidator implements ConstraintValidator<ForgeRegIdentifier, String> {

    private ForgeRegIdentifier constraint;

    @Override
    public void initialize(ForgeRegIdentifier constraint) {
        this.constraint = constraint;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext ctx) {

        String message = ForgeRegIdentifier.RULE_VIOLATION;

        if (ForgeRegIdentifier.RULE_REGEX.matcher(value).find()) {
            if (ForgeRegIdentifier.CONVENTION_REGEX.matcher(value).find()) {
                return true;
            }
            else {
                /* Registry name is valid according to Minecraft standards but doesn't conform
                 * to mod specific naming conventions so we're gonna just throw a warning here
                 */
                AnnotationUtils.changeAnnotationValue(constraint, "level", "WARN");
                message = ForgeRegIdentifier.CONVENTION_VIOLATION;
            }
        }
        /* Disable current violation and build a new one with a different output
         * message depending on if the rule or convention was broken
         */
        ctx.disableDefaultConstraintViolation();
        message += " " + ForgeRegIdentifier.SECONDARY_LOG;
        ctx.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        return false;
    }
}
