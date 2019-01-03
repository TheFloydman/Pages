package thefloydman.pages.symbol.modifiers;

import com.xcompwiz.mystcraft.api.symbol.BlockDescriptor;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import thefloydman.pages.util.Reference;

public class SymbolBlock extends com.xcompwiz.mystcraft.symbol.modifiers.SymbolBlock {
	private String unlocalizedBlockName;

	public SymbolBlock(final BlockDescriptor block, final String word) {
		super(block, word);
		this.unlocalizedBlockName = getUnlocalizedName(block.blockstate);
	}

	public static ResourceLocation getSymbolIdentifier(final IBlockState blockstate) {
		String domain = blockstate.getBlock().getRegistryName().getResourceDomain();
		return new ResourceLocation(Reference.MOD_ID, "block_" + blockstate.getBlock().getRegistryName().getResourcePath() + "_"
				+ blockstate.getBlock().getMetaFromState(blockstate));
	}

	private static String getUnlocalizedName(final IBlockState blockstate) {
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
		String blockName = I18n.format(this.unlocalizedBlockName + ".name", new Object[0]);
		if (blockName.startsWith("Block of ")) {
			return blockName;
		}
		if (blockName.endsWith(" Block")) {
			blockName = blockName.substring(0, blockName.length() - " Block".length()).trim();
		}
		return blockName + " Block";
	}
}
