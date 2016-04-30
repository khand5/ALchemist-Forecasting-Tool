package _2xb3.finalProject.model;

/**
 * This class defines the model for the Alchemist app.
 */
public class AlchemistModel {

	private GrantDatabase grantDB;
	private long lastDBLoadTimeMillis;
	
	/**
	 * This constructor measures the amount of time it took for the database to load.
	 */
	public AlchemistModel(){
		lastDBLoadTimeMillis = System.currentTimeMillis();
		grantDB = new GrantDatabase();
		lastDBLoadTimeMillis = System.currentTimeMillis() - lastDBLoadTimeMillis;
	}
	
	/**
	 * Accessor method which returns the GrantDatabase object.
	 * @return GrantDatabase object which manages all grants.
	 * @see GrantDatabase
	 */
	public GrantDatabase getGrantDatabase(){
		return this.grantDB;
	}	
	
	/**
	 * Accesses the time it took to load the database in seconds.
	 * @return
	 */
	public float getDBLoadTimeSeconds() {
		return lastDBLoadTimeMillis / 1000.0F;
	}
	
}