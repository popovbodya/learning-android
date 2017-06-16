package ru.popov.bodya.eventsmanager;


import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class EventTest {


    @Test
    public void serializabilityCheck() throws IOException, ClassNotFoundException {

        Event expectedEvent = new Event();
        expectedEvent.setTitle("expectedEvent title");
        expectedEvent.setDescription("expectedEvent description");
        expectedEvent.setDateStart(String.valueOf(1497640920918L));
        expectedEvent.setDateEnd(String.valueOf(1497642320999L));

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(expectedEvent);
        out.flush();

        byte[] eventBytes = bos.toByteArray();
        bos.close();

        ByteArrayInputStream bis = new ByteArrayInputStream(eventBytes);
        ObjectInputStream in = new ObjectInputStream(bis);
        Event actualEvent = (Event) in.readObject();
        in.close();

        assertThat(actualEvent, is(actualEvent));

    }
}
