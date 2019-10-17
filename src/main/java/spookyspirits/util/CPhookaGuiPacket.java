package spookyspirits.util;

import java.io.IOException;
import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.IServerPlayNetHandler;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.potion.Effects;
import spookyspirits.entity.PhookaEntity;
import spookyspirits.init.ModObjects;
import spookyspirits.init.SpookySpirits;

public class CPhookaGuiPacket implements IPacket<IServerPlayNetHandler> {

	private String riddleId;
	private byte answer;

	public CPhookaGuiPacket() { }

	public CPhookaGuiPacket(final String riddle, final byte buttonId) {
		riddleId = riddle;
		answer = buttonId;
	}

	@Override
	public void readPacketData(final PacketBuffer buf) throws IOException {
		this.riddleId = buf.readString();
		this.answer = buf.readByte();
	}

	@Override
	public void writePacketData(final PacketBuffer buf) throws IOException {
		buf.writeString(riddleId);
		buf.writeByte(answer);
	}

	@Override
	public void processPacket(final IServerPlayNetHandler handler) {
		SpookySpirits.LOGGER.info("Processing Phooka GUI packet. Riddle = " + this.riddleId + ", userInput = " + this.answer);
		if(handler instanceof ServerPlayNetHandler) {
			final PlayerEntity player = ((ServerPlayNetHandler)handler).player;
			// extraneous check for server world
			if(player.isServerWorld() && !player.getEntityWorld().isRemote) {
				// make sure player is close enough to a phooka
				final List<PhookaEntity> phookaList = player.getEntityWorld().getEntitiesWithinAABB(PhookaEntity.class, player.getBoundingBox().grow(3.0D));
				final PhookaRiddle riddle = PhookaRiddles.getByName(riddleId);
				if(!phookaList.isEmpty() && riddle != null) {
					// apply effects based on answer
					if(this.answer == riddle.getCorrectAnswer()) {
						// woohoo!
						riddle.getBlessing().accept(player);
						clearCurses(player);
						SpookySpirits.LOGGER.info("Answer was correct!");
					} else {
						// aw man :(
						riddle.getCursing().accept(player);
						SpookySpirits.LOGGER.info("Answer was wrong!");
					}
					// despawn the entity
					for(final PhookaEntity e : phookaList) {
						e.setDespawningTicks(1);
					}
				}
			}
		}
		
	}
	
	/**
	 * Removes all negative Phooka Effects from the player
	 * @param player the player
	 **/
	private static void clearCurses(final PlayerEntity player) {
		player.removePotionEffect(Effects.BLINDNESS);
		player.removePotionEffect(Effects.NAUSEA);
		player.removePotionEffect(Effects.SLOWNESS);
		player.removePotionEffect(ModObjects.PHOOKA_CURSE_SPONGE);
		player.extinguish();
		
	}

}