package _2xb3.finalProject.tests;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import _2xb3.finalProject.model.Grant;
import _2xb3.finalProject.model.GrantColumn;
import _2xb3.finalProject.utils.TimSort;

public class TestFunctionality {
	Grant testGrant,testGrant2;
	Integer[] randList;
	Comparator<Integer> intCompare,intCompare2;
	@Before
	public void setUp() throws Exception {
		testGrant = new Grant(1234, "Science", "McMaster", "John Doe", "Ontario", "Algorithm Design",2016, 35000);
		testGrant2 = new Grant(1233, "AScience", "AMcMaster", "AJohn Doe", "AOntario", "AAlgorithm Design",2015, 34000);
		randList = new Integer[(int) Math.round(Math.random()*100+30)];
		for (int i = 0 ; i < randList.length;i++){
			randList[i] = (int) Math.round((Math.random()*1000000));
		}
		
		intCompare =  new Comparator<Integer>() {
			@Override
			public int compare(Integer i1, Integer i2) {
				return Integer.compare(i1, i2);
			}
		};
		intCompare2 =  new Comparator<Integer>() {
			@Override
			public int compare(Integer i1, Integer i2) {
				return Integer.compare(i1, i2)*-1;
			}
		};
	}

	/**
	 * Test the Grant ADT for field accuracy
	 */
	@Test
	public void testGrant() {
		try{
			assertTrue((float)testGrant.get(GrantColumn.Amount)==35000);
			assertTrue((int)testGrant.get(GrantColumn.Year)==2016);
			assertTrue(((String)testGrant.get(GrantColumn.Subject)).equals("Algorithm Design"));
			assertTrue(((String)testGrant.get(GrantColumn.Province)).equals("Ontario"));
			assertTrue(((String)testGrant.get(GrantColumn.Professor)).equals("John Doe"));
			assertTrue(((String)testGrant.get(GrantColumn.Organization)).equals("McMaster"));
			assertTrue(((String)testGrant.get(GrantColumn.FieldOfResearch)).equals("Science"));
			assertTrue(((int)testGrant.get(GrantColumn.id))==1234);
		}catch(Exception e){
			fail("Failed to cast to appropriate type");
		}
	}
	
	/**
	 * Test the comparators for accurate comparisons
	 */
	@Test
	public void testComparators(){
		int current = 0;
		//greater than
		current = GrantColumn.id.getComparator().compare(testGrant, testGrant2);
		assertTrue(current > 0);
		current = GrantColumn.Amount.getComparator().compare(testGrant, testGrant2);
		assertTrue(current > 0);
		current = GrantColumn.Year.getComparator().compare(testGrant, testGrant2);
		assertTrue(current > 0);
		current = GrantColumn.Subject.getComparator().compare(testGrant, testGrant2);
		assertTrue(current > 0);
		current = GrantColumn.Province.getComparator().compare(testGrant, testGrant2);
		assertTrue(current > 0);
		current = GrantColumn.Professor.getComparator().compare(testGrant, testGrant2);
		assertTrue(current > 0);
		current = GrantColumn.Organization.getComparator().compare(testGrant, testGrant2);
		assertTrue(current > 0);
		current = GrantColumn.FieldOfResearch.getComparator().compare(testGrant, testGrant2);
		assertTrue(current > 0);
		
		//equals
		current = GrantColumn.id.getComparator().compare(testGrant, testGrant);
		assertTrue(current == 0);
		current = GrantColumn.Amount.getComparator().compare(testGrant, testGrant);
		assertTrue(current == 0);
		current = GrantColumn.Year.getComparator().compare(testGrant, testGrant);
		assertTrue(current == 0);
		current = GrantColumn.Subject.getComparator().compare(testGrant, testGrant);
		assertTrue(current == 0);
		current = GrantColumn.Province.getComparator().compare(testGrant, testGrant);
		assertTrue(current == 0);
		current = GrantColumn.Professor.getComparator().compare(testGrant, testGrant);
		assertTrue(current == 0);
		current = GrantColumn.Organization.getComparator().compare(testGrant, testGrant);
		assertTrue(current == 0);
		current = GrantColumn.FieldOfResearch.getComparator().compare(testGrant, testGrant);
		assertTrue(current == 0);
		
		//lesser than
		current = GrantColumn.id.getComparator().compare(testGrant2, testGrant);
		assertTrue(current < 0);
		current = GrantColumn.Amount.getComparator().compare(testGrant2, testGrant);
		assertTrue(current < 0);
		current = GrantColumn.Year.getComparator().compare(testGrant2, testGrant);
		assertTrue(current < 0);
		current = GrantColumn.Subject.getComparator().compare(testGrant2, testGrant);
		assertTrue(current < 0);
		current = GrantColumn.Province.getComparator().compare(testGrant2, testGrant);
		assertTrue(current < 0);
		current = GrantColumn.Professor.getComparator().compare(testGrant2, testGrant);
		assertTrue(current < 0);
		current = GrantColumn.Organization.getComparator().compare(testGrant2, testGrant);
		assertTrue(current < 0);
		current = GrantColumn.FieldOfResearch.getComparator().compare(testGrant2, testGrant);
		assertTrue(current < 0);
	}
	
	/**
	 * Test the TimSort for accurate sorting of items
	 */
	@Test
	public void testTimSort(){
		List<Integer> result = TimSort.sort(Arrays.asList(randList),intCompare);
		for(int i = 0; i < result.size()-1;i++){
			assertTrue(result.get(i).compareTo(result.get(i+1))<=0);
		}
		result = TimSort.sort(Arrays.asList(randList),intCompare2);
		for(int i = 0; i < result.size()-1;i++){
			assertTrue(result.get(i).compareTo(result.get(i+1))>=0);
		}
	}
	
	

}
