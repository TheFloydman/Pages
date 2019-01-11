package thefloydman.pages.symbol.modifiers;

import com.xcompwiz.mystcraft.api.symbol.BlockDescriptor;
import com.xcompwiz.mystcraft.api.symbol.ModifierUtils;
import com.xcompwiz.mystcraft.api.world.AgeDirector;
import com.xcompwiz.mystcraft.logging.LoggerUtils;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
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
	private String modID = "";
	private String blockID = "";
	private String subID = "";

	public SymbolBlock(final BlockDescriptor block, final String word, final String modID, final String blockID,
			final String subID, final String localizationOverride) {
		super(getSymbolIdentifier(block.blockstate));
		this.blockDescriptor = block;
		this.setWords(new String[] { "Transform", "Constraint", word, this.registryName.getResourcePath() });
		this.modID = modID;
		this.blockID = blockID;
		this.subID = subID;
		this.localizationOverride = localizationOverride;
		this.unlocalizedBlockName = getUnlocalizedName(block.blockstate);
	}

	public static ResourceLocation getSymbolIdentifier(final IBlockState blockstate) {
		String domain = blockstate.getBlock().getRegistryName().getResourceDomain();
		if (domain.equals("minecraft")) {
			domain = Reference.MOD_ID;
		}
		return new ResourceLocation(domain, "block_" + blockstate.getBlock().getRegistryName().getResourcePath() + "_"
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
		if (this.localizationOverride.trim().length() > 0) {
			return this.localizationOverride;
		}

		// Botania localization
		if (this.modID.equals("botania")) {
			String nameBotania = "tile.botania:" + this.unlocalizedBlockName.trim().substring(5) + this.subID + ".name";
			nameBotania = I18n.format(nameBotania, new Object[0]);
			if (!nameBotania.contains("Block")) {
				nameBotania += " Block";
			}
			return nameBotania;
		}

		String blockName = this.unlocalizedBlockName.trim();

		// General-purpose localization
		if (this.subID.trim().length() > 0) {
			blockName += "." + this.subID;
		}
		if (!blockName.endsWith(".name")) {
			blockName += ".name";
		}
		blockName = I18n.format(blockName, new Object[0]);

		if (blockName.contains("Block")) {
			return blockName;
		}

		return blockName + " Block";
	}

	@Override
	public void registerLogic(final AgeDirector controller, final long seed) {
		ModifierUtils.pushBlock(controller, this.blockDescriptor);
	}
}
