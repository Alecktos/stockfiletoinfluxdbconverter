package com.alecktos.stockfiletoinfluxconverter;

import com.alecktos.misc.DateTime;
import com.alecktos.misc.FileHandler;
import com.alecktos.misc.InfluxdbDAO;
import com.alecktos.misc.LineFileReader;
import com.alecktos.misc.logger.Logger;

import java.util.HashMap;
import java.util.List;

import static java.lang.String.format;

public class Main {

	public static void main(String[] args) {
		LineFileReader lineFileReader = new LineFileReader();
		List<String> lines = lineFileReader.getLinesFromFile(
				args[0]
		);

		System.out.println(format("Lines in file: %s", lines.size()));

		final String measurement = args.length >= 2 ? args[1] : "test_measurement";
		final String dbName = args.length >= 3 ? args[2] : "test_db";

		final InfluxdbDAO influxdbDAO = new InfluxdbDAO();

		lines.remove(0);
		lines.stream().forEach(line -> {
			StockFileLineExtractor stockFileLineExtractor = new StockFileLineExtractor(line);


			HashMap<String, Object> fields = new HashMap<String, Object>() {{
				put("stock_value", stockFileLineExtractor.getPriceFromRow());
			}};

			influxdbDAO.writeInflux(
					fields,
					DateTime.createFromTimeStamp(stockFileLineExtractor.getTimeStamp()),
					measurement,
					dbName
			);
		});
	}
}
