package lebah.portal;

public class MobileVC {
	
	private static MobileVC instance = null;
	
	private MobileVC() {
		
	}
	
	public static MobileVC getInstance() {
		if ( instance == null ) instance = new MobileVC();
		return instance;
	}
	
	

}
