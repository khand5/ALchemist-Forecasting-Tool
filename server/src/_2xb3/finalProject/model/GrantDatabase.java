package _2xb3.finalProject.model;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import com.google.gson.Gson;

import _2xb3.finalProject.utils.FileUtils;
import _2xb3.finalProject.utils.Series;
import _2xb3.finalProject.utils.SeriesData;
import _2xb3.finalProject.utils.TimSort;

/**
 * This class describes the grant database which manages the grants. It loads,
 * sanitizes, optimizes, sorts, searches, projects, and analyzes the grants.
 * 
 * Uses the GSON library to convert Java objects to JSON Strings.
 */
public class GrantDatabase {

	private List<Grant> grants = new ArrayList<Grant>();
	private static final File OPTIMIZED_DB_FILE = new File("data/database.db");
	/**
	 * Standard constructor which loads and sanitizes the database files, then
	 * save them in an efficient format for faster loading in the future.
	 * 
	 * @param useBackup
	 *            If false, it will not load previously optimized dataset, but
	 *            reload and sanitize from scratch.
	 */
	public GrantDatabase() {
		Gson gson = new Gson();

		if (OPTIMIZED_DB_FILE.exists()) {
			Scanner sc;
			try {
				sc = new Scanner(OPTIMIZED_DB_FILE);

				while (sc.hasNext()) {
					String line = sc.nextLine();
					grants.add(gson.fromJson(line, Grant.class));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			List<Grant> grants = new ArrayList<Grant>();
			for (int i = 1991; i <= 2014; i++) {
				List<String> lines = FileUtils.read("data/NSERC_GRT_FYR{year}_AWARD.csv.xls".replace("{year}", "" + i));
				lines.remove(0);

				boolean errorLine;
				for (String line : lines) {
					errorLine = false;
					String[] data = line.split(",");

					String field = "";
					String institution = "";
					String professor = "";
					String province = "";
					String subject = "";
					int year = i;
					int amount = 0;
					int id = -1;

					try {
						List<String> sanitizedData = new ArrayList<String>();

						for (int j = 0; j < data.length; j++) {
							if (data[j].length() > 0 && data[j].charAt(0) == '"') {
								String cell = "";
								do {
									cell += data[j];
									if (cell.charAt(cell.length() - 1) == '"') {
										// found ending quote
										sanitizedData.add(cell);
										cell = null;
										break;
									}
								} while (++j < data.length);
								if (cell != null) {
									throw new IllegalStateException("Data begins with \" but doesn't end with \".");
								}
							} else {
								sanitizedData.add(data[j]);
							}
						}

						data = sanitizedData.toArray(new String[0]);

						field = data[26].replaceAll(",", ";");
						institution = data[4].replaceAll(",", ";");
						professor = data[1].replaceAll(",", ";");
						province = data[5].replaceAll(",", ";");

						if (province.startsWith("Qu") && province.endsWith("bec")) {
							province = "Quebec";
						}

						subject = data[28].replaceAll(",", ";");
						amount = Integer.parseInt(data[11]);

						id = Integer.parseInt(data[0]);
					} catch (Exception e) {
						errorLine = true;
					}
					if (!errorLine)
						grants.add(new Grant(id, field, institution, professor, province, subject, year, amount));
				}
			}
			grants = sort(GrantColumn.Professor.getComparator(), grants);
			this.grants = grants;
			
			save();
		}

	}

	/**
	 * Sorts the given list of grants based on a comparator.
	 * 
	 * @param comparator
	 *            Comparator to use for sorting
	 * @param grants
	 *            The list of grants to sort
	 * @return
	 */
	private List<Grant> sort(Comparator<Grant> comparator, List<Grant> grants) {
		return TimSort.sort(grants, comparator);
	}

	/**
	 * Searches an array of grants in a specific column for a specific grant.
	 * 	 
	 * This method finds the start and end index of all grants which match a
	 * particular grant in a particular grant column.
	 * 
	 * @param column
	 *            GrantColumn to search in
	 * @param array
	 *            Array of grants to search
	 * @param searchFor
	 *            Key to search for in given grant column
	 * @return start and end indexes of grants which fit the search, or null
	 */
	private int[] search(GrantColumn column, Grant[] array, Object searchFor) {
		Grant key = null;
		Comparator<Grant> comparator = column.getComparator();

		boolean validNumber = false;
		int val = -1;
		try {
			val = Integer.parseInt((String) searchFor);
			validNumber = true;
		} catch (Exception e) {
			validNumber = false;
		}

		switch (column) {
		case FieldOfResearch:
			key = new Grant(-1, (String) searchFor, "", "", "", "", 0, 0F);
			break;
		case Amount:
			if (validNumber) {
				key = new Grant(-1, "", "", "", "", "", 0, val);
				break;
			} else {
				return new int[0];
			}
		case Organization:
			key = new Grant(-1, "", (String) searchFor, "", "", "", 0, 0F);
			break;
		case Professor:
			key = new Grant(-1, "", "", (String) searchFor, "", "", 0, 0F);
			break;
		case Province:
			key = new Grant(-1, "", "", "", (String) searchFor, "", 0, 0F);
			break;
		case Subject:
			key = new Grant(-1, "", "", "", "", (String) searchFor, 0, 0F);
			break;
		case Year:
			if (validNumber) {
				key = new Grant(-1, (String) searchFor, "", "", "", "", Integer.parseInt((String) searchFor), 0F);
				break;
			} else {
				return new int[0];
			}
		case id:
			if (validNumber) {
				key = new Grant(Integer.parseInt((String) searchFor), "", "", "", "", "", 0, 0F);
				break;
			} else {
				return new int[0];
			}
		case Number:
			return new int[0];
		}
		
		if(key == null) return new int[0];
		
		int aroundIndex = Arrays.binarySearch(array, key, comparator);
		if (aroundIndex < 0)
			return null;

		int startIndex = aroundIndex;
		while (comparator.compare(grants.get(startIndex), key) == 0) {
			if (--startIndex < 0) {
				startIndex = -1;
				break;
			}
		}
		startIndex++;

		if (aroundIndex == grants.size()) {
			return new int[] { startIndex, aroundIndex - 1 };
		}

		while (comparator.compare(grants.get(aroundIndex), key) == 0) {
			if (++aroundIndex == grants.size()) {
				return new int[] { startIndex, aroundIndex - 1 };
			}
		}
		aroundIndex--;
		return new int[] { startIndex, aroundIndex };
	}

	/**
	 * Obtains grants for a given column based on a search criteria.
	 * 
	 * @param column
	 *            Column to search in
	 * @param searchFor
	 *            Criteria to search for
	 * @return List of grants which fit the criterion
	 */
	public List<Grant> getGrantsFor(GrantColumn column, Object searchFor) {
		grants = sort(column.getComparator(), this.grants);

		Grant[] array = grants.toArray(new Grant[0]);

		List<Grant> list = new ArrayList<Grant>();

		int[] indicies = search(column, array, searchFor);

		if (indicies == null || indicies.length == 0) {
			return list;
		} else {
			for (int i = indicies[0]; i <= indicies[1]; i++) {
				list.add(array[i]);
			}
			return list;
		}
	}

	/**
	 * Saves the database in an efficient JSON format to a flat file.
	 * 
	 * @return true if successful, false if not successful
	 */
	private boolean save() {
		Gson gson = new Gson();

		String lineSeparator = System.getProperty("line.separator");

		try {
			FileWriter writer = new FileWriter(OPTIMIZED_DB_FILE);
			for (Grant g : grants) {
				writer.append(gson.toJson(g) + lineSeparator);
			}
			writer.close();
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	/**
	 * Accesses all the grants in the database.
	 * 
	 * @return list of grants
	 */
	public List<Grant> getGrants() {
		return grants;
	}

	/**
	 * Generates a Series of grants based on a given X,Y column scheme. This can
	 * be used to plot graphs.
	 * 
	 * @see Series
	 * @param grants
	 *            List of grants to generate series from
	 * @param x
	 *            Independent column
	 * @param y
	 *            Dependent column
	 * @return A series which can be plotted
	 */
	public Series generateSeries(List<Grant> grants, GrantColumn x, GrantColumn y) {
		// Sort grants according to Y, X.
		grants = sort(y.getComparator(), grants);
		grants = sort(x.getComparator(), grants);

		// Map X to Y
		Map<String, Integer> data = new HashMap<String, Integer>();

		for (Grant g : grants) {
			String key = g.get(x).toString();

			if (y == GrantColumn.Number) {
				if (data.get(key) == null) {
					data.put(key, 1);
				} else {
					data.put(key, data.get(key) + 1);
				}
			} else if (y == GrantColumn.Amount) {
				if (data.get(key) == null) {
					data.put(key, 0);
				} else {
					data.put(key, (int) (data.get(key) + g.amount));
				}
			} else {
				data.put(key, 0);
			}
		}

		Series s = new Series(x, y);

		for (Entry<String, Integer> entry : data.entrySet()) {

			SeriesData seriesData = new SeriesData(entry.getKey());
			seriesData.data.add(entry.getValue());
			if (x != GrantColumn.Province || Province.isProvince(entry.getKey())) {
				s.data.add(seriesData);
			}
		}
		
		return s;
	}
}
