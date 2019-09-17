package io.study.test.basic;

public class MoveSignTest {

	public static void main(String[] args) {//1-> 0000000001 
		for (int i = 0; i < 1001; i++) {
			int depth = 3<<i;
			int depth1 = 521>>i;
			System.out.println(depth1);
		//System.out.println(depth);
		}

	}

}
