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

import thefloydman.pages.util.Reference;

public abstract class BlockInfo {

	public static void checkForConfig(File configDir) throws IOException, URISyntaxException {
		new File(configDir.getAbsolutePath() + "\\pages").mkdirs();
		createFileIfVoid("assets/pages/blocks.csv",
				configDir.getAbsolutePath() + "\\pages\\blocks_" + Reference.VERSION + ".csv");
		createFileIfVoid("assets/pages/blocks_custom.csv", configDir.getAbsolutePath() + "\\pages\\blocks_custom.csv");
	}

	private static void createFileIfVoid(String pathFrom, String pathTo) throws IOException, URISyntaxException {
		File file = new File(pathTo);
		if (!file.exists() || file.isDirectory()) {
			Files.copy(new File(BlockInfo.class.getClassLoader().getResource(pathFrom).toURI()), file);
		}
	}

	public static List<List<String>> getBlockInfoFromConfig(File configDir) throws IOException {

		File fileBlocks = new File(configDir.getAbsolutePath() + "\\pages\\blocks_" + Reference.VERSION + ".csv");
		File fileCustomBlocks = new File(configDir.getAbsolutePath() + "\\pages\\blocks_custom.csv");

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
