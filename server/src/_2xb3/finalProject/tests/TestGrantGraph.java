package _2xb3.finalProject.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import _2xb3.finalProject.controller.AlchemistController;
import _2xb3.finalProject.model.GrantGraph;

public class TestGrantGraph {

	private GrantGraph g;
	/*
	 * setting up database and graph to test on
	 */
	@Before
	public void setUp() throws Exception {
		AlchemistController c = new AlchemistController();
		g = new GrantGraph(c.getModel().getGrantDatabase());
	}

	@After
	public void tearDown() throws Exception {
	}

	/*
	 * Testing to see if the graph can find the sum of all the grants for that year
	 */
	@Test
	public void testGetYearGrants() {
		double test =0.0;
		//calulated real results taken from database
		double[] realResult = {451159747,434116337,417146970,477141539,526133116,537934451,554668568,614020215,695509585,763406534,809510608};
		int counter=0;
		for(int i = 2000;i<=2010;i++,counter++){
			test = g.getYearGrants(i);
			assertEquals(test,realResult[counter],test*0.05);	//5% error margin	
		}
		//testing a invalid year
		test = g.getYearGrants(1920);
		assertNull(test);
		
	}

	/*
	 * Testing if the graph can get the total sum of grants for a province in a specific year
	 */
	@Test
	public void testGetProvinceGrants() {
		double test=0.0;
		String[] provinces = {"Ontario","Quebec","Alberta","Manitoba","British Columbia","Nova Scotia"};	//testing some of the provinces
		double[] realResults = {266318645,153739008,64879685,15013926,102110102,18845819};
		for(int i = 0; i<provinces.length;i++){
			test =g.getProvinceGrants(provinces[i], 2014);
			assertEquals(test,realResults[i],test*0.5);
		}
		//testing to see that its fail-safe works
		test = g.getProvinceGrants("NFL", 2001);
		assertEquals(test,0.0,0);
	}

	
}
