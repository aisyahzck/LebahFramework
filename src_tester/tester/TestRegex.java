package tester;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestRegex {
	
	public static void main(String[] args) throws Exception {
		

		//String strPattern = "^[0-9a-zA-Z_-]{3,15}$";
		String strPattern = "^[0-9a-zA-Z_-]*$";
		String test = "sianG-_898";
		Pattern pattern = Pattern.compile(strPattern);
		Matcher matcher = pattern.matcher(test);
		System.out.println(matcher.matches());
		
	}
}
