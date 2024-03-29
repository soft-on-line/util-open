package org.open.test;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import junit.framework.Assert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.open.util.DateUtil;
import org.open.util.FileUtil;
import org.open.util.RandomUtil;
import org.open.util.RegexpUtil;
import org.open.util.debug.DebugUtil;
import org.springframework.util.DigestUtils;

public strictfp class TestJDK {

	private static final Log log = LogFactory.getLog(TestJDK.class);

	// @TestAnnotation(name = "moon", age = "10")
	// public void testAnnotation_() {
	// log.info("This is testAnnotation_().");
	// }
	//
	// public static void testAnnotation() {
	// TestJDK testJDK = new TestJDK();
	// testJDK.testAnnotation_();
	//
	// Annotation[] annotations = testJDK.getClass().getAnnotations();
	// if (annotations == null || annotations.length < 1) {
	// log.info("No annotations.");
	// } else {
	// for (Annotation annotation : annotations) {
	// log.info(annotation.toString());
	// }
	// }
	//
	// Method[] methods = testJDK.getClass().getMethods();
	// if (methods == null || methods.length < 1) {
	// log.info("No methods.");
	// } else {
	// for (Method method : methods) {
	// if (method.isAnnotationPresent(TestAnnotation.class)) {
	// for (Annotation annotation : method.getAnnotations()) {
	// TestAnnotation testAnnotation = method.getAnnotation(TestAnnotation.class);
	//
	// log.info(testAnnotation.name());
	// log.info(testAnnotation.age());
	//
	// log.info(annotation.toString());
	// }
	//
	// log.info("Annotation-method: " + method.getName());
	// }
	// }
	// }
	// }
	//
	// public static void testObserver() {
	// TestObservable testObservable = new TestObservable();
	//
	// TestObserver testObserver = new TestObserver(testObservable);
	//
	// testObservable.setName("moon.");
	// testObservable.setName("zhima.");
	// }

	public static void testClone() {
		Object[] obj = new Object[2];

		obj.clone();
		// new Object();
		// Object obj1 = obj.;
	}

	public static void testIterator() {
		List<String> list = new ArrayList<String>();

		for (int i = 0; i < 10; i++) {
			list.add(String.valueOf(i));
		}

		//        Iterator<String> iterator = list.iterator();
		// for(String str : iterator){
		//
		// }
	}

	public static void testMath() {
		double cent = 0.5;

		BigDecimal val = new BigDecimal(0.7);
		BigDecimal newCent = BigDecimal.valueOf(cent).divide(val, BigDecimal.ROUND_HALF_EVEN);

		log.info(val.intValue());
		log.info(val.floatValue());
		log.info(val.longValue());
		log.info(newCent.longValue());
		log.info(newCent.intValue());
	}

	static int length = 0x8FFFFFF;

	public static void testNIO() {
		try {
			//            long st = new Date().getTime();
			@SuppressWarnings("resource")
			MappedByteBuffer mb = new RandomAccessFile("d:/temp/my_nio.dat", "rw").getChannel().map(FileChannel.MapMode.READ_WRITE, 0, length);
			for (int i = 0; i < length; i++) {
				mb.put((byte) 'p');
			}
			System.out.println("write finished");

			// log.infoUsedTime(new Date().getTime() - st);
			System.out.println("length:" + length);

			// st = new Date().getTime();
			// RandomAccessFile raf = new RandomAccessFile("d:/temp/my.data","rw");
			//
			// for (int i = 0; i < length; i++) {
			// raf.write((byte) 'p');
			// }
			// System.out.println("write finished");
			//
			// log.infoUsedTime(new Date().getTime() - st);
			// System.out.println("length:" + length);

		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void _testDouble(String testDouble) {
		log.info(testDouble + " Regexp:" + RegexpUtil.isMatch(testDouble, "(\\d{1,}\\.\\d{0,2})"));
		try {
			// DecimalFormat decimalFormat = new DecimalFormat();
			// decimalFormat.setMaximumFractionDigits(2);
			// decimalFormat.setParseBigDecimal(false);
			DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance();
			decimalFormat.applyPattern("0.00");

			log.info(Double.valueOf(decimalFormat.format(Double.valueOf(testDouble))));
			log.info(Double.valueOf(testDouble));

			double d = Double.valueOf(testDouble);
			if (Double.valueOf(decimalFormat.format(d)) != d) {
				log.info("Wrong!");
			} else {
				log.info("Right!");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			log.info("Wrong!");
		}
	}

	@Test
	public strictfp void testDouble() {
		//		_testDouble("879..");
		//		_testDouble(".879");
		//		_testDouble(".87");
		//		_testDouble("0.87");
		//		_testDouble("100000000000000000000000000000000000000000000011110.87");

		int time = 18857;
		int redo = 99977;

		//		System.out.println(0.001 * time);
		//		System.out.println(0.001 * redo);
		System.out.println(0.001 * time * redo);
		System.out.println(0.001 * (time * redo));
		System.out.println(1.0 * time * redo / 1000);

		BigDecimal a = new BigDecimal(time);
		BigDecimal b = new BigDecimal(redo);
		BigDecimal c = a.multiply(b);
		//		double md = 0.001;
		float md = 0.001f;
		System.out.println(new BigDecimal(0.001f));
		System.out.println(new BigDecimal(0.001));
		BigDecimal d = new BigDecimal(md);
		BigDecimal f = c.multiply(d);
		System.out.println(f);
		System.out.println(a.multiply(b));
		System.out.println(a.multiply(b).multiply(d));
		System.out.println("Real:");
		System.out.println(a.multiply(b).multiply(new BigDecimal("0.001")));

		int m = 1102342342;
		int n = 234234234;
		int k = m * n;
		long y = 1;
		long l = m * n;
		long x = y * m * n;
		System.out.println(k);
		System.out.println(l);
		System.out.println(x);

		System.out.println(666 * 111);

		System.out.println(new BigInteger(String.valueOf(m)).multiply(new BigInteger(String.valueOf(n))));

		System.out.println(new BigDecimal(0.1).toString()); // 0.1000000000000000055511151231257827021181583404541015625
		System.out.println(new BigDecimal("0.1").toString()); // 0.1
		System.out.println(new BigDecimal(Double.toString(0.1000000000000000055511151231257827021181583404541015625)).toString()); // 0.1
		System.out.println(new BigDecimal(Double.toString(0.1)).toString()); // 0.1
	}

	@Test
	public void testString() {
		log.info("\r\n   \r\n  private static final long serialVersionUID  \r\n\r\n".matches("[.|\\s]*private[.|\\s]*static[.|\\s]*final[.|\\s]*long[.|\\s]*serialVersionUID[.|\\s]*"));

		String content = "<a href=\"\" style=\"text-decoration:none;\"  target=\"_blank\" >";
		content = content.replaceAll("(style=\".+\")", "");
		DebugUtil.print("Content:" + content);
	}

	public static void test2() {
		Long l = (Long) null;
		System.out.println(l);
	}

	public static void test3() {
		String str = "http://www.blueandgold.com/images/photos/Image/basketball/players/harangody_luke/harangody_guard_sfr09_200x300.jpg";
		System.out.println(str.length());
		System.out.println(str);
		int l = 255;
		for (int i = 0; i < l; i++) {
			System.out.print("i");
		}
	}

	private static void innerThrowRuntimeException() {
		if (true) {
			// throw new Exception();
		}
		if (true) {
			throw new RuntimeException();
		}
	}

	public static void test4() {
		try {
			if (true) {
				throw new RuntimeException();
			}
		}
		catch (Exception e) {
			System.out.println("I catched RuntimeException.");
		}

		try {
			if (true) {
				throw new RuntimeException();
			}
		}
		catch (RuntimeException e) {
			System.out.println("I catched RuntimeException.");
		}

		try {
			if (true) {
				Assert.assertNotNull("test", null);
			}
		}
		catch (Error e) {
			System.out.println("I catched Error.");
		}

		// innerThrowRuntimeException();

		try {
			innerThrowRuntimeException();
		}
		catch (Exception e) {
			System.out.println("I catched innerThrowRuntimeException.");
		}
	}

	public static void test5() {
		File file = new File("d:/测试.rar");
		System.out.println(file.getName());

		String multiUserName = "hundsun,\r\nzhima_2008,    test";
		List<String> listUserName = new ArrayList<String>();
		String[] arrayUserName = multiUserName.split("[;|；|,|，|\\s]");
		for (String userName : arrayUserName) {
			if (null == userName || userName.trim().length() == 0) {
				continue;
			}
			listUserName.add(userName.trim());
			System.out.println(userName.trim());
		}
		// System.out.println(listUserName);
	}

	@Test
	public void voidTestDigestUtils() {
		String str = "";
		str = DigestUtils.md5DigestAsHex(str.getBytes());
		System.out.println(str);
	}

	// @Test
	public strictfp static void testFloat() {
		DebugUtil.print(0.0f / (1 * -0.5));
		DebugUtil.print(Math.abs(0.0f / (1 * -0.5)));

		DebugUtil.print(Float.valueOf((0.045f - 0.031f) * 100).intValue());
		DebugUtil.print((char) 254);

		DebugUtil.print(Float.valueOf(0.05f).compareTo(0.04f));
		DebugUtil.print(Float.compare(0.05f, 0.04f));

		double a = 0.5;

		Number a_ = a;
		DebugUtil.print(a_.getClass().isInstance(Double.class));
		DebugUtil.print(a_ instanceof Double);

		Double b = new Double(0.7f);
		DebugUtil.print(b.getClass().isInstance(Double.class));

		Number c = 5;

		DebugUtil.print(c.doubleValue());

		DebugUtil.print(Math.sqrt(64));
		DebugUtil.print(Math.pow(64, 0.5));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void testMap() {

		DebugUtil.printMemoryInfo();
		String[] data = new String[100000];
		for (int i = 0; i < data.length; i++) {
			data[i] = String.valueOf(i);
		}
		DebugUtil.printMemoryInfo();

		HashMap<Integer, Object> hashMap = new HashMap<Integer, Object>();
		hashMap.put(null, "test");
		for (int i = 0; i < data.length; i++) {
			// hashMap.put(i, data[i]);
			hashMap.put(i, data[i].intern());
		}
		DebugUtil.printMemoryInfo();

		HashMap<Integer, Object> hashMap1 = new HashMap<Integer, Object>();
		for (int i = 0; i < data.length; i++) {
			// hashMap1.put(i, data[i]);
			hashMap1.put(i, data[i].intern());
		}
		DebugUtil.printMemoryInfo();

		HashMap<Integer, Object> hashMap2 = new HashMap<Integer, Object>();
		for (int i = 0; i < data.length; i++) {
			// hashMap2.put(i, data[i]);
			hashMap2.put(i, data[i].intern());
		}
		DebugUtil.printMemoryInfo();

		Object a = new Object();
		Object b = new Object();

		HashMap hashMap3 = new HashMap();
		hashMap3.put(a, a);
		hashMap3.put(b, b);

		HashMap hashMap4 = new HashMap();
		hashMap4.put(a, a);
		hashMap4.put(b, b);

		DebugUtil.print(hashMap4.get(a).equals(hashMap3.get(a)));
	}

	public static void testCharSet() {
		DebugUtil.print(Charset.isSupported("void"));
		DebugUtil.print(Charset.isSupported("gbk"));
		DebugUtil.print(Charset.isSupported("GBK"));
	}

	public static void testSwitch() {
		for (int i = 0; i < 10; i++) {
			switch (i) {
				case 5:
					continue;
				case 6:
					break;
			}
			System.out.println(i);
		}
	}

	public static void testBigInteger() {
		BigInteger bigInteger = new BigInteger("0");
		System.out.println(bigInteger);

		int n = 120;

		bigInteger = bigInteger.setBit(n);
		System.out.println(bigInteger);
		System.out.println(bigInteger.bitCount());

		n = 121;

		bigInteger = bigInteger.setBit(n);
		System.out.println(bigInteger);
		System.out.println(bigInteger.bitCount());

		bigInteger = bigInteger.setBit(1);
		System.out.println(bigInteger);

		System.out.println(bigInteger.testBit(2));

		byte b = 10;
		System.out.println(b);

		System.out.println(Integer.valueOf(0).byteValue());
		System.out.println(Integer.valueOf(1).byteValue());
		// System.out.println(Integer.valueOf(10).byteValue());
		// System.out.println(Integer.valueOf(100).byteValue());
		// System.out.println(Integer.valueOf(1000).byteValue());
		System.out.println(Integer.valueOf(1210).byteValue());
		System.out.println(Integer.valueOf(12100).byteValue());
		System.out.println(Integer.valueOf(12101).byteValue());
		System.out.println(Integer.valueOf(12102).byteValue());

		char[] data = "12102".toCharArray();
		byte[] b_data = new byte[data.length];
		for (int i = 0; i < data.length; i++) {
			b_data[i] = (byte) data[i];
		}
		System.out.println(new String(b_data));
	}

	//
	// public static void testBeanUtils() {
	// PropertyDescriptor[] array = BeanUtils.getPropertyDescriptors(TestObject.class);
	// for (PropertyDescriptor each : array) {
	// System.out.println(each.getName() + ";" + each.getPropertyType());
	// }
	// }

	public static void testDate() {
		System.out.println(String.valueOf(DateUtil.getDate(5000, 1, 1).getTime()).length());
		System.out.println((DateUtil.getYMD() + String.valueOf(new Date().getTime())).length());
	}

	public static void testBigDecimal() {
		long a = 5;
		long b = 3;
		BigDecimal c = new BigDecimal(a).divide(new BigDecimal(b), 2, BigDecimal.ROUND_HALF_EVEN).multiply(new BigDecimal(100));
		System.out.println(c);
		System.out.println(c.longValue());
	}

	public static void testUUID() {
		// DebugUtil.print(UUID.fromString("moon"));
		// DebugUtil.print(UUID.fromString("moon"));
		// DebugUtil.print(UUID.randomUUID().getLeastSignificantBits());
		// DebugUtil.print(UUID.randomUUID().timestamp());
		DebugUtil.print(new Random().nextInt());
		DebugUtil.print(RandomUtil.nextInt(10, 99));
	}

	@SuppressWarnings("unused")
	private static class T {

		private Integer a;

		public T(Integer a) {
			this.a = a;
		}

		@Override
		public boolean equals(Object obj) {
			if (null == a) {
				return this.equals(obj);
			}
			if (obj instanceof T) {
				return this.a.equals(((T) obj).a);
			} else if (obj instanceof Integer) {
				return this.a.equals((Integer) obj);
			} else {
				return this.equals(obj);
			}
		}

		@Override
		public int hashCode() {
			if (null == a) {
				return this.hashCode();
			}
			return this.a.hashCode();
		}

		public String toString() {
			return this.a.toString();
		}
	}

	private static String[] arrayName = { "鸿", "源", "鹏", "网", "思", "灵", "毅", "航", "黎", "越", "辉", "信", "诚", "汇", "鼎", "宇", "腾", "道", "程", "达", "博" };

	public static void testName() {
		int count = 0;
		for (int i = 0; i < arrayName.length; i++) {
			for (int j = 0; j < arrayName.length; j++) {
				if (i != j) {
					// System.out.println((++count) + "\t=>杭州<font color='red'>" + arrayName[i] + arrayName[j]
					// + "</font>网络科技有限公司<br/>");
					System.out.println((++count) + "\t=>杭州'" + arrayName[i] + arrayName[j] + "'网络科技有限公司");
				}
			}
			System.out.println();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// testObserver();
		// testClone();
		// testAnnotation();
		// testMath();
		// testNIO();
		// testDouble();
		// testString();
		// test2();
		// test3();
		// test4();
		// test5();
		// testFloat();
		// testMap();
		// testCharSet();
		// testSwitch();
		// testBigInteger();
		// testBeanUtils();
		// testDate();
		// testBigDecimal();
		// testUUID();

		testName();
	}

	@Test
	public void test() {
		DecimalFormat format = new DecimalFormat();

		format.applyPattern("0.00");

		Float d = Float.valueOf(56.08f);

		System.out.println(format.format(d));
	}

	@Test
	public void testFile() throws IOException {
		File file = new File("d://tt.jpg");
		System.out.println(file.getName());
		System.out.println(file.getAbsolutePath());
		System.out.println(file.getCanonicalPath());
		System.out.println(file.getParent());
		System.out.println(file.getPath());

		System.out.println(FileUtil.getNameNoSuffix(file));
		System.out.println(FileUtil.getSuffix(file));
	}

	@Test
	public void test6() {
		String srcName = "tt    asdfaf";
		//		String srcName = "tt asdfaf";
		srcName = srcName.replaceAll("\\s{2,}", " ");
		System.out.println(srcName);
	}

}
