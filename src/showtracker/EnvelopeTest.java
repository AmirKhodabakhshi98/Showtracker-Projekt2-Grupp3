package showtracker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnvelopeTest {

    @Test
    void getType() {
        Envelope envelope = new Envelope("Test", "Series");
        assertEquals("Series", envelope.getType());
    }

    @Test
    void getContent() {
        Envelope envelope = new Envelope("Test", "Series");
        assertEquals("Test", envelope.getContent());
    }

    @Test
    void getTypeWhenNull(){
        Envelope envelope = new Envelope(null, null);
        assertEquals(null, envelope.getType());
    }

    @Test
    void getContentWhenNull(){
        Envelope envelope = new Envelope(null, null);
        assertEquals(null, envelope.getContent());
    }

}