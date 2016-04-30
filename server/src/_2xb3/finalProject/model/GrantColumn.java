package _2xb3.finalProject.model;

import java.util.Comparator;

// TEST CASE: Make sure all chars are unique
// TEST CASE: Make sure comparators compare what they're suppose to!
/**
 * Enumerated descriptions of all the columns in the grant database files.
 */
public enum GrantColumn {
	/**
	 * The ID column must be unique.
	 */
	id('i', new Comparator<Grant>() {
		@Override
		public int compare(Grant g1, Grant g2) {
			int id1 = g1.id;
			int id2 = g2.id;
			return Integer.compare(id1, id2);
		}
	}),
	
	FieldOfResearch('f', new Comparator<Grant>() {
		@Override
		public int compare(Grant g1, Grant g2) {
			String s1 = g1.field;
			String s2 = g2.field;
			
			return s1.compareTo(s2);
		}
	}),
	
	Organization('o', new Comparator<Grant>() {
		@Override
		public int compare(Grant g1, Grant g2) {
			String o1 = g1.organizationName;
			String o2 = g2.organizationName;
			
			return o1.compareTo(o2);
		}
	}),
	
	Professor('n', new Comparator<Grant>() {
		@Override
		public int compare(Grant g1, Grant g2) {
			String p1 = g1.professorName;
			String p2 = g2.professorName;
			
			return p1.compareTo(p2);
		}
	}),
	
	Province('p', new Comparator<Grant>() {
		@Override
		public int compare(Grant g1, Grant g2) {
			String p1 = g1.province;
			String p2 = g2.province;
			
			return p1.compareTo(p2);
		}
	}),
	
	Subject('s', new Comparator<Grant>() {
		@Override
		public int compare(Grant g1, Grant g2) {
			String s1 = g1.subject;
			String s2 = g2.subject;
			
			return s1.compareTo(s2);
		}
	}),
	
	Year('y', new Comparator<Grant>() {
		@Override
		public int compare(Grant g1, Grant g2) {
			int y1 = g1.year;
			int y2 = g2.year;

			return Integer.compare(y1, y2);
		}
	}),

	Amount('a', new Comparator<Grant>() {
		@Override
		public int compare(Grant o1, Grant o2) {
			float a1 = o1.amount;
			float a2 = o2.amount;
			
			return Float.compare(a1, a2);
		}
	}),
	
	Number('z', new Comparator<Grant>() {
		@Override
		public int compare(Grant arg0, Grant arg1) {
			return 0;
		}
	});
	
	private char abbr;
	private Comparator<Grant> comparator;

	/**
	 * Enumeration Constructor
	 * @param abbr Abbreviation for the column
	 * @param comparator Comparator for the column
	 */
	private GrantColumn(char abbr, Comparator<Grant> comparator) {
		this.abbr = abbr;
		this.comparator = comparator;
	}

	/**
	 * Method which returns a column based on abbreviation
	 * @param column Abbreviation of column
	 * @return Grant column which is associated with the abbreviation or null
	 */
	public static GrantColumn get(char column) {
		for(GrantColumn col : values()) {
			if(col.abbr == column)
				return col;
		}
		
		return null;
	}
	
	/**
	 * Accesses the comparator for the column
	 * @return Comparator which compares based on the column
	 */
	public Comparator<Grant> getComparator() {
		return comparator;
	}
	
}
