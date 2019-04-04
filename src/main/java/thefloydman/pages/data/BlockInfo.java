package thefloydman.pages.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.common.io.Files;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import thefloydman.pages.util.Reference;

public class BlockInfo {

	public static String[] fileNames = {
			"vanilla",
			"appliedenergistics2",
			"biomesoplenty",
			"custom"
			};

	public void checkForConfig(File configDir) throws IOException, URISyntaxException {
		new File(configDir.getAbsolutePath().replaceAll("\\\\", "/") + "/pages").mkdirs();

		for (String name : fileNames) {
			createFileIfVoid("assets/pages/blocks_" + name + ".json",
					configDir.getAbsolutePath().replaceAll("\\\\", "/") + "/pages/blocks_" + name + ".json");
		}
	}

	private void createFileIfVoid(String pathFrom, String pathTo) throws IOException {
		File fileTo = new File(pathTo);

		if (!fileTo.exists() || fileTo.isDirectory()) {

			InputStream inputStream = null;
			OutputStream outputStream = null;
			try {
				inputStream = this.getClass().getClassLoader().getResourceAsStream(pathFrom);
				outputStream = new FileOutputStream(fileTo);
				byte[] buffer = new byte[1024];
				int length;
				while ((length = inputStream.read(buffer)) > 0) {
					outputStream.write(buffer, 0, length);
				}
				inputStream.close();
				outputStream.close();
			} finally {
			}

		}
	}

	public static JsonArray getBlockInfoFromConfigJson(File configDir) {
		JsonParser parser = new JsonParser();
		JsonArray jArray = new JsonArray();
		for (String name : fileNames) {
			File file = new File(
					configDir.getAbsolutePath().replaceAll("\\\\", "/") + "/pages/blocks_" + name + ".json");
			try {
				jArray.addAll((JsonArray) parser.parse(new FileReader(file)));
			} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return jArray;
	}

}
