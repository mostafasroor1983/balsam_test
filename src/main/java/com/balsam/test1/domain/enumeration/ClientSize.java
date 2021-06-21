package com.balsam.test1.domain.enumeration;

/**
 * The ClientSize enumeration.
 */
public enum ClientSize {
    LESSTHAN1000("&lt;1000"),
    BETWEEN1000AND5000("1000-5000"),
    BETWEEN5000AND10000("5000-10000"),
    BETWEEN10000AND50000("10000-50000"),
    BETWEEN50000AND1000000("50000-1000000"),
    LARGERTHANT100000("&gt;100000");

    private final String value;

    ClientSize(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
