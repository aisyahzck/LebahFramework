package lebah.util;

import java.io.PrintStream;

public class Log {
	
	public static void out(String str) {
		System.out.println(str);
	}
	
	public static PrintStream out() {
		return System.out;
	}

}
