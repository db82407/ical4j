/**
 * Copyright (c) 2012, Ben Fortuna
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  o Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 *  o Neither the name of Ben Fortuna nor the names of any other contributors
 * may be used to endorse or promote products derived from this software
 * without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.fortuna.ical4j.transform;

import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.CalendarComponent;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.transform.property.MethodUpdate;
import net.fortuna.ical4j.transform.property.SequenceIncrement;

/**
 * $Id$
 *
 * Created: 26/09/2004
 *
 * Transforms a calendar for METHOD=REQUEST.
 * @author benfortuna
 */
public class RequestTransformer implements Transformer<Calendar> {

    /**
     * {@inheritDoc}
     */
    public final Calendar transform(final Calendar calendar) {
        PropertyList calProps = calendar.getProperties();

        MethodUpdate methodUpdate = new MethodUpdate(Method.REQUEST);
        methodUpdate.transform(calendar);

        SequenceIncrement sequenceIncrement = new SequenceIncrement();

        Property uid = null;

        // if a calendar component has already been published previously
        // update the sequence number..
        for (CalendarComponent component : calendar.getComponents()) {
            if (uid == null) {
                uid = component.getProperty(Property.UID);
            } else if (!uid.equals(component.getProperty(Property.UID))) {
                throw new IllegalArgumentException("All components must share the same non-null UID");
            }
            sequenceIncrement.transform(component);
        }
        return calendar;
    }
}
