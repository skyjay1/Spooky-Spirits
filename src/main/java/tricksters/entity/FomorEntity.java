package tricksters.entity;

import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.monster.DrownedEntity;
import net.minecraft.world.World;

public class FomorEntity extends DrownedEntity {

	public FomorEntity(EntityType<? extends DrownedEntity> type, World world) {
		super(type, world);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(8, new LookAtGoal(this, FomorEntity.class, 12.0F, 0.01F));

	}
	
	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return this.getHeight() - 0.1F;
	}

}
