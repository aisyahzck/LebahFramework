package lebah.msg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JSON {
	
	private List<String> elements = new ArrayList<String>();
	private List<String> arrays = new ArrayList<String>();
	private Map<String, String> mapArr = new HashMap<String, String>();
	private Map<String, String> mapValue = new HashMap<String, String>();
	
	public JSON add(String name, String value) {
		mapValue.put(name, value);
		return this;
	}
	
	public void add(String name, JSON json) {
		
		if ( mapArr.get(name) == null ) {
			mapArr.put(name, json.getJSONElements());
		} else {
			String s = (String) mapArr.get(name);
			s += ", " + json.getJSONElements();
			mapArr.put(name, s);
		}
		
	}
	
	public String getJSONElements() {
		
		if ( mapValue.size() > 0 ) {
			Set<String> keys = mapValue.keySet();
			for ( String k : keys ) {
				elements.add("\"" + k + "\"" + ":" + "\"" + mapValue.get(k) + "\"");
			}
		}
		
		
		if ( mapArr.size() > 0 ) {
			Set<String> keys = mapArr.keySet();
			for ( String k : keys ) {
				arrays.add("\"" + k + "\"" + ":[" + mapArr.get(k) + "]");
			}
		}
		
		String s = "{";
		int i = 0;
		for ( String e : elements ) {
			if ( i++ > 0 ) s += ", ";
			s += e;
		}
		
		if ( arrays.size() > 0 ) s += ", ";
		i = 0;
		for ( String e : arrays ) {
			if ( i++ > 0 ) s += ", ";
			s += e;
		}
	
		s += "}";
		return s;
	}

	
	public static void main(String[] args) throws Exception {
		
		JSON json = new JSON();
		
		JSON user = new JSON();
		user.add("id", "ali").add("name", "Ali Albab").add("age", "70");
		json.add("users", user);
		
		user = new JSON();
		user.add("id", "abu").add("name", "Abu Bakar").add("age", "91");
		json.add("users", user);

		json.add("messages", "55");
		json.add("inbox", "8");
		
		user = new JSON();
		user.add("id", "123").add("name", "Bedah");
		user.add("name", "Zabedah Ismail"); //this will replace the above Bedah
		json.add("users", user);
		
		user = null;
		
		System.out.println(json.getJSONElements());
		
	}
	

}
