package org.open.util;

class InnerObject {

    private Integer    id;
    private String     testString;
    private int        testInt;

    private static int count = 0;

    public InnerObject() {
        testInt = (++count);
        id = testInt;
        testString = "s_" + testInt;
    }

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }

    public int getTestInt() {
        return testInt;
    }

    public void setTestInt(int testInt) {
        this.testInt = testInt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
