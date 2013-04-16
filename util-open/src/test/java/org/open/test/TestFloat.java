package org.open.test;


public class TestFloat {

	public static void main(String[] args) {
		int x = 0x3d800000;
		int i = 1 << 22;
		int j = 1 << 4;
		float f = 0.1f;
		int y = Float.floatToIntBits(f);
		float rest = f - ((float) 1) / j;
		while (i > 0) {
			j <<= 1;
			float deta = ((float) 1) / j;
			if (rest >= deta) {
				rest -= deta;
				x |= i;
			}
			i >>= 1;
		}
		pr(x);
		pr(y);
	}

	static void pr(int i) {
		System.out.println(Integer.toBinaryString(i));
	}

}