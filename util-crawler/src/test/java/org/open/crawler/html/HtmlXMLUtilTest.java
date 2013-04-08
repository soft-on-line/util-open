package org.open.crawler.html;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.open.util.ReaderUtil;
import org.open.util.RegexpUtil;


public class HtmlXMLUtilTest extends TestCase {
    private static final Log log = LogFactory.getLog(HtmlXMLUtilTest.class);
    
    public void testGetAllImage() throws IOException{
        log.info(HtmlXMLUtil.getAllImage(ReaderUtil.read("d:/test_parse_img.html")));
    }
    
    private final static String RegexpParseImg
        = "((<[a|A].+?>.*?<[i|I][m|M][g|G].+?>.*?</[a|A]>)|(<[i|I][m|M][g|G].+?>))";
    
    public void testParseImg() throws IOException {
        
        String html = ReaderUtil.read("d:/test_parse_img.html");
        //html = html.toLowerCase();
        
        log.info(RegexpUtil.matchGroups(html, RegexpParseImg));
        
        List<HtmlXMLUtil.ImgInfo> listImgInfo = HtmlXMLUtil.parseImg(html);
        for(HtmlXMLUtil.ImgInfo imgInfo : listImgInfo){
            log.info(imgInfo.toString());
        }
    }

}
