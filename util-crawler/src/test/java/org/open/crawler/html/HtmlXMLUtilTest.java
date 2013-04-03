package org.open.crawler.html;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.open.util.ReaderUtil;
import org.open.util.RegexpUtil;
import org.open.util.debug.DebugUtil;

import junit.framework.TestCase;


public class HtmlXMLUtilTest extends TestCase {
    private static final Log log = LogFactory.getLog(HtmlXMLUtilTest.class);
    private DebugUtil du = new DebugUtil(DebugUtil.InstanceModel.ConsoleModel);
    
    public void testGetAllImage(){
        log.info(HtmlXMLUtil.getAllImage(ReaderUtil.read("d:/test_parse_img.html")));
    }
    
    private final static String RegexpParseImg
        = "((<[a|A].+?>.*?<[i|I][m|M][g|G].+?>.*?</[a|A]>)|(<[i|I][m|M][g|G].+?>))";
    
    public void testParseImg() {
        
        String html = ReaderUtil.read("d:/test_parse_img.html");
        //html = html.toLowerCase();
        
        log.info(RegexpUtil.matchGroups(html, RegexpParseImg));
        
        List<HtmlXMLUtil.ImgInfo> listImgInfo = HtmlXMLUtil.parseImg(html);
        for(HtmlXMLUtil.ImgInfo imgInfo : listImgInfo){
            log.info(imgInfo.toString());
        }
    }

}
