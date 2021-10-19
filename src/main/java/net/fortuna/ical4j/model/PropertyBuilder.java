package net.fortuna.ical4j.model;

import net.fortuna.ical4j.model.property.XProperty;
import net.fortuna.ical4j.util.CompatibilityHints;
import org.apache.commons.codec.DecoderException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

/**
 * Provides a configurable builder for creating {@link Property} instances from {@link String} values.
 *
 * You can specify an arbitrary list of supported property factories, and a list of property names to ignore.
 */
public class PropertyBuilder extends AbstractContentBuilder {

    private final List<PropertyFactory<?>> factories;

    private String name;

    private String value;

    private ParameterList parameters = new ParameterList();

    public PropertyBuilder() {
        this(Collections.emptyList());
    }

    public PropertyBuilder(List<PropertyFactory<? extends Property>> factories) {
        super(CompatibilityHints.isHintEnabled(CompatibilityHints.KEY_RELAXED_PARSING));
        this.factories = factories;
    }

    public PropertyBuilder name(String name) {
        // property names are case-insensitive, but convert to upper case to simplify further processing
        this.name = name.toUpperCase();
        return this;
    }

    public PropertyBuilder value(String value) {
        // remove any trailing whitespace
        this.value = value.trim();
        return this;
    }

    public PropertyBuilder parameter(Parameter parameter) {
        parameters.add(parameter);
        return this;
    }

    public Property build() throws ParseException, IOException, URISyntaxException {
        Property property = null;
        String decodedValue;
        try {
            decodedValue = PropertyCodec.INSTANCE.decode(value);
        } catch (DecoderException e) {
            decodedValue = value;
        }

        for (PropertyFactory<?> factory : factories) {
            if (factory.supports(name)) {
                property = factory.createProperty(parameters, value);
            }
        }

        if (property == null) {
            if (isExperimentalName(name)) {
                property = new XProperty(name, parameters, value);
            } else if (allowIllegalNames()) {
                property = new XProperty(name, parameters, value);
            } else {
                throw new IllegalArgumentException("Illegal property [" + name + "]");
            }
        }

        if (property instanceof Encodable) {
            property.setValue(decodedValue);
        }

        return property;
    }
}
