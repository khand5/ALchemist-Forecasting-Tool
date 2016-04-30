package _2xb3.finalProject.model;

/**
 * Enumeration of the Canadian Provinces and how they are seen in the database.
 * Used to sanitize the database and further requests.
 */
public enum Province {
	Ontario("Ontario"),
	Quebec("Quebec"),
	NovaScotia("Nova Scotia"),
	PrinceEdwardIsland("Prince Edward Island"),
	Manitoba("Manitoba"),
	Alberta("Alberta"),
	NewBrunswick("New Brunswick"),
	NewfoundlandAndLabrador("Newfoundland and Labrador"),
	Saskatchewan("Saskatchewan"),
	BritishColumbia("British Columbia"),
	NorthWestTerritories("Northwest Territories"),
	Yukon("Yukon"),
	Nunavut("Nunavut");
	
	private String name;

	private Province(String name) {
		this.name = name;
	}
	
	public static boolean isProvince(String name) {
		for(Province p : values()) {
			if(p.name.equals(name)) {
				return true;
			}
		}
		return false;
	}
}
