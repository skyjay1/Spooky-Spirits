package spookyspirits.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.world.World;

public class FlyingSkull extends FlyingEntity {

	protected FlyingSkull(EntityType<? extends FlyingEntity> type, World world) {
		super(type, world);
		
	}

	
	public boolean isFlaming() {
		return true;
	}
}
