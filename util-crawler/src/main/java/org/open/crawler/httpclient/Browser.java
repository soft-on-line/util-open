package org.open.crawler.httpclient;

import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.open.crawler.cpdetector.CharCodeUtil;
import org.open.crawler.exception.BrowserProxyException;
import org.open.util.BeanUtil;
import org.open.util.ReaderUtil;

/**
 * @author ibm
 * @version $Id: Browser.java,v 1.2 2010/01/26 03:23:51 wyp Exp $
 */
public class Browser {

    /**
     * 定义用于抓取html的最大长度
     */
    public static final int  HTML_MAX_SIZE = 1048576;

    /**
     * 定义用于抓取html的最大长度
     */
    public static final int  HTML_MIN_SIZE = 100;

    private static final Log log           = LogFactory.getLog(Browser.class);

    protected String         charSet;
    protected String         html;
    protected byte[]         stream;
    protected int            timeout;

    public Browser() {
        timeout = 0;
    }

    public String getCharSet() {
        return charSet;
    }

    public void getConnection(String url) throws Exception {
        // before do connection set html is null.
        this.html = null;
        this.stream = null;
        this.charSet = "";
        HttpGet httpGet = null;
        HttpClient httpClient = null;
        try {
            HttpParams httpParams = new BasicHttpParams();
            if (this.timeout > 0) // 设置超时
            {
                HttpConnectionParams.setConnectionTimeout(httpParams, this.timeout);
                HttpConnectionParams.setSoTimeout(httpParams, this.timeout);
            }
            httpClient = new DefaultHttpClient(httpParams);
            httpGet = new HttpGet(url);

            httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.2)");
            httpGet.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");

            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                stream = ReaderUtil.readByte(entity.getContent());
                // String contentType = response.getFirstHeader("Content-Type").getValue();
                charSet = CharCodeUtil.getCharSet(stream);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            // Do not feel like reading the response body
            // Call abort on the request object
            if (httpGet != null) {
                httpGet.abort();
            }
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
    }

    /**
     * 根据网页流头信息 以及内容字符信息判断当前网页流编码格式
     *
     * @param entity HTTP entity
     * @param contentType 在response 头信息中的 Content-Type 内容
     * @param data 网页字符流
     * @return 返回得到的网页字符编码格式
     */
    // protected String getCharSet(HttpEntity entity, String contentType, final byte[] data)
    // {
    // String charSet = EntityUtils.getContentCharSet(entity);
    // Pattern pattern = Pattern.compile(
    // "charset\\s*=\\s*([0-9a-zA-Z\\-\\.\\:\\_]+)", Pattern.DOTALL
    // + Pattern.CASE_INSENSITIVE);
    // // System.out.println("charSet:"+charSet);
    // if(StringUtil.isEmpty(charSet))
    // {
    // int f = 0;
    // // 1, content-type
    // Matcher m = pattern.matcher(contentType);
    // if (m.find() == true)
    // {
    // charSet = m.group(1);
    // }
    // else
    // {
    // // 2, utf-32
    // if (data.length >= 4
    // && ((data[0] == 0 && data[1] == 0
    // && data[2] == (byte) 0xfe && data[3] == (byte) 0xff) || (data[0] == (byte) 0xff
    // && data[1] == (byte) 0xfe && data[2] == 0 && data[3] == 0))) {
    // f += 4;
    // charSet = "utf-32";
    // }
    // // 2, utf-8
    // else if (data.length >= 3 && data[0] == (byte) 0xef
    // && data[1] == (byte) 0xbb && data[2] == (byte) 0xbf)
    // {
    // f += 3;
    // charSet = "utf-8";
    // }
    // // 3, utf-16
    // else if (data.length >= 2
    // && ((data[0] == (byte) 0xfe && data[1] == (byte) 0xff) || (data[1] == (byte) 0xfe && data[0] == (byte) 0xff)))
    // {
    // // f += 2; // java auto detect
    // charSet = "utf-16";
    // }
    // else
    // {
    // charSet = "GB18030";
    // }
    // }
    // }
    // return charSet;
    // }

    /**
     * 获取网页字符串流 字符串流通过charSet进行编解码
     *
     * @return 网页内容
     */
    public String getHTML() {
        if (this.html == null) {
            if (this.stream != null) {
                String html = "";
                try {
                    html = new String(stream, charSet);
                    this.html = html;
                } catch (UnsupportedEncodingException e) {
                    log.error(BeanUtil.getMethodName(e) + "() error!", e);
                    html = null;
                }
            }
        }
        return html;
    }

    public byte[] getStream() {
        return stream;
    }

    /**
     * 以post方式进行HTTP连接
     *
     * @param url 连接URL
     * @param httphead HTTP头
     * @throws Exception
     */
    public void postConnection(String url, String httphead) throws Exception {
        this.html = null;
        this.stream = null;
        HttpPost httpPost = null;
        HttpClient httpClient = null;
        try {
            HttpParams httpParams = new BasicHttpParams();
            if (this.timeout > 0) // 设置超时
            {
                HttpConnectionParams.setConnectionTimeout(httpParams, this.timeout);
                HttpConnectionParams.setSoTimeout(httpParams, this.timeout);
            }
            httpClient = new DefaultHttpClient(httpParams);
            httpPost = new HttpPost(url);

            StringEntity reqEntity = new StringEntity(httphead);
            httpPost.setEntity(reqEntity);

            HttpResponse response = httpClient.execute(httpPost);

            if (response.getStatusLine().getStatusCode() > 300) {
                throw new BrowserProxyException("url:" + url + " fail! status code:"
                                                + response.getStatusLine().getStatusCode());
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                stream = ReaderUtil.readByte(entity.getContent());
                // String contentType = response.getFirstHeader("Content-Type").getValue();
                // charSet = CharCodeUtil.getCharSet(entity, contentType, stream);
                charSet = CharCodeUtil.getCharSet(stream);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            // Do not feel like reading the response body
            // Call abort on the request object
            if (httpPost != null) {
                httpPost.abort();
            }
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
    }

    /**
     * 设置超时时间
     *
     * @param timeout 超时 毫秒
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void shutdown() {
        // When HttpClient instance is no longer needed,
        // shut down the connection manager to ensure
        // immediate deallocation of all system resources
        // httpclient.getConnectionManager().shutdown();
    }

}
