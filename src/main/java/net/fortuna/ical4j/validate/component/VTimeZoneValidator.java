package net.fortuna.ical4j.validate.component;

import net.fortuna.ical4j.model.component.Observance;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.validate.ComponentValidator;
import net.fortuna.ical4j.validate.ValidationException;
import net.fortuna.ical4j.validate.Validator;

public class VTimeZoneValidator implements Validator<VTimeZone> {

    @Override
    public void validate(VTimeZone target) throws ValidationException {
        ComponentValidator.VTIMEZONE.validate(target);

        /*
         * ; one of 'standardc' or 'daylightc' MUST occur ..; and each MAY occur more than once. standardc / daylightc /
         */
        if (!target.getComponent(Observance.STANDARD).isPresent()
                && !target.getComponent(Observance.DAYLIGHT).isPresent()) {
            throw new ValidationException("Sub-components ["
                    + Observance.STANDARD + "," + Observance.DAYLIGHT
                    + "] must be specified at least once");
        }
        target.getObservances().getAll().forEach(ComponentValidator.OBSERVANCE_ITIP::validate);
    }
}
