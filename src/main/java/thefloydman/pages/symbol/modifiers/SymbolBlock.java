package thefloydman.pages.symbol.modifiers;

import javax.annotation.Nullable;

import com.xcompwiz.mystcraft.api.symbol.BlockDescriptor;
import com.xcompwiz.mystcraft.api.symbol.ModifierUtils;
import com.xcompwiz.mystcraft.api.world.AgeDirector;
import com.xcompwiz.mystcraft.logging.LoggerUtils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import thefloydman.pages.symbol.PagesSymbolBase;
import thefloydman.pages.util.Reference;

public class SymbolBlock extends PagesSymbolBase {
	public final BlockDescriptor blockDescriptor;
	private String unlocalizedBlockName;
	private String localizationOverride = "";
	private String localizationNonstandard = "";
	private String modId = "";
	private String blockId = "";
	private String subId = "";

	public SymbolBlock(final BlockDescriptor block, final String word, final String modId, final String blockId,
			final String subId, final String localizationOverride, @Nullable final String symbolId,
			final String localizationNonstandard) {
		super(getSymbolIdentifier(block.blockstate, symbolId));
		this.blockDescriptor = block;
		this.modId = modId;
		this.blockId = blockId;
		this.subId = subId;
		this.localizationOverride = localizationOverride;
		this.localizationNonstandard = localizationNonstandard;
		this.unlocalizedBlockName = getUnlocalizedName(block.blockstate);
	}

	public static ResourceLocation getSymbolIdentifier(final IBlockState blockstate, @Nullable final String symbolId) {
		String domain = blockstate.getBlock().getRegistryName().getResourceDomain();
		if (symbolId != null && !symbolId.isEmpty()) {
			return new ResourceLocation(domain, symbolId);
		}
		return new ResourceLocation(domain,
				"block_" + blockstate.getBlock().getRegistryName().getResourcePath() + "_"
						+ blockstate.getBlock().getMetaFromState(blockstate));
	}

	@Override
	public boolean generatesConfigOption() {
		return true;
	}

	private String getUnlocalizedName(final IBlockState blockstate) {
		ItemStack attempt = ItemStack.EMPTY;
		try {
			attempt = blockstate.getBlock().getPickBlock(blockstate, (RayTraceResult) null, (World) null,
					BlockPos.ORIGIN, (EntityPlayer) null);
		} catch (Exception ex) {
		}
		if (attempt.isEmpty()) {
			final Item i = Item.getItemFromBlock(blockstate.getBlock());
			if (i != Items.AIR) {
				final int meta = blockstate.getBlock().getMetaFromState(blockstate);
				attempt = new ItemStack(i, 1, meta);
			}
		}
		String name;
		if (attempt.isEmpty()) {
			name = blockstate.getBlock().getUnlocalizedName();
		} else {
			name = attempt.getUnlocalizedName();
		}
		return name;
	}

	@Override
	public String generateLocalizedName() {
		if (!this.localizationOverride.trim().isEmpty()) {
			return this.localizationOverride;
		}

		String blockName;

		if (!this.localizationNonstandard.trim().isEmpty()) {
			blockName = this.localizationNonstandard.trim();
			blockName = I18n.format(blockName, new Object[0]);
			blockName = addBlockIfAbsent(blockName);
			return blockName;
		}

		// Botania localization
		if (this.modId.equals("botania")) {
			String nameBotania = "tile.botania:" + this.unlocalizedBlockName.trim().substring(5) + this.subId + ".name";
			nameBotania = I18n.format(nameBotania, new Object[0]);
			nameBotania = addBlockIfAbsent(nameBotania);
			return nameBotania;
		}

		blockName = this.unlocalizedBlockName.trim();

		// General-purpose localization
		if (this.subId.trim().length() > 0) {
			blockName += "." + this.subId;
		}
		if (!blockName.endsWith(".name")) {
			blockName += ".name";
		}
		blockName = I18n.format(blockName, new Object[0]);
		blockName = addBlockIfAbsent(blockName);
		return blockName;
	}

	private String addBlockIfAbsent(String str) {
		if (!str.contains("Block")) {
			str += " Block";
		}
		return str;
	}

	@Override
	public void registerLogic(final AgeDirector controller, final long seed) {
		ModifierUtils.pushBlock(controller, this.blockDescriptor);
	}

}
