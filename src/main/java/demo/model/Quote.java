package demo.model;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * class containing a String value of a quote identified by a unique int id
 */
@XmlRootElement
public class Quote {

    @JsonbProperty("id")
    private int id;

    @JsonbProperty("quote")
    private String quote;

    public Quote(String quote, int id) {
        this.quote = quote;
        this.id = id;
    }

    public Quote() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public int getID() {
        return id;
    }

    public String getQuote() {
        return quote;
    }

    @Override
    public String toString() {
        return "{\n" +
                "\tid: " + id +
                "\n\tquote: " + quote +
                "\n}";
    }

}
