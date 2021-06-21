package com.balsam.test1.domain.enumeration;

/**
 * The EmployeeSize enumeration.
 */
public enum EmployeeSize {
    LESSTHAN10("&lt;10"),
    BETWEEN10AND50("10-50"),
    BETWEEN50AND100("50-100"),
    BETWEEN500AND1000("500-1000"),
    LARGERTHANT1000("&gt;1000");

    private final String value;

    EmployeeSize(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
