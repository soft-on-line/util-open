package org.open.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.open.Message;

public class MessageLogAppender extends AppenderSkeleton {

    public MessageLogAppender() {
    }

    protected void append(LoggingEvent event) {
        if (null == this.layout) {
            Message.instance.in(event.toString());
        } else {
            Message.instance.in(this.layout.format(event));
        }
    }

    public void close() {
        this.closed = true;
    }

    public boolean requiresLayout() {
        return true;
    }
}
