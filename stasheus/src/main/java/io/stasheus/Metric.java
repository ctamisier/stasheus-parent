package io.stasheus;

public class Metric {

    private String key;

    private Object value;

    public Metric(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public static Metric fromLine(String inputLine) {
        int lastSpace = inputLine.lastIndexOf(" ");
        String key = inputLine.substring(0, lastSpace);
        String valueString = inputLine.substring(lastSpace + 1);

        Object value;
        try {
            value = Float.parseFloat(valueString);
        } catch (NumberFormatException nfe) {
            value = valueString;
        }
        return new Metric(key, value);
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

}
