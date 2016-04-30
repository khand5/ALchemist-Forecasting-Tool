package _2xb3.finalProject.utils;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This is a utility class which helps manage files.
 */
public class FileUtils {

	/**
	 * Gets the lines in a file as a list.
	 * @param file Location of file on disk.
	 * @return A list of lines in the file, or an empty list if an error occured.
	 */
	public static List<String> read(String file) {
		List<String> lines = new ArrayList<String>();
		
		Scanner sc;
		try {
			InputStreamReader is = new InputStreamReader(new FileInputStream(file), "UTF-8");
			sc = new Scanner(is);
			
			while(sc.hasNext()) {
				lines.add(sc.nextLine());
			}
			
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lines;
	}
	
}
