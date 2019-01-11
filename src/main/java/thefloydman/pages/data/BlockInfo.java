package thefloydman.pages.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.io.Files;

import net.minecraft.util.ResourceLocation;
import thefloydman.pages.util.Reference;

public class BlockInfo {

	public void checkForConfig(File configDir) throws IOException, URISyntaxException {
		new File(configDir.getAbsolutePath() + "/pages").mkdirs();
		createFileIfVoid("assets/pages/blocks.csv",
				configDir.getAbsolutePath() + "/pages/blocks_" + Reference.VERSION + ".csv");
		createFileIfVoid("assets/pages/blocks_custom.csv",
				configDir.getAbsolutePath() + "/pages/blocks_custom.csv");
	}

	private void createFileIfVoid(String fileFrom, String pathTo) throws IOException {
		File file = new File(pathTo);

		if (!file.exists() || file.isDirectory()) {

			FileWriter writer = new FileWriter(file);
			List<List<String>> blockList = getDefaultBlockInfoFromAsset(fileFrom);
			boolean firstTime = true;
			for (int i = 0; i < blockList.size(); i++) {
				String line = blockList.get(i).stream().collect(Collectors.joining(","));
				if (firstTime == false) {
					writer.write("\n");
				}
				writer.write(line);
				firstTime = false;
			}
			writer.close();

		}
	}

	private List<List<String>> getDefaultBlockInfoFromAsset(String fileFrom) throws IOException {
		List<List<String>> records = new ArrayList<>();
		InputStream in = getClass().getClassLoader().getResourceAsStream(fileFrom);
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line = reader.readLine()) != null) {
			String[] values = line.split(",");
			records.add(Arrays.asList(values));
		}
		return records;

	}

	public static List<List<String>> getBlockInfoFromConfig(File configDir) throws IOException {

		File fileBlocks = new File(configDir.getAbsolutePath() + "/pages/blocks_" + Reference.VERSION + ".csv");
		File fileCustomBlocks = new File(configDir.getAbsolutePath() + "/pages/blocks_custom.csv");

		List<List<String>> records = new ArrayList<>();

		BufferedReader readerOne = new BufferedReader(new FileReader(fileBlocks));
		String lineOne;
		while ((lineOne = readerOne.readLine()) != null) {
			String[] values = lineOne.split(",");
			records.add(Arrays.asList(values));
		}
		readerOne.close();

		BufferedReader readerTwo = new BufferedReader(new FileReader(fileCustomBlocks));
		boolean firstLineSkipped = false;
		String lineTwo;
		while ((lineTwo = readerTwo.readLine()) != null) {
			if (firstLineSkipped == false) {
				firstLineSkipped = true;
				continue;
			}
			String[] values = lineTwo.split(",");
			records.add(Arrays.asList(values));
		}
		readerTwo.close();

		return records;

	}

}
