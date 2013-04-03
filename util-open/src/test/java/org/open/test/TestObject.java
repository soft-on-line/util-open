package org.open.test;

public class TestObject {

    private String  str;
    private Integer id;

    public String toString() {
        System.out.println(TestObject.class.getName());
        return TestObject.class.getName();
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
