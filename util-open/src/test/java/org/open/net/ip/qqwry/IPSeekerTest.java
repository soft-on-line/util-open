package org.open.net.ip.qqwry;


import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IPSeekerTest extends TestCase {

	private static final Log log      = LogFactory.getLog(IPSeekerTest.class);
	private IPSeeker         ipSeeker = IPSeeker.instance;

	public void testGetIPEntriesDebug() {

	}

	public void testGetIPLocation() {

		IPLocation ipLocation = ipSeeker.getIPLocation("127.0.0.1");

		log.info("IP_Area:" + ipLocation.getCountry());
		log.info("IP_Area:" + ipLocation.getArea());
		log.info("IP_Area:" + ipLocation.toString());
		log.info("IP_Area:" + ipLocation.toString());

		log.info("IP_Area:" + ipSeeker.getIPLocation("115.238.73.181"));
	}

	public void testGetIPEntries() {

	}

	public void testGetCountryByteArray() {

	}

	public void testGetCountryString() {

	}

	public void testGetAreaByteArray() {

	}

	public void testGetAreaString() {

	}

	public void testGetIPInfo() {

	}

}
