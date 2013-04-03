package org.open.crawler.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.open.crawler.cpdetector.CharCodeUtil;
import org.open.crawler.exception.BrowserProxyException;
import org.open.util.ReaderUtil;

/**
 * Copyright (c) 2009,蚊子工作室 All rights reserved. 通过代理服务器连接的模拟浏览器 引用开源项目 httpclient
 *
 * @see httpclient
 * @author peng
 * @version $Id: BrowserProxy.java,v 1.2 2010/01/26 03:23:51 wyp Exp $
 */
public class BrowserProxy extends Browser {

    private String proxyIp;
    private String proxyPasswd;
    private int    proxyPort;
    private String proxyUser;

    public BrowserProxy() {
        this.proxyIp = null;
        this.proxyPort = -1;
    }

    /**
     * 构造函数
     *
     * @param proxyIp 代理服务器IP
     * @param proxyPort 代理服务器端口
     */
    public BrowserProxy(String proxyIp, int proxyPort) {
        this.proxyIp = proxyIp;
        this.proxyPort = proxyPort;
    }

    /**
     * 构造函数
     *
     * @param proxyIp 代理服务器IP
     * @param proxyPort 代理服务器端口
     * @param proxyUser 代理服务器鉴权用户
     * @param proxyPasswd 代理服务其鉴权用户密码
     */
    public BrowserProxy(String proxyIp, int proxyPort, String proxyUser, String proxyPasswd) {
        this.proxyIp = proxyIp;
        this.proxyPort = proxyPort;
        this.proxyPasswd = proxyPasswd;
        this.proxyUser = proxyUser;
    }

    /**
     * 设置代理 并通过http的get方式获取输入地址的html
     *
     * @param url 需要获取的url地址
     * @throws Exception
     */
    public void getConnection(String url) throws Exception {
        // before do connection set html is null.
        this.html = null;
        this.stream = null;
        if (this.proxyIp == null || this.proxyPort == -1) {
            throw new Exception("ProxyIP or ProxyPort is null!");
        } else {
            HttpGet httpGet = null;
            DefaultHttpClient httpclient = null;
            try {
                HttpParams httpParams = new BasicHttpParams();

                if (super.timeout > 0) {
                    HttpConnectionParams.setConnectionTimeout(httpParams, super.timeout);
                    HttpConnectionParams.setSoTimeout(httpParams, super.timeout);
                }
                httpclient = new DefaultHttpClient(httpParams);
                httpGet = new HttpGet(url);
                httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.1.2)");
                httpGet.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
                if (proxyUser != null && proxyPasswd != null) {
                    httpclient.getCredentialsProvider().setCredentials(
                                                                       new AuthScope(proxyIp, proxyPort),
                                                                       new UsernamePasswordCredentials(proxyUser,
                                                                                                       proxyPasswd));
                }
                HttpHost proxy = new HttpHost(this.proxyIp, this.proxyPort);
                httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
                HttpResponse response = httpclient.execute(httpGet);

                if (response.getStatusLine().getStatusCode() > 300) {
                    throw new BrowserProxyException("url:" + url + " fail! status code:"
                                                    + response.getStatusLine().getStatusCode());
                }
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    stream = ReaderUtil.readByte(entity.getContent());
                    // String contentType = response.getFirstHeader("Content-Type").getValue();
                    // charSet = getCharSet(entity, contentType, stream);
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
                if (httpclient != null) {
                    httpclient.getConnectionManager().shutdown();
                }
            }
        }
    }

    /**
     * 设置代理 并通过http的post方式获取输入地址的html
     *
     * @param url 需要获取的url地址
     * @throws Exception
     */
    public void postConnection(String url, String httphead) throws Exception {
        // before do connection set html is null.
        this.html = null;
        this.stream = null;
        HttpPost httpPost = null;
        DefaultHttpClient httpclient = null;
        try {
            HttpParams httpParams = new BasicHttpParams();

            if (super.timeout > 0) {
                HttpConnectionParams.setConnectionTimeout(httpParams, super.timeout);
                HttpConnectionParams.setSoTimeout(httpParams, super.timeout);
            }
            httpclient = new DefaultHttpClient(httpParams);
            httpPost = new HttpPost(url);
            StringEntity reqEntity = new StringEntity(httphead);
            httpPost.setEntity(reqEntity);
            if (proxyUser != null && proxyPasswd != null) {
                httpclient.getCredentialsProvider().setCredentials(
                                                                   new AuthScope(proxyIp, proxyPort),
                                                                   new UsernamePasswordCredentials(proxyUser,
                                                                                                   proxyPasswd));
            }
            HttpHost proxy = new HttpHost(this.proxyIp, this.proxyPort);
            httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
            HttpResponse response = httpclient.execute(httpPost);

            if (response.getStatusLine().getStatusCode() > 300) {
                throw new BrowserProxyException("url:" + url + " fail! status code:"
                                                + response.getStatusLine().getStatusCode());
            }
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                stream = ReaderUtil.readByte(entity.getContent());
                // String contentType = response.getFirstHeader("Content-Type").getValue();
                // charSet = getCharSet(entity, contentType, stream);
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
            if (httpclient != null) {
                httpclient.getConnectionManager().shutdown();
            }
        }
    }

    /**
     * 设置代理服务器信息
     *
     * @param proxyIp 代理服务器IP
     * @param proxyPort 代理服务器端口
     */
    public void setProxy(String proxyIp, int proxyPort) {
        this.proxyIp = proxyIp;
        this.proxyPort = proxyPort;
    }

    /**
     * 设置代理服务器
     *
     * @param proxyIp 代理服务器ＩＰ
     * @param proxyPort　代理服务器端口
     * @param proxyUser　代理服务器登陆用户名
     * @param proxyPasswd　代理服务器登陆密码
     */
    public void setProxy(String proxyIp, int proxyPort, String proxyUser, String proxyPasswd) {
        this.proxyIp = proxyIp;
        this.proxyPort = proxyPort;
        this.proxyUser = proxyUser;
        this.proxyPasswd = proxyPasswd;
    }

    public String toString() {
        return "[ProxyIP:" + proxyIp + " ProxyPort:" + proxyPort + "]";
    }

}
