package thefloydman.pages.symbol;

import com.xcompwiz.mystcraft.symbol.SymbolBase;
import net.minecraft.util.ResourceLocation;

public abstract class PagesSymbolBase extends SymbolBase {

	public PagesSymbolBase(ResourceLocation registryName) {
		super(registryName);
	}

	@Override
	public String getUnlocalizedName() {
		return "pages.symbol." + this.getRegistryName().getResourcePath();
	}
}