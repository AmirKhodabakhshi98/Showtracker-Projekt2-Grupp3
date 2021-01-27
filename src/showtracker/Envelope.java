package showtracker;

import java.io.Serializable;

/**
 * @author Filip Spånberg
 * To easily send different information between client and server,
 * it is packaged in an Envelope
 */
public class Envelope implements Serializable {
    private static final long serialVersionUID = 3158624303211464043L;
    private final Object content;
    private final String type;

    public Envelope(Object content, String type) {
        this.content = content;
        this.type = type;
    }

    /**
     * Gets the type of Envelope
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the content of the envelope
     * @return
     */
    public Object getContent() {
        return content;
    }
}