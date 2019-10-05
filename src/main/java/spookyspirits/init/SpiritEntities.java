package spookyspirits.init;

import net.minecraft.entity.EntityType;
import net.minecraftforge.registries.ObjectHolder;
import spookyspirits.entity.WillOWisp;

@ObjectHolder(SpookySpirits.MODID)
public class SpiritEntities {
	
	@ObjectHolder("willowisp")
	public static final EntityType<WillOWisp> WILL_O_WISP = null;
}