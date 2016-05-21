package com.business.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class MarkerFilter extends AbstractMatcherFilter<ILoggingEvent> {

    private Marker marker;

    @Override
    public FilterReply decide(ILoggingEvent event) {
        FilterReply reply;

        if (marker == null) {
            if (event.getMarker() == null) {
                reply = onMatch;
            } else {
                reply = onMismatch;
            }
        } else {
            if (event.getMarker() != null && marker.contains(event.getMarker())) {
                reply = onMatch;
            } else {
                reply = onMismatch;
            }
        }

        return reply;
    }

    public final void setMarker(String marker) {
        this.marker = MarkerFactory.getMarker(marker);
    }

}
