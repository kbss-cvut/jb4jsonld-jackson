package cz.cvut.kbss.jsonld.jackson.serialization;

public class SerializationConstants {

    /**
     * Configuration parameter indicating in what form the serialization output should be.
     */
    public static final String FORM = "form";

    /**
     * Compact form of the JSON-LD document.
     *
     * This form is without context and uses full IRIs of mapped properties as terms in the resulting JSON-LD object.
     *
     * Used as a value of the {@link #FORM} configuration parameter.
     */
    public static final String FORM_COMPACT = "compact";

    /**
     * Compact form with context.
     *
     * This form creates a context where field names are used as terms in the resulting JSON-LD object and are mapped
     * to IRIs (or more complex term definitions) in the context.
     *
     * Used as a value of the {@link #FORM} configuration parameter.
     */
    public static final String FORM_COMPACT_WITH_CONTEXT = "compact-with-context";

    private SerializationConstants() {
        throw new AssertionError();
    }
}
