package _2xb3.finalProject.controller;

import java.util.Comparator;
import java.util.List;

import com.google.gson.Gson;

import _2xb3.finalProject.model.AlchemistModel;
import _2xb3.finalProject.model.Grant;
import _2xb3.finalProject.model.GrantColumn;
import _2xb3.finalProject.utils.Series;
import _2xb3.finalProject.utils.SeriesData;
import _2xb3.finalProject.utils.TimSort;
import spark.Filter;
import spark.Request;
import spark.Response;
import spark.Spark;

/**
 * Note: This class uses the GSON library to convert Java objects to JSON Strings.
 */
public class AlchemistController {

	private AlchemistModel model;

	/**
	 * Constructor for the Alchemist Controller.
	 *
	 * This constructor boots up a RESTful server, which acts as the API
	 * provider.
	 *
	 * The Spark library is used to create the HTTP Server.
	 */
	public AlchemistController() {
		this.model = new AlchemistModel();
		Gson gson = new Gson();

		// Server port
		Spark.port(4567);

		/* Modifies CORS rules. (Cross-Origin-Requests).
		 *
		 * Cross Origin Requests refer to requests that are outside of the server's
		 * domain. i.e. if the server is running on localhost, then requests that do
		 * not originate from localhost are either allowed or rejected, based on the
		 * CORS rules
		 */
		Spark.before(new Filter() {
			@Override
			public void handle(Request request, Response response) {
				response.header("Access-Control-Allow-Origin", "*");
				response.header("Access-Control-Request-Method", "*");
				response.header("Access-Control-Allow-Headers", "*");
			}
		});

		/*
			Sets all requests to use UTF-8
		*/
		Spark.before((req, res) -> {
			res.type("application/json; charset=UTF-8");
		});

		// compares two sets of data by name
		Comparator<SeriesData> seriesDataComparator = (sd1, sd2) -> {
			if(sd1.name.equals("Other")) return Integer.MIN_VALUE;
			else return -1*sd1.name.compareTo(sd2.name);
		};

		/**
		 * GET endpoint which generates a series of data based on the following parameters
		 *	@param :column one of the columns defined in {@link GrantColumn}
		 *	@param :object the string/integer to search for in the column
		 *	@param :x column to use as the x-axis for resulting data
		 *	@param :y column to use as the y-axis for the resulting data
		 */
		Spark.get("/api/search/:column/:object/generate/:x/:y", (req, res) -> {
			GrantColumn column = GrantColumn.get(req.params(":column").charAt(0));
			Object searchFor = req.params(":object");

			List<Grant> grants = model.getGrantDatabase().getGrantsFor(column, searchFor);

			GrantColumn x = GrantColumn.get(req.params(":x").charAt(0));
			GrantColumn y = GrantColumn.get(req.params(":y").charAt(0));

			System.out.println("Generating series for '" + (String) searchFor + "' in column '" + column.toString() +"' using x-axis '" + x.toString() + "' and y-axis '" + y.toString() + "'");
			Series s = model.getGrantDatabase().generateSeries(grants, x, y);
			s.project();
			s.optimizeData();
			s.data = TimSort.sort(s.data, (Comparator<SeriesData>) seriesDataComparator);
			return gson.toJson(s.data);
		});

		/**
		 * GET endpoint which returns all data in a column which match a specific string/integer
		 *	@param :column the grant column to search in
		 *	@param :object the string/integer to search for
		 */
		Spark.get("/api/get/:column/:object", (req, res) -> {
			GrantColumn column = GrantColumn.get(req.params(":column").charAt(0));
			Object searchFor = req.params(":object");
			System.out.println("Requested '" + (String) searchFor + "' in column '" + column.toString() + "'");

			List<Grant> grants = model.getGrantDatabase().getGrantsFor(column, searchFor);
			if (grants != null)
				return gson.toJson(grants);

			return "{'error':'Could not get objects.'}";
		});

		/**
		 * GET endpoint which returns the string "Success".
		 *	Can be used as a health check.
		 */
		Spark.get("/api/ping", (req, res) -> {
			System.out.println("Requested Ping.");
			return gson.toJson("Success");
		});
	}

	public AlchemistModel getModel() {
		return model;
	}

	/**
	 * Program Entry Point
	 */
	public static void main(String[] args) {
		new AlchemistController();
		System.out.println("=== Server Started ===");
	}

}
