package spookyspirits.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.monster.GuardianEntity;
import net.minecraft.world.World;

public class FomorEntity extends GuardianEntity {

	public FomorEntity(EntityType<? extends GuardianEntity> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(8, new LookAtGoal(this, FomorEntity.class, 12.0F, 0.01F));

	}

}
