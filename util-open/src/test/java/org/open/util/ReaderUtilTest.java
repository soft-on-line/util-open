package org.open.util;


import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

public class ReaderUtilTest {

	@Test
	public void testGetFileNamesFile() {

	}

	@Test
	public void testGetFileNamesFileBoolean() {

	}

	@Test
	public void testReadFile() {

	}

	@Test
//	public void testReadURI() throws URISyntaxException, MalformedURLException {
	public void testReadURI() throws URISyntaxException, IOException {
		String uri = "http://www.baidu.com";
		String content = ReaderUtil.read(new URL(uri));
		System.out.println("Content:" + content);
	}

	@Test
	public void testReadInputStream() {

	}

	@Test
	public void testReadInputStreamString() {

	}

	@Test
	public void testReadString() {

	}

	@Test
	public void testReadStringString() {

	}

	@Test
	public void testReadFileString() {

	}

	@Test
	public void testReadByteFile() {

	}

	@Test
	public void testReadByteInputStream() {

	}

	@Test
	public void testReadByteString() {

	}

}
