package _2xb3.finalProject.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Michael Bitzos
 * @version 1.0
 */
public class GrantGraph {
	private Set<String> provinces;
	private HashMap<String,HashMap<String,Double>> adj;	//key =country/province/institute/year || value = (hashmap key =connection string ||value = weight)
	//takes in a db for initialization
	/**
	 *
	 * @param db Holds all in the grant information
	 */
	public GrantGraph(GrantDatabase db){
		//initialize hashmap ROOT as root
		provinces = new HashSet<String>();
		HashMap<String,Double> conn = new HashMap<String,Double>();
		adj = new HashMap<String,HashMap<String,Double>>();
		adj.put("ROOT", conn);
		for(Grant e: db.getGrants()){
			provinces.add(e.province);		//adds province to unqiue list of provinces.

			//updating ROOT with each year
			Double previousAmount= 0.0;

			previousAmount = getConnConn("ROOT", Integer.toString(e.year));
			adj.get("ROOT").put(Integer.toString(e.year),e.amount+previousAmount);

			//updating year connections

			previousAmount = getConnConn(Integer.toString(e.year),e.province+e.year);
			adj.get(Integer.toString(e.year)).put(e.province+e.year,e.amount+previousAmount);


			//updating provinces specific to year connections

			previousAmount = getConnConn(e.province+e.year,e.organizationName+e.year);
			adj.get(e.province+e.year).put(e.organizationName+e.year,e.amount+previousAmount);


			//each individual grant
//			adj.get(e.getOrganizationName()+e.getYear()+e.getProvince()).put(e.getProfessorName()+e.getYear(),e.getAmount()+previousAmount);
			previousAmount = getConnConn(e.organizationName+e.year,e.professorName+e.year);
			adj.get(e.organizationName+e.year).put(e.professorName+e.year,e.amount+previousAmount);

		}
	}

	/**
	 * @param year which year you want to find total grant amount for
	 * @return  total grants in that year
	 */
	public double getYearGrants(int year){
		return adj.get("ROOT").get(Integer.toString(year));
	}
	/**
	 *
	 * @param province the province you want to find the total grant amount for
	 * @param year The year you want to find the total grant amount for the province
	 * @return RETURNS THE TOTAL GRANTS OF THE PROVINCE,YEAR FOR MAPPING
	 */
	public double getProvinceGrants(String province, int year){
		if(adj.get(Integer.toString(year)).containsKey(province+year)){
			return adj.get(Integer.toString(year)).get(province+year);
		}
		else{
			return 0.0;
		}

	}
	/**
	 *
	 * @param year The year you want to find the total grant amount for
	 * @return Array[][] returns an array of provinces where each String[i] is a different province String[i][0] = name of province, String[i][1] = totalGrantAmount, String[i][2] = totalGrantAmountProvince/totalGrantAmount
	 */
	public String[][] getAllProvinceGrants(int year){
		String[][] results = new String[provinces.size()][2];
		@SuppressWarnings("unused")
		double totalGrants = getYearGrants(year);
		int i=0;
		for(String e: provinces){
			if(!e.isEmpty()){
				results[i][0] = e;
				i++;
			}
		}
		for(String[] e: results){
			//calculate percentage of grants over the entire country
			e[1] = Double.toString(getProvinceGrants(e[0],year));
			//e[2] = Double.toString(Double.parseDouble(e[1])/(double) totalGrants);
		}
		return results;
	}
	/**
	 * @param organization the specific place you want to find total grants for
	 * @param year the year you want to find the total grants for that organization
	 * @return the total grants of the organization
	 */
	public double getOrganizationGrants(String organization, int year){
		//looping through all provinces searching for organization
		for(String e: adj.get(year).keySet()){
			if(adj.get(e).containsKey(organization+year)){
				return adj.get(e).get(organization+year);
			}
		}
		return 0.0;
	}
	/**
	 * @param location The starting edge location (A)
	 * @return the connections connected to A
	 */
	public HashMap<String,Double> getConn(String location){
		if(adj.get(location) == null) {
			adj.put(location, new HashMap<String, Double>());
		}
		return adj.get(location);
	}
	/**
	 * @param adjLocation the one end of the edge
	 * @param location the other other of the end
	 * @return the grant amount between adjLocation and location
	 */
	public double getConnConn(String adjLocation, String location) {
		HashMap<String, Double> concon = getConn(adjLocation);

		if(concon.get(location) == null) {
			concon.put(location, 0.0);
		}
		return concon.get(location);
	}
}
