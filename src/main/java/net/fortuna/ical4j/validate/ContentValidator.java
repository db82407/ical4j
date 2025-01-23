package net.fortuna.ical4j.validate;

import net.fortuna.ical4j.model.Content;

import java.util.List;

public interface ContentValidator {

    String ASSERT_NONE_MESSAGE = "Content [{0}] is not applicable";

    String ASSERT_ONE_OR_LESS_MESSAGE = "Content [{0}] must only be specified once";

    String ASSERT_ONE_MESSAGE = "Content [{0}] must be specified once";

    String ASSERT_ONE_OR_MORE_MESSAGE = "Content [{0}] must be specified at least once";

    static <T extends Content> void assertNone(final String name, final List<T> content, boolean warn) throws ValidationException {
        Validator.assertFalse(input -> input.parallelStream().anyMatch(c -> c.getName().equals(name)),
                ASSERT_NONE_MESSAGE, warn, content, name);
    }

    static <T extends Content> void assertOne(final String name, final List<T> content, boolean warn) throws ValidationException {
        Validator.assertFalse(input -> input.stream().filter(c -> c.getName().equalsIgnoreCase(name)).count() != 1,
                ASSERT_ONE_MESSAGE, warn, content, name);
    }

    static <T extends Content> void assertOneOrLess(final String name, final List<T> content, boolean warn) throws ValidationException {
        Validator.assertFalse(input -> input.stream().filter(c -> c.getName().equalsIgnoreCase(name)).count() > 1,
                ASSERT_ONE_OR_LESS_MESSAGE, warn, content, name);
    }

    static <T extends Content> void assertOneOrMore(final String name, final List<T> content, boolean warn) throws ValidationException {
        Validator.assertFalse(input -> input.stream().noneMatch(c -> c.getName().equalsIgnoreCase(name)),
                ASSERT_ONE_OR_MORE_MESSAGE, warn, content, name);
    }
}
