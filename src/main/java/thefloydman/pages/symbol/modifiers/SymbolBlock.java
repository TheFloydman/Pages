package thefloydman.pages.symbol.modifiers;

import com.xcompwiz.mystcraft.api.symbol.BlockDescriptor;
import com.xcompwiz.mystcraft.api.symbol.ModifierUtils;
import com.xcompwiz.mystcraft.api.world.AgeDirector;

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
import thefloydman.pages.logging.LoggerUtils;
import thefloydman.pages.symbol.PagesSymbolBase;
import thefloydman.pages.util.Reference;

public class SymbolBlock extends PagesSymbolBase {
	public final BlockDescriptor blockDescriptor;
	private String unlocalizedBlockName;
	private String modId = "";
	private String nameTinkers = "";

	public SymbolBlock(final BlockDescriptor block, final String word) {
		super(getSymbolIdentifier(block.blockstate));
		this.blockDescriptor = block;
		this.setWords(new String[] { "Transform", "Constraint", word, this.registryName.getResourcePath() });
		this.unlocalizedBlockName = getUnlocalizedName(block.blockstate);
	}

	public SymbolBlock(final BlockDescriptor block, final String word, final String modId, final String name) {
		super(getSymbolIdentifier(block.blockstate));
		this.blockDescriptor = block;
		this.setWords(new String[] { "Transform", "Constraint", word, this.registryName.getResourcePath() });
		this.unlocalizedBlockName = getUnlocalizedName(block.blockstate);
		this.modId = modId;
		this.nameTinkers = name;
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
		if (isTinkers(blockName)) {
			blockName = I18n.format(this.unlocalizedBlockName + "." + this.nameTinkers + ".name", new Object[0]);
		}
		// Molten seared stone from Tinkers' Construct has the name "Seared Stone
		// Block", so this gives the solid version a unique name.
		if (blockName.equals("Seared Stone")) {
			return "Solid Seared Stone Block";
		}
		if (blockName.contains("Block")) {
			return blockName;
		}
		return blockName + " Block";
	}

	@Override
	public void registerLogic(final AgeDirector controller, final long seed) {
		ModifierUtils.pushBlock(controller, this.blockDescriptor);
	}

	private boolean isTinkers(String name) {
		if (this.modId.equals("tconstruct")) {
			return true;
		}
		return false;
	}
}
