package _2xb3.finalProject.model;

/**
 * This class describes the ADT for the rows of Grants in the CSV files.
 */
public class Grant {
	int id;
	
	String professorName;

	String province;
	String organizationName;

	String field;
	String subject;

	int year;
	float amount;

	/**
	 * Standard constructor which takes in all the columns in the csv files, as described by the GrantColumn class.
	 * @see GrantColumn
	 * 
	 * @param id ID of the Grant
	 * @param field Field of Research of the grant
	 * @param institutionName Institution/Organization name of the grant
	 * @param professorName Full name of the professor who was awarded the grant
	 * @param province Province in which the grant was awarded
	 * @param researchSubject Subject for which the grant was awarded
	 * @param year Year in which grant was awarded
	 * @param amount Amount of Canadian Dollars ($) awarded
	 */
	public Grant(int id, String field, String institutionName, String professorName, String province,
			String researchSubject, int year, float amount) {

		this.id = id;

		this.amount = amount;
		this.year = year;

		this.professorName = professorName;
		this.organizationName = institutionName;
		this.province = province;
		this.field = field;
		this.subject = researchSubject;
	}

	/**
	 * Accessor which gets the appropriate grant data based on a given column
	 * @param x Column in question
	 * @return Grant's data relating to the given column, or null.
	 */
	public Object get(GrantColumn x) {
		switch(x) {
		case Amount:
			return amount;
		case FieldOfResearch:
			return field;
		case Number:
			return "Number of Grants";
		case Organization:
			return organizationName;
		case Professor:
			return professorName;
		case Province:
			return province;
		case Subject:
			return subject;
		case Year:
			return year;
		case id:
			return id;
		
		}
		return null;
	}
	
}
