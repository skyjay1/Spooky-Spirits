package spookyspirits.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.world.World;

public class Wisp extends FlyingEntity {

	protected Wisp(EntityType<? extends FlyingEntity> type, World world) {
		super(type, world);
		
	}
}
