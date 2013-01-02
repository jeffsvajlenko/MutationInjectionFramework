package org.apache.commons.attributes;

/**
 * Attribute indicating what elements an attribute may be applied to.
 * This is checked at runtime. If the attribute is absent, it defaults
 * to {@link Target#ALL Target.ALL}.
 *
 * <p>This attribute is intended to be used with attribute classes:
 *
 * <pre><code>
 * &#x2f;**
 *  * MyAttribute can only be applied to classes and fields, not methods.
 *  * &#x40;&#x40;Target(Target.CLASS | Target.FIELD)
 *  *&#x2f;
 * public class MyAttribute { ... }
 * </code></pre>
 *
 * @since 2.1
 */
public class Target
{

    /**
     * Indicates that the attribute can be applied to a class or interface.
     *
     * @since 2.1
     */
    public static final int CLASS = 1;

    /**
     * Indicates that the attribute can be applied to a field.
     *
     * @since 2.1
     */
    public static final int FIELD = 2;

    /**
     * Indicates that the attribute can be applied to a method.
     *
     * @since 2.1
     */
    public static final int METHOD = 4;

    /**
     * Indicates that the attribute can be applied to a constructor.
     *
     * @since 2.1
     */
    public static final int CONSTRUCTOR = 8;

    /**
     * Indicates that the attribute can be applied to a method parameter.
     *
     * @since 2.1
     */
    public static final int METHOD_PARAMETER = 16;

    /**
     * Indicates that the attribute can be applied to a constructor parameter.
     *
     * @since 2.1
     */
    public static final int CONSTRUCTOR_PARAMETER = 32;

    /**
     * Indicates that the attribute can be applied to a method return value.
     *
     * @since 2.1
     */
    public static final int RETURN = 64;

    /**
     * Indicates that the attribute can be applied to a parameter of a method or a constructor.
     * It is equal to <code>METHOD_PARAMETER | CONSTRUCTOR_PARAMETER</code>.
     *
     * @since 2.1
     */
    public static final int PARAMETER = METHOD_PARAMETER | CONSTRUCTOR_PARAMETER;

    /**
     * Indicates that the attribute can be applied to any program element.
     *
     * @since 2.1
     */
    public static final int ALL = CLASS | FIELD | METHOD | CONSTRUCTOR | PARAMETER | RETURN;

    private final int flags;

    /**
     * Creates a new target attribute.
     *
     * @param flags a bitwise or of flags indicating the allowed targets.
     * @since 2.1
     */
    public Target (int flags)
    {
        this.flags = flags;
    }

    /**
     * Returns an <code>int</code> that is the bitwise or of the allowed target flags.
     *
     * @since 2.1
     */
    public int getFlags ()
    {
        return flags;
    }

}
