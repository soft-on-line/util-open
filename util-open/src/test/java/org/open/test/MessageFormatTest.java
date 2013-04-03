package org.open.test;

import java.text.MessageFormat;

import org.junit.Test;

public class MessageFormatTest {
    
    @Test
    public void testMessageFormat(){
        
        String pattern = "from ''test'' a like ''{0}%'' and b like \'{0}\\%\'";
//        String pattern = "from a like {0} and b like {0}";
        
        System.out.println(MessageFormat.format(pattern, "06"));
    }
}
