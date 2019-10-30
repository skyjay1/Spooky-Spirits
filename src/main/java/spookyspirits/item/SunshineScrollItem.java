package spookyspirits.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class SunshineScrollItem extends Item {

	public SunshineScrollItem() {
		super(new Item.Properties().maxStackSize(16).group(ItemGroup.MISC).defaultMaxDamage(4));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
		worldIn.getWorldInfo().setRaining(false);
		worldIn.getWorldInfo().setThundering(false);
		worldIn.setRainStrength(0.0F);
		worldIn.setThunderStrength(0.0F);
		return new ActionResult<>(ActionResultType.SUCCESS, ItemStack.EMPTY);
	}

}
