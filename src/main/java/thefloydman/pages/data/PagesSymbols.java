package thefloydman.pages.data;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.xcompwiz.mystcraft.api.symbol.BlockCategory;
import com.xcompwiz.mystcraft.api.symbol.BlockDescriptor;
import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;
import com.xcompwiz.mystcraft.grammar.GrammarGenerator;
import com.xcompwiz.mystcraft.instability.InstabilityBlockManager;
import com.xcompwiz.mystcraft.item.ItemPage;
import com.xcompwiz.mystcraft.symbol.SymbolManager;
import com.xcompwiz.mystcraft.world.AgeController;
import com.xcompwiz.util.CollectionUtils;

import thefloydman.pages.symbol.PagesSymbolBase;
import thefloydman.pages.symbol.modifiers.SymbolBlock;

public class PagesSymbols {

	public static final Logger LOGGER = LogManager.getLogger();
	static String modId = "";
	static String blockId = "";
	private static String subId = "";
	private static String localizationOverride = "";
	private static String localizationNonstandard = "";

	public static <V, T> void initialize(File configDir) {

		JsonArray blockArray = BlockInfo.getBlockInfoFromConfigJson(configDir);

		for (int i = 0; i < blockArray.size(); i++) {
			JsonObject blockObject = blockArray.get(i).getAsJsonObject();
			modId = "minecraft";
			if (blockObject.get("mod_id") != null) {
				if (!blockObject.get("mod_id").getAsString().equals("")) {
					modId = blockObject.get("mod_id").getAsString();
				}
			}
			if (!Loader.isModLoaded(modId)) {
				continue;
			}
			String word = blockObject.get("word").getAsString();
			int cardRank = blockObject.get("loot_weight").getAsInt();
			blockId = blockObject.get("block_id").getAsString();
			String symbolId = blockObject.get("symbol_id").getAsString();
			if (blockObject.get("localization_nonstandard") != null) {
				localizationNonstandard = blockObject.get("localization_nonstandard").getAsString();
				LOGGER.info("Using nonstandard localization for block " + modId + ":" + blockId + " - "
						+ localizationNonstandard);
			}
			if (blockObject.get("localization_override") != null) {
				localizationOverride = blockObject.get("localization_override").getAsString();
				LOGGER.info("Using localization override for block " + modId + ":" + blockId + " - "
						+ localizationOverride);
			}
			if (blockObject.get("sub_id") != null) {
				subId = blockObject.get("sub_id").getAsString();
				LOGGER.info("Adding sub ID for block " + modId + ":" + blockId + " - " + subId);
			}
			IBlockState blockState = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(modId, blockId))
					.getDefaultState();
			// Add properties to blockstate.
			if (blockObject.get("properties") != null) {
				JsonArray propertiesArray = blockObject.get("properties").getAsJsonArray();
				for (int j = 0; j < propertiesArray.size(); j++) {
					JsonObject propertyObject = propertiesArray.get(j).getAsJsonObject();
					String propertyId = propertyObject.get("id").getAsString();
					String propertyValue = propertyObject.get("value").getAsString();
					IProperty property = parseProperty(blockState, propertyId);
					Comparable value = null;
					Object[] possibleValues = property.getAllowedValues().toArray();
					String acceptableValues = "";
					for (Object singleValue : possibleValues) {
						if (singleValue.toString().equals(propertyValue)) {
							value = (Comparable) singleValue;
							break;
						}
					}
					if (value == null) {
						for (Object singleValue : possibleValues) {
							acceptableValues += singleValue.toString().toLowerCase() + ", ";
							if (singleValue.toString().toLowerCase().equals(propertyValue.toLowerCase())) {
								value = (Comparable) singleValue;
								break;
							}
						}
						acceptableValues = acceptableValues.substring(0, acceptableValues.length() - 2);
					}
					if (value == null) {
						LOGGER.info("Cannot apply value \"" + propertyValue + "\" to property \"" + propertyId
								+ "\". Leaving in default state.");
						LOGGER.info("Acceptable values are: " + acceptableValues);
						continue;
					}
					blockState = blockState.withProperty(property, value);
				}
			}
			LOGGER.info("Registering page for blockstate \"" + blockState + "\".");
			BlockSymbol page = BlockSymbol.create(word, cardRank, blockState, symbolId);
			page.register();

			// Add blockstate to grammar categories.
			JsonArray categoryArray = blockObject.get("categories").getAsJsonArray();
			for (int j = 0; j < categoryArray.size(); j++) {
				JsonObject categoryObject = categoryArray.get(j).getAsJsonObject();
				String categoryModId = "mystcraft";
				if (categoryObject.get("mod_id") != null) {
					if (!categoryObject.get("mod_id").getAsString().trim().equals("")) {
						categoryModId = categoryObject.get("mod_id").getAsString();
					}
				}
				String categoryId = categoryObject.get("category_id").getAsString();
				int weight = categoryObject.get("weight").getAsInt();
				BlockCategory category = BlockCategory
						.getBlockCategory(new ResourceLocation(categoryModId, categoryId));
				page.add(category, weight);
			}

			// Add block instability.
			if (blockObject.get("instability_base") != null && blockObject.get("instability_exposed") != null) {
				LOGGER.info("Registering instability values for blockstate \"" + blockState.toString() + "\".");
				float instabilityFactorBase = blockObject.get("instability_base").getAsFloat();
				float instabilityFactorExposed = blockObject.get("instability_exposed").getAsFloat();
				InstabilityBlockManager.setInstabilityFactors(blockState, instabilityFactorExposed,
						instabilityFactorBase);
			}
		}
	}

	protected static IProperty parseProperty(IBlockState state, String propertyName) {
		Collection<IProperty<?>> propertyCollection = state.getPropertyKeys();
		for (IProperty<?> property : propertyCollection) {
			if (property.getName().equals(propertyName)) {
				return property;
			}
		}
		return null;
	}

	public static class BlockSymbol {
		private BlockDescriptor descriptor;
		private SymbolBlock symbol;

		private BlockSymbol(final BlockDescriptor descriptor, final SymbolBlock symbol) {
			this.descriptor = descriptor;
			this.symbol = symbol;
		}

		private BlockSymbol() {
		}

		public BlockSymbol add(final BlockCategory cat, final Integer rank) {
			if (this.descriptor == null || this.symbol == null) {
				return this;
			}
			if (!this.descriptor.isUsable(cat)) {
				this.descriptor.setUsable(cat, true);
				this.symbol.addRule(new GrammarGenerator.Rule(cat.getGrammarBinding(),
						CollectionUtils.buildList(this.symbol.getRegistryName()), rank));
			}
			return this;
		}

		public static BlockSymbol create(final String word, final int cardrank, final IBlockState blockstate,
				final String symbolId) {
			final BlockDescriptor descriptor = new BlockDescriptor(blockstate);
			final SymbolBlock symbol = new SymbolBlock(descriptor, word, modId, blockId, subId, localizationOverride,
					symbolId, localizationNonstandard);
			if (SymbolManager.hasBinding(symbol.getRegistryName())) {
				LOGGER.info("Cannot register symbol because it has already been registered.");
				return new BlockSymbol();
			}
			symbol.setWords(
					new String[] { "Transform", "Constraint", word, symbol.getRegistryName().getResourcePath() });
			symbol.setCardRank(cardrank);
			return new BlockSymbol(descriptor, symbol);
		}

		public IAgeSymbol getSymbol() {
			return this.symbol;
		}

		private BlockSymbol register() {
			if (this.symbol != null) {
				SymbolManager.tryAddSymbol(this.symbol);
			}
			return this;
		}
	}
}
