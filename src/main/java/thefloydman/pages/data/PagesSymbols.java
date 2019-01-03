package thefloydman.pages.data;

import java.lang.reflect.Field;
import java.util.List;

import com.xcompwiz.mystcraft.api.symbol.BlockCategory;
import com.xcompwiz.mystcraft.api.symbol.BlockDescriptor;
import com.xcompwiz.mystcraft.api.symbol.IAgeSymbol;
import com.xcompwiz.mystcraft.grammar.GrammarGenerator;
import com.xcompwiz.mystcraft.symbol.SymbolManager;
import com.xcompwiz.mystcraft.world.AgeController;
import com.xcompwiz.util.CollectionUtils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import thefloydman.pages.config.PagesConfig;
import thefloydman.pages.logging.LoggerUtils;
import thefloydman.pages.symbol.PagesSymbolBase;
import thefloydman.pages.symbol.modifiers.SymbolBlock;

public class PagesSymbols {

	public static void initialize() {

		List<List<String>> blockList = new BlockInfo().getList();

		for (int i = 1; i < blockList.size(); i++) {
			if (!Loader.isModLoaded(blockList.get(i).get(1))) {
				break;
			}
			if (blockList.get(i).get(0).toLowerCase().equals("false")) {
				continue;
			}
			LoggerUtils.info("Adding page for block " + blockList.get(i).get(1) + ":" + blockList.get(i).get(4),
					new Object[0]);
			BlockModifierContainerObject page = BlockModifierContainerObject.createPage(blockList.get(i).get(2),
					Integer.valueOf(blockList.get(i).get(3)),
					Block.getBlockFromName(blockList.get(i).get(1) + ":" + blockList.get(i).get(4)),
					Integer.valueOf(blockList.get(i).get(5)));
			page.register();
			for (int cat = 6; cat < blockList.get(i).size(); cat += 2) {
				if (blockList.get(i).get(cat).equals("")) {
					break;
				}

				Field categoryField = null;
				try {
					String catLower = blockList.get(i).get(cat);
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
		/*
		 * // Vanilla blocks. BlockModifierContainerObject .createPage("Wealth",
		 * PagesConfig.vanillaBlocks.cardRankBlockOfDiamond, Blocks.DIAMOND_BLOCK, 0)
		 * .register().add(BlockCategory.CRYSTAL, 8).add(BlockCategory.STRUCTURE,
		 * 8).add(BlockCategory.SOLID, 8); BlockModifierContainerObject
		 * .createPage("Greed", PagesConfig.vanillaBlocks.cardRankBlockOfEmerald,
		 * Blocks.EMERALD_BLOCK, 0) .register().add(BlockCategory.CRYSTAL,
		 * 9).add(BlockCategory.STRUCTURE, 9).add(BlockCategory.SOLID, 9); // RFTools
		 * blocks. if (Loader.isModLoaded("rftools")) { Field blockField = null; try {
		 * blockField = Class.forName("mcjty.rftools.blocks.ModBlocks").getField(
		 * "dimensionalShardBlock"); System.out.println(blockField.getName()); } catch
		 * (NoSuchFieldException | SecurityException | ClassNotFoundException e) {
		 * e.printStackTrace(); } Block block = null; try { block = (Block)
		 * blockField.get(block); } catch (IllegalArgumentException |
		 * IllegalAccessException e) { e.printStackTrace(); }
		 * BlockModifierContainerObject.createPage("Power",
		 * PagesConfig.rfBlocks.cardRankDimensionalShardOre, block, 0)
		 * .register().add(BlockCategory.STRUCTURE, 4).add(BlockCategory.SOLID, 4); } //
		 * Thaumcraft blocks. if (Loader.isModLoaded("thaumcraft")) {
		 * 
		 * }
		 */
	}

	public static class BlockModifierContainerObject {
		private BlockDescriptor descriptor;
		private SymbolBlock symbol;

		private BlockModifierContainerObject(final BlockDescriptor descriptor, final SymbolBlock symbol) {
			this.descriptor = descriptor;
			this.symbol = symbol;
		}

		private BlockModifierContainerObject() {
		}

		public BlockModifierContainerObject add(final BlockCategory cat, final Integer rank) {
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

		public static BlockModifierContainerObject create(final String word, final int cardrank,
				final IBlockState blockstate) {
			final BlockDescriptor descriptor = new BlockDescriptor(blockstate);
			final SymbolBlock symbol = new SymbolBlock(descriptor, word);
			if (SymbolManager.hasBinding(symbol.getRegistryName())) {
				LoggerUtils.info("Some mod is attempting to register a block symbol over an existing registration.",
						new Object[0]);
				return new BlockModifierContainerObject();
			}
			symbol.setCardRank(cardrank);
			return new BlockModifierContainerObject(descriptor, symbol);
		}

		private static BlockModifierContainerObject createPage(final String word, final int cardrank, final Block block,
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

		private BlockModifierContainerObject register() {
			if (this.symbol != null) {
				SymbolManager.tryAddSymbol(this.symbol);
			}
			return this;
		}
	}

}
