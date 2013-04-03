package org.open.util;

import java.io.Serializable;

public class TestObject implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2036672441088815465L;
    private Integer           obj_int;
    private String            obj_string;

    public Integer getObj_int() {
        return obj_int;
    }

    public void setObj_int(Integer objInt) {
        obj_int = objInt;
    }

    public String getObj_string() {
        return obj_string;
    }

    public void setObj_string(String objString) {
        obj_string = objString;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TestObject [obj_int=");
        builder.append(obj_int);
        builder.append(", obj_string=");
        builder.append(obj_string);
        builder.append("]");
        return builder.toString();
    }
}
