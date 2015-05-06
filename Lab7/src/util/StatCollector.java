package util;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class StatCollector {

	private static Map<Integer, Map<String, Float>> data;

	static {
		data = new HashMap<Integer, Map<String, Float>>();
	}

	public synchronized static void addDataPoint(int idx, String name, float value) {
		Map<String, Float> dataMap = data.get(idx);
		if (dataMap == null) {
			dataMap = new HashMap<>();
			data.put(idx, dataMap);
		}

		dataMap.put(name, value);
	}

	public static void flush() {
		try (FileWriter writer = new FileWriter("data.csv")) {

			Set<Integer> indices = data.keySet();

			if (indices.size() == 0)
				return;

			List<String> keySet = new ArrayList<>(data.get(0).keySet());
			String[] header = new String[keySet.size()];
			keySet.toArray(header);

			//System.out.println(String.join(";", header));
			writer.append(String.join(";", header));
			writer.append("\n");

			for (Integer idx : indices) {
				Map<String, Float> dataMap = data.get(idx);
				List<Float> values = new ArrayList<>();

				keySet.stream().map(dataMap::get).forEach(values::add);

				String[] valuesArray = new String[values.size()];
				values.stream().map(f -> f == null ? "0" : f.toString())
						.collect(Collectors.toList()).toArray(valuesArray);

				//System.out.println(String.join(";", valuesArray));
				writer.append(String.join(";", valuesArray));
				writer.append("\n");
			}

			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
