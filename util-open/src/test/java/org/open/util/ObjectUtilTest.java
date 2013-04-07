package org.open.util;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.junit.Test;

public class ObjectUtilTest implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 6757144428789150587L;

	@Test
	public void test() {
		TestObject testObject = new TestObject();
		testObject.setObj_int(10);
		testObject.setObj_string("test");
		System.out.println(testObject);

		String objectString = ObjectUtil.object2String(testObject);
		System.out.println(objectString);

		String base64 = CodeUtil.BASE64Encoder(objectString);
		System.out.println(base64);

		System.out.println(new String(CodeUtil.BASE64Decoder(base64)));

		System.out.println(ObjectUtil.string2object(objectString, TestObject.class));
		//        System.out.println(ObjectUtil.string2object(objectString));
	}

	//    @Test
	public void testDeserialize() throws IOException, ClassNotFoundException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		//        BigInteger bi = new BigInteger("0");

		TestObject bi = new TestObject();
		bi.setObj_int(10);
		bi.setObj_string("test");

		oos.writeObject(bi);
		byte[] str = baos.toByteArray();

		System.out.println(new String(str));
		String base64 = CodeUtil.BASE64Encoder(str);
		System.out.println(base64);

		//        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(new String(str).getBytes())));
		ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new ByteArrayInputStream(CodeUtil.BASE64Decoder(base64))));
		Object obj = ois.readObject();
		System.out.println(obj);
		System.out.println(obj.getClass().getName());
		System.out.println(obj);
	}

}
