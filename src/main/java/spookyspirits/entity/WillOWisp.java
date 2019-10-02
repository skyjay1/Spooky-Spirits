package spookyspirits.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.world.World;

public class WillOWisp extends FlyingEntity {
	
	private Wisp wisp;

	protected WillOWisp(EntityType<? extends FlyingEntity> type, World world) {
		super(type, world);
		
	}

}
