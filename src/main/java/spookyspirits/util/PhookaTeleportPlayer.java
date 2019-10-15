package spookyspirits.util;

import java.util.function.Consumer;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PhookaTeleportPlayer implements Consumer<PlayerEntity> {

	private final int min;
	private final int max;

	public PhookaTeleportPlayer(final int minRange, final int maxRange) {
		min = minRange;
		max = maxRange;
	}

	@Override
	public void accept(PlayerEntity t) {
		final BlockPos origin = t.getPosition();
		BlockPos p;
		int x, y, z;
		int attemptsLeft = 30;
		// try to teleport the player until it works, or you fail too many times
		do {
			x = min + t.getEntityWorld().getRandom().nextInt(max);
			z = min + t.getEntityWorld().getRandom().nextInt(max);
			if (t.getEntityWorld().getRandom().nextBoolean()) {
				x *= -1;
			}
			if (t.getEntityWorld().getRandom().nextBoolean()) {
				z *= -1;
			}
			p = getFloorY(t.getEntityWorld(), origin.add(x, 200, z));
		} while (attemptsLeft-- > 0 && t.attemptTeleport(p.getX() + 0.5D, p.getY(), p.getZ() + 0.5D, false));

		if (attemptsLeft > 0) {
			// assume success
			t.getEntityWorld().playSound((PlayerEntity) null, p.getX(), p.getY(), p.getZ(),
					SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
			t.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
		}
	}

	private static BlockPos getFloorY(final World world, BlockPos p) {
		while (world.isAirBlock(p.down()) && p.getY() > 0) {
			p = p.down();
		}
		return p;
	}

}
