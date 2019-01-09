package thefloydman.pages.data;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import com.xcompwiz.mystcraft.api.symbol.BlockCategory;
import com.xcompwiz.mystcraft.api.symbol.BlockDescriptor;
import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;
import com.xcompwiz.mystcraft.grammar.GrammarGenerator;
import com.xcompwiz.mystcraft.symbol.SymbolManager;
import com.xcompwiz.mystcraft.world.AgeController;
import com.xcompwiz.util.CollectionUtils;

import thefloydman.pages.logging.LoggerUtils;
import thefloydman.pages.symbol.PagesSymbolBase;
import thefloydman.pages.symbol.modifiers.SymbolBlock;

public class PagesSymbols {

	private static String subID = "";
	private static String localizationOverride = "";

	public static void initialize(File configDir) {

		List<List<String>> blockList = null;
		try {
			blockList = new BlockInfo().getBlockInfoFromConfig(configDir);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		for (int i = 1; i < blockList.size(); i++) {
			boolean enabled = Boolean.valueOf(blockList.get(i).get(0));
			String modID = String.valueOf(blockList.get(i).get(1));
			if (modID.trim().equals("") || modID == null) {
				modID = "minecraft";
			}
			String word = String.valueOf(blockList.get(i).get(2));
			int cardRank = Integer.valueOf(blockList.get(i).get(3));
			String blockId = String.valueOf(blockList.get(i).get(4));
			int meta = Integer.valueOf(blockList.get(i).get(5));
			
			try {
				subID = String.valueOf(blockList.get(i).get(6));
			} catch (ArrayIndexOutOfBoundsException e) {
			}
			try {
				localizationOverride = String.valueOf(blockList.get(i).get(7));
			} catch (ArrayIndexOutOfBoundsException e) {
			}
			int catStart = 8;
			if (!Loader.isModLoaded(modID) || enabled == false) {
				continue;
			}
			LoggerUtils.info("Adding page for block " + modID + ":" + blockId, new Object[0]);
			BlockSymbol page = BlockSymbol.createPage(word, cardRank, Block.getBlockFromName(modID + ":" + blockId),
					meta);
			page.register();
			for (int cat = catStart; cat < blockList.get(i).size(); cat += 2) {
				if (blockList.get(i).get(cat).equals("")) {
					break;
				}

				Field categoryField = null;
				try {
					String catLower = String.valueOf(blockList.get(i).get(cat));
					String catUpper = catLower.toUpperCase();
					categoryField = Class.forName("com.xcompwiz.mystcraft.api.symbol.BlockCategory").getField(catUpper);
				} catch (NoSuchFieldException | SecurityException | ClassNotFoundException e) {
					e.printStackTrace();
				}
				BlockCategory category = null;
				try {
					category = (BlockCategory) categoryField.get(category);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}

				page.add(category, Integer.valueOf(blockList.get(i).get(cat + 1)));
			}
		}
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

		public static BlockSymbol create(final String word, final int cardrank, final IBlockState blockstate) {
			final BlockDescriptor descriptor = new BlockDescriptor(blockstate);
			final SymbolBlock symbol = new SymbolBlock(descriptor, word, subID, localizationOverride);
			if (SymbolManager.hasBinding(symbol.getRegistryName())) {
				LoggerUtils.info("Cannot register symbol because it has already been registered.", new Object[0]);
				return new BlockSymbol();
			}
			symbol.setCardRank(cardrank);
			return new BlockSymbol(descriptor, symbol);
		}

		private static BlockSymbol createPage(final String word, final int cardrank, final Block block,
				final int metadata) {
			final IBlockState state = block.getStateFromMeta(metadata);
			if (state == null) {
				return null;
			}
			return create(word, cardrank, state);
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
