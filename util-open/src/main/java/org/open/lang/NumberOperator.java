package org.open.lang;

/**
 * Number工具类
 * 
 * @author Qin Zhipeng
 * @date 2010-09-21 10:10
 */
public strictfp class NumberOperator {

    public static Number add(Number a, Number b) {
        if (a instanceof Byte) {
            return NumberOperator.add(a.byteValue(), b);
        } else if (a instanceof Short) {
            return NumberOperator.add(a.shortValue(), b);
        } else if (a instanceof Integer) {
            return NumberOperator.add(a.intValue(), b);
        } else if (a instanceof Long) {
            return NumberOperator.add(a.longValue(), b);
        } else if (a instanceof Float) {
            return NumberOperator.add(a.floatValue(), b);
        } else if (a instanceof Double) {
            return NumberOperator.add(a.doubleValue(), b);
        } else {
            throw new IllegalArgumentException("Number [" + a + "] can't add Number [" + b + "]");
        }
    }

    public static Number add(byte a, Number b) {
        if (b instanceof Byte) {
            return a + (Byte) b;
        } else if (b instanceof Short) {
            return a + (Short) b;
        } else if (b instanceof Integer) {
            return a + (Integer) b;
        } else if (b instanceof Long) {
            return a + (Long) b;
        } else if (b instanceof Float) {
            return a + (Float) b;
        } else if (b instanceof Double) {
            return a + (Double) b;
        } else {
            throw new IllegalArgumentException("Number [" + b + "] can't add [" + a + "]");
        }
    }

    public static Number add(short a, Number b) {
        if (b instanceof Byte) {
            return a + (Byte) b;
        } else if (b instanceof Short) {
            return a + (Short) b;
        } else if (b instanceof Integer) {
            return a + (Integer) b;
        } else if (b instanceof Long) {
            return a + (Long) b;
        } else if (b instanceof Float) {
            return a + (Float) b;
        } else if (b instanceof Double) {
            return a + (Double) b;
        } else {
            throw new IllegalArgumentException("Number [" + b + "] can't add [" + a + "]");
        }
    }

    public static Number add(int a, Number b) {
        if (b instanceof Byte) {
            return a + (Byte) b;
        } else if (b instanceof Short) {
            return a + (Short) b;
        } else if (b instanceof Integer) {
            return a + (Integer) b;
        } else if (b instanceof Long) {
            return a + (Long) b;
        } else if (b instanceof Float) {
            return a + (Float) b;
        } else if (b instanceof Double) {
            return a + (Double) b;
        } else {
            throw new IllegalArgumentException("Number [" + b + "] can't add [" + a + "]");
        }
    }

    public static Number add(long a, Number b) {
        if (b instanceof Byte) {
            return a + (Byte) b;
        } else if (b instanceof Short) {
            return a + (Short) b;
        } else if (b instanceof Integer) {
            return a + (Integer) b;
        } else if (b instanceof Long) {
            return a + (Long) b;
        } else if (b instanceof Float) {
            return a + (Float) b;
        } else if (b instanceof Double) {
            return a + (Double) b;
        } else {
            throw new IllegalArgumentException("Number [" + b + "] can't add [" + a + "]");
        }
    }

    public static Number add(float a, Number b) {
        if (b instanceof Byte) {
            return a + (Byte) b;
        } else if (b instanceof Short) {
            return a + (Short) b;
        } else if (b instanceof Integer) {
            return a + (Integer) b;
        } else if (b instanceof Long) {
            return a + (Long) b;
        } else if (b instanceof Float) {
            return a + (Float) b;
        } else if (b instanceof Double) {
            return a + (Double) b;
        } else {
            throw new IllegalArgumentException("Number [" + b + "] can't add [" + a + "]");
        }
    }

    public static Number add(double a, Number b) {
        if (b instanceof Byte) {
            return a + (Byte) b;
        } else if (b instanceof Short) {
            return a + (Short) b;
        } else if (b instanceof Integer) {
            return a + (Integer) b;
        } else if (b instanceof Long) {
            return a + (Long) b;
        } else if (b instanceof Float) {
            return a + (Float) b;
        } else if (b instanceof Double) {
            return a + (Double) b;
        } else {
            throw new IllegalArgumentException("Number [" + b + "] can't add [" + a + "]");
        }
    }

    public static Number divide(Number a, Number b) {
        if (a instanceof Byte) {
            return NumberOperator.divide(a.byteValue(), b);
        } else if (a instanceof Short) {
            return NumberOperator.divide(a.shortValue(), b);
        } else if (a instanceof Integer) {
            return NumberOperator.divide(a.intValue(), b);
        } else if (a instanceof Long) {
            return NumberOperator.divide(a.longValue(), b);
        } else if (a instanceof Float) {
            return NumberOperator.divide(a.floatValue(), b);
        } else if (a instanceof Double) {
            return NumberOperator.divide(a.doubleValue(), b);
        } else {
            throw new IllegalArgumentException("Number [" + a + "] can't divide Number [" + b + "]");
        }
    }

    public static Number divide(byte a, Number b) {
        if (b instanceof Byte) {
            return a / (Byte) b;
        } else if (b instanceof Short) {
            return a / (Short) b;
        } else if (b instanceof Integer) {
            return a / (Integer) b;
        } else if (b instanceof Long) {
            return a / (Long) b;
        } else if (b instanceof Float) {
            return a / (Float) b;
        } else if (b instanceof Double) {
            return a / (Double) b;
        } else {
            throw new IllegalArgumentException("Number [" + b + "] can't divide [" + a + "]");
        }
    }

    public static Number divide(short a, Number b) {
        if (b instanceof Byte) {
            return a / (Byte) b;
        } else if (b instanceof Short) {
            return a / (Short) b;
        } else if (b instanceof Integer) {
            return a / (Integer) b;
        } else if (b instanceof Long) {
            return a / (Long) b;
        } else if (b instanceof Float) {
            return a / (Float) b;
        } else if (b instanceof Double) {
            return a / (Double) b;
        } else {
            throw new IllegalArgumentException("Number [" + b + "] can't divide [" + a + "]");
        }
    }

    public static Number divide(int a, Number b) {
        if (b instanceof Byte) {
            return a / (Byte) b;
        } else if (b instanceof Short) {
            return a / (Short) b;
        } else if (b instanceof Integer) {
            return a / (Integer) b;
        } else if (b instanceof Long) {
            return a / (Long) b;
        } else if (b instanceof Float) {
            return a / (Float) b;
        } else if (b instanceof Double) {
            return a / (Double) b;
        } else {
            throw new IllegalArgumentException("Number [" + b + "] can't divide [" + a + "]");
        }
    }

    public static Number divide(long a, Number b) {
        if (b instanceof Byte) {
            return a / (Byte) b;
        } else if (b instanceof Short) {
            return a / (Short) b;
        } else if (b instanceof Integer) {
            return a / (Integer) b;
        } else if (b instanceof Long) {
            return a / (Long) b;
        } else if (b instanceof Float) {
            return a / (Float) b;
        } else if (b instanceof Double) {
            return a / (Double) b;
        } else {
            throw new IllegalArgumentException("Number [" + b + "] can't divide [" + a + "]");
        }
    }

    public static Number divide(float a, Number b) {
        if (b instanceof Byte) {
            return a / (Byte) b;
        } else if (b instanceof Short) {
            return a / (Short) b;
        } else if (b instanceof Integer) {
            return a / (Integer) b;
        } else if (b instanceof Long) {
            return a / (Long) b;
        } else if (b instanceof Float) {
            return a / (Float) b;
        } else if (b instanceof Double) {
            return a / (Double) b;
        } else {
            throw new IllegalArgumentException("Number [" + b + "] can't divide [" + a + "]");
        }
    }

    public static Number divide(double a, Number b) {
        if (b instanceof Byte) {
            return a / (Byte) b;
        } else if (b instanceof Short) {
            return a / (Short) b;
        } else if (b instanceof Integer) {
            return a / (Integer) b;
        } else if (b instanceof Long) {
            return a / (Long) b;
        } else if (b instanceof Float) {
            return a / (Float) b;
        } else if (b instanceof Double) {
            return a / (Double) b;
        } else {
            throw new IllegalArgumentException("Number [" + b + "] can't divide [" + a + "]");
        }
    }

    public static Number min(Number a, Number b) {
        if (a instanceof Byte) {
            return NumberOperator.min(a.byteValue(), b);
        } else if (a instanceof Short) {
            return NumberOperator.min(a.shortValue(), b);
        } else if (a instanceof Integer) {
            return NumberOperator.min(a.intValue(), b);
        } else if (a instanceof Long) {
            return NumberOperator.min(a.longValue(), b);
        } else if (a instanceof Float) {
            return NumberOperator.min(a.floatValue(), b);
        } else if (a instanceof Double) {
            return NumberOperator.min(a.doubleValue(), b);
        } else {
            throw new IllegalArgumentException("Number [" + a + "] can't min Number [" + b + "]");
        }
    }

    public static Number min(byte a, Number b) {
        if (b instanceof Byte) {
            return Math.min(a, (Byte) b);
        } else if (b instanceof Short) {
            return Math.min(a, (Short) b);
        } else if (b instanceof Integer) {
            return Math.min(a, (Integer) b);
        } else if (b instanceof Long) {
            return Math.min(a, (Long) b);
        } else if (b instanceof Float) {
            return Math.min(a, (Float) b);
        } else if (b instanceof Double) {
            return Math.min(a, (Double) b);
        } else {
            throw new IllegalArgumentException("Number [" + b + "] can't min [" + a + "]");
        }
    }

    public static Number min(short a, Number b) {
        if (b instanceof Byte) {
            return Math.min(a, (Byte) b);
        } else if (b instanceof Short) {
            return Math.min(a, (Short) b);
        } else if (b instanceof Integer) {
            return Math.min(a, (Integer) b);
        } else if (b instanceof Long) {
            return Math.min(a, (Long) b);
        } else if (b instanceof Float) {
            return Math.min(a, (Float) b);
        } else if (b instanceof Double) {
            return Math.min(a, (Double) b);
        } else {
            throw new IllegalArgumentException("Number [" + b + "] can't min [" + a + "]");
        }
    }

    public static Number min(int a, Number b) {
        if (b instanceof Byte) {
            return Math.min(a, (Byte) b);
        } else if (b instanceof Short) {
            return Math.min(a, (Short) b);
        } else if (b instanceof Integer) {
            return Math.min(a, (Integer) b);
        } else if (b instanceof Long) {
            return Math.min(a, (Long) b);
        } else if (b instanceof Float) {
            return Math.min(a, (Float) b);
        } else if (b instanceof Double) {
            return Math.min(a, (Double) b);
        } else {
            throw new IllegalArgumentException("Number [" + b + "] can't min [" + a + "]");
        }
    }

    public static Number min(long a, Number b) {
        if (b instanceof Byte) {
            return Math.min(a, (Byte) b);
        } else if (b instanceof Short) {
            return Math.min(a, (Short) b);
        } else if (b instanceof Integer) {
            return Math.min(a, (Integer) b);
        } else if (b instanceof Long) {
            return Math.min(a, (Long) b);
        } else if (b instanceof Float) {
            return Math.min(a, (Float) b);
        } else if (b instanceof Double) {
            return Math.min(a, (Double) b);
        } else {
            throw new IllegalArgumentException("Number [" + b + "] can't min [" + a + "]");
        }
    }

    public static Number min(float a, Number b) {
        if (b instanceof Byte) {
            return Math.min(a, (Byte) b);
        } else if (b instanceof Short) {
            return Math.min(a, (Short) b);
        } else if (b instanceof Integer) {
            return Math.min(a, (Integer) b);
        } else if (b instanceof Long) {
            return Math.min(a, (Long) b);
        } else if (b instanceof Float) {
            return Math.min(a, (Float) b);
        } else if (b instanceof Double) {
            return Math.min(a, (Double) b);
        } else {
            throw new IllegalArgumentException("Number [" + b + "] can't min [" + a + "]");
        }
    }

    public static Number min(double a, Number b) {
        if (b instanceof Byte) {
            return Math.min(a, (Byte) b);
        } else if (b instanceof Short) {
            return Math.min(a, (Short) b);
        } else if (b instanceof Integer) {
            return Math.min(a, (Integer) b);
        } else if (b instanceof Long) {
            return Math.min(a, (Long) b);
        } else if (b instanceof Float) {
            return Math.min(a, (Float) b);
        } else if (b instanceof Double) {
            return Math.min(a, (Double) b);
        } else {
            throw new IllegalArgumentException("Number [" + b + "] can't min [" + a + "]");
        }
    }

    public static Number max(Number a, Number b) {
        if (a instanceof Byte) {
            return NumberOperator.max(a.byteValue(), b);
        } else if (a instanceof Short) {
            return NumberOperator.max(a.shortValue(), b);
        } else if (a instanceof Integer) {
            return NumberOperator.max(a.intValue(), b);
        } else if (a instanceof Long) {
            return NumberOperator.max(a.longValue(), b);
        } else if (a instanceof Float) {
            return NumberOperator.max(a.floatValue(), b);
        } else if (a instanceof Double) {
            return NumberOperator.max(a.doubleValue(), b);
        } else {
            throw new IllegalArgumentException("Number [" + a + "] can't max Number [" + b + "]");
        }
    }

    public static Number max(byte a, Number b) {
        if (b instanceof Byte) {
            return Math.max(a, (Byte) b);
        } else if (b instanceof Short) {
            return Math.max(a, (Short) b);
        } else if (b instanceof Integer) {
            return Math.max(a, (Integer) b);
        } else if (b instanceof Long) {
            return Math.max(a, (Long) b);
        } else if (b instanceof Float) {
            return Math.max(a, (Float) b);
        } else if (b instanceof Double) {
            return Math.max(a, (Double) b);
        } else {
            throw new IllegalArgumentException("Number [" + b + "] can't max [" + a + "]");
        }
    }

    public static Number max(short a, Number b) {
        if (b instanceof Byte) {
            return Math.max(a, (Byte) b);
        } else if (b instanceof Short) {
            return Math.max(a, (Short) b);
        } else if (b instanceof Integer) {
            return Math.max(a, (Integer) b);
        } else if (b instanceof Long) {
            return Math.max(a, (Long) b);
        } else if (b instanceof Float) {
            return Math.max(a, (Float) b);
        } else if (b instanceof Double) {
            return Math.max(a, (Double) b);
        } else {
            throw new IllegalArgumentException("Number [" + b + "] can't max [" + a + "]");
        }
    }

    public static Number max(int a, Number b) {
        if (b instanceof Byte) {
            return Math.max(a, (Byte) b);
        } else if (b instanceof Short) {
            return Math.max(a, (Short) b);
        } else if (b instanceof Integer) {
            return Math.max(a, (Integer) b);
        } else if (b instanceof Long) {
            return Math.max(a, (Long) b);
        } else if (b instanceof Float) {
            return Math.max(a, (Float) b);
        } else if (b instanceof Double) {
            return Math.max(a, (Double) b);
        } else {
            throw new IllegalArgumentException("Number [" + b + "] can't max [" + a + "]");
        }
    }

    public static Number max(long a, Number b) {
        if (b instanceof Byte) {
            return Math.max(a, (Byte) b);
        } else if (b instanceof Short) {
            return Math.max(a, (Short) b);
        } else if (b instanceof Integer) {
            return Math.max(a, (Integer) b);
        } else if (b instanceof Long) {
            return Math.max(a, (Long) b);
        } else if (b instanceof Float) {
            return Math.max(a, (Float) b);
        } else if (b instanceof Double) {
            return Math.max(a, (Double) b);
        } else {
            throw new IllegalArgumentException("Number [" + b + "] can't max [" + a + "]");
        }
    }

    public static Number max(float a, Number b) {
        if (b instanceof Byte) {
            return Math.max(a, (Byte) b);
        } else if (b instanceof Short) {
            return Math.max(a, (Short) b);
        } else if (b instanceof Integer) {
            return Math.max(a, (Integer) b);
        } else if (b instanceof Long) {
            return Math.max(a, (Long) b);
        } else if (b instanceof Float) {
            return Math.max(a, (Float) b);
        } else if (b instanceof Double) {
            return Math.max(a, (Double) b);
        } else {
            throw new IllegalArgumentException("Number [" + b + "] can't max [" + a + "]");
        }
    }

    public static Number max(double a, Number b) {
        if (b instanceof Byte) {
            return Math.max(a, (Byte) b);
        } else if (b instanceof Short) {
            return Math.max(a, (Short) b);
        } else if (b instanceof Integer) {
            return Math.max(a, (Integer) b);
        } else if (b instanceof Long) {
            return Math.max(a, (Long) b);
        } else if (b instanceof Float) {
            return Math.max(a, (Float) b);
        } else if (b instanceof Double) {
            return Math.max(a, (Double) b);
        } else {
            throw new IllegalArgumentException("Number [" + b + "] can't max [" + a + "]");
        }
    }
}
