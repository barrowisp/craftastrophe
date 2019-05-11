package io.barrowisp.craftastrophe.defines;

import io.barrowisp.craftastrophe.validator.RegistryNameValidator;
import org.apache.logging.log4j.spi.StandardLevel;

import javax.annotation.meta.TypeQualifierNickname;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.regex.Pattern;

/**
 * <p>Forge registry identifiers are used to create resource location objects that get stored
 * in each Forge registry entry when we register game objects such as items, blocks, recipes etc.</p>
 * To ensure compatibility and uniformity these names need to conform to Forge naming standards:</p>
 * <ul>
 *     <li>Identifiers should <b>ALWAYS</b> represent a valid resource location,
 *     containing both the resource domain and path separated by a semicolon.</li>
 *     <li>Identifiers should <b>NOT</b> contain whitespaces.</li>
 *     <li>Identifiers <i>can</i> contain special characters such as underscores(<code>_</code>),
 *     periods(<code>.</code>) and dashes(<code>-</code>) as well as forward slashes (<code>/</code>).</li>
 *     <li>Forward slashes are only allowed as part of the resource location path.</li>
 *     <li>Use of other special characters not listed above is <b>NOT</b> allowed</li>
 *     <li>All characters <b>HAVE</b> to be {@code lower-case}.</li>
 * </ul>
 * In addition to these rules a Forge identifier used to register game objects <i>should</i>
 * be formatted according to the following set of conventions for better readibility and easier maintenance:
 * <ul>
 *      <li>Identifier domain and path should always start with an alphabet letter.</b></li>
 *      <li>Permitted special characters can only be used to separate words.</li>
 *      <li>Numbers should only be used as a domain or path segment suffix.</li>
 *      <li>However numbers <i>can</i> be used in word segments that were separated by special characters.</li>
 * </ul>
 * <p>Here are some examples of valid registry names:</p>
 * <ul style="list-style-type:none">
 *     <li>{@code example-mod:validblock}</li>
 *     <li>{@code example.mod:valid_block.3/item1}</li>
 * </ul>
 * <p>These are highly unusual but still conventional:</p>
 * <ul style="list-style-type:none">
 *     <li>{@code my-mod1:item2-ab31-c/test}</li>
 *     <li>{@code my-m0d1:item20-ab3a1/my.test}</li>
 * </ul>
 * <p>These however are breaking naming standards:</p>
 * <ul style="list-style-type:none">
 *     <li>{@code name_of_my_item_or_block}</li>
 *     <li>{@code example-mod:invalid block}</li>
 *     <li>{@code example-mod:invalid$block}</li>
 * </ul>
 * <p>And these are breaking naming conventions:</p>
 * <ul style="list-style-type:none">
 *     <li>{@code example-mod:0invalid_block}</li>
 *     <li>{@code 1example2:invalid--block//}</li>
 *     <li>{@code 11exmod:/.-/}</li>
 * </ul>
 *
 * @see net.minecraftforge.registries.IForgeRegistryEntry
 * @see net.minecraft.util.ResourceLocation
 */
@Documented
@CustomModAnnotation
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.CONSTRUCTOR })
@Constraint(validatedBy = { RegistryNameValidator.class })
@SuppressWarnings("unused")
public @interface ForgeRegIdentifier {

    String level() default "ERROR";
    String message() default RULE_VIOLATION;

    String RULE_VIOLATION = "Found malformed registry name $value.";
    String CONVENTION_VIOLATION = "Found unconventional registry name $value.";
    String SECONDARY_LOG = "Check @ForgeRegIdentifier documentation for more information";

    /**
     * <p>This is a regular expression used to validate registry names.</p>
     * It's a mod specific naming convention so it will just throw warnings.
     */
    Pattern CONVENTION_REGEX = Pattern.compile("^([a-z]+(?:[0-9]*[-_.][a-z0-9]+)*[0-9]*)(\\:)" +
            "((?:[a-z]+(?:[a-z]*[0-9]*[-_.][a-z0-9]+)*(?:[a-z0-9]*[\\/][a-z0-9]+)?)+[0-9]*)$");

    /**
     * <p>This is a regular expression used to validate registry names.</p>
     * It's a Minecraft naming rule for resource locations so it will throw errors.
     */
    Pattern RULE_REGEX = Pattern.compile("^[a-z0-9-_.]+(:)[a-z0-9-_.\\/]+$");

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
