package _2xb3.finalProject.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import _2xb3.finalProject.model.GrantColumn;
import net.sourceforge.openforecast.DataPoint;
import net.sourceforge.openforecast.DataSet;
import net.sourceforge.openforecast.Forecaster;
import net.sourceforge.openforecast.ForecastingModel;
import net.sourceforge.openforecast.Observation;

/**
 * An object which describes data which correlates, and can be plotted.
 * 
 * This class utilizes the OpenForecast library to project series further.
 */
public class Series {

	private static final float OPTIMIZE_PERCENT = 0.05F;
	
	public List<SeriesData> data;
	private GrantColumn x;
	@SuppressWarnings("unused")
	private GrantColumn y;
	
	/**
	 * Creates a Series object which relates column X with Y.
	 * @param x Independent column
	 * @param y Dependent column
	 */
	public Series(GrantColumn x, GrantColumn y) {
		this.y = y;
		this.x = x;
		data = new ArrayList<SeriesData>();
	}
	
	/**
	 * Projects up to 2020, if the Independent column (X) is GrantColumn.YEAR
	 * 
	 * Uses the OpenForecast library.
	 */
	public void project() {
		if(x != GrantColumn.Year) return;
		
		DataSet dataset = new DataSet();
		
		for(SeriesData p : data) {
			if(p.name == null || !p.name.matches("-?\\d+(\\.\\d+)?")) continue;
			
			Observation o = new Observation(p.sumOfData());
			o.setIndependentValue("year", Integer.parseInt(p.name));
			
			dataset.add(o);
		}
		if(dataset.size() > 2) {
			ForecastingModel model = Forecaster.getBestForecast(dataset);
			
			DataPoint twentyFifteen = new Observation(0.0);
			twentyFifteen.setIndependentValue("year", 2015);
			
			DataPoint twentySixteen = new Observation(0.0);
			twentySixteen.setIndependentValue("year", 2016);
			
			DataPoint twentySeventeen = new Observation(0.0);
			twentySeventeen.setIndependentValue("year", 2017);
			
			DataPoint twentyEighteen = new Observation(0.0);
			twentyEighteen.setIndependentValue("year", 2018);

			DataPoint twentyNineteen = new Observation(0.0);
			twentyNineteen.setIndependentValue("year", 2019);
			
			DataPoint twentyTwenty = new Observation(0.0);
			twentyTwenty.setIndependentValue("year", 2020);
			
			SeriesData _2015 = new SeriesData("2015");
			_2015.data.add((int) model.forecast(twentyFifteen));
			SeriesData _2016 = new SeriesData("2016");
			_2016.data.add((int) model.forecast(twentySixteen));
			SeriesData _2017 = new SeriesData("2017");
			_2017.data.add((int) model.forecast(twentySeventeen));
			SeriesData _2018 = new SeriesData("2018");
			_2018.data.add((int) model.forecast(twentyEighteen));
			SeriesData _2019 = new SeriesData("2019");
			_2019.data.add((int) model.forecast(twentyNineteen));
			SeriesData _2020 = new SeriesData("2020");
			_2020.data.add((int) model.forecast(twentyTwenty));
			data.add(_2015);
			data.add(_2016);
			data.add(_2017);
			data.add(_2018);
			data.add(_2019);
			data.add(_2020);
		}
	}
	
	/**
	 * Optimizes the series such that entries that are very insignificant are grouped together in an "Other" category.
	 */
	public void optimizeData() {
		if(x == GrantColumn.Year || x == GrantColumn.Province) return;
		
		Optional<SeriesData> maxPoint = data.stream().max(new Comparator<SeriesData>() {
			@Override
			public int compare(SeriesData o1, SeriesData o2) {
				Integer i1 = o1.sumOfData();
				Integer i2 = o2.sumOfData();
				
				return i1.compareTo(i2);
			}
		});
		
		if(maxPoint.isPresent()) {
			SeriesData otherData = new SeriesData("Other");
			otherData.data.add(0, 0);
			
			SeriesData max = maxPoint.get();
			
			float optimizeMax = max.sumOfData() * OPTIMIZE_PERCENT;
			for(int i = 0; i < data.size(); i++) {
				SeriesData s = data.get(i);
				if(s.sumOfData() < optimizeMax) {
					int prev = otherData.data.get(0);
					otherData.data.set(0, prev + s.sumOfData());
					data.remove(i--);
				}
			}
			
			if(otherData.data.get(0).intValue() != 0) {
				data.add(otherData);
			}
		}
	}
	

}
