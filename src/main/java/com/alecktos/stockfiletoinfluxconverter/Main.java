package com.alecktos.stockfiletoinfluxconverter;

import com.alecktos.misc.DateTime;
import com.alecktos.misc.FileHandler;
import com.alecktos.misc.InfluxdbDAO;
import com.alecktos.misc.LineFileReader;

import java.util.List;

public class Main {

	public static void main(String[] args) {
		LineFileReader lineFileReader = new LineFileReader();
		List<String> lines = lineFileReader.getLinesFromFile(
				args[0]
		);

		final InfluxdbDAO influxdbDAO = new InfluxdbDAO();

		lines.remove(0);
		lines.stream().forEach(line -> {
			StockFileLineExtractor stockFileLineExtractor = new StockFileLineExtractor(line);
			//stockFileLineExtractor.
			influxdbDAO.writeInflux(
					stockFileLineExtractor.getPriceFromRow(),
					DateTime.createFromTimeStamp(stockFileLineExtractor.getTimeStamp()),
					"stocks-test"
			);
		});
	}
}
