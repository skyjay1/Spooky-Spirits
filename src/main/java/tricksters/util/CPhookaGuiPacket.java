package tricksters.util;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.IServerPlayNetHandler;
import net.minecraft.network.play.ServerPlayNetHandler;
import net.minecraft.potion.Effects;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.network.NetworkEvent;
import tricksters.entity.PhookaEntity;
import tricksters.init.ModObjects;
import tricksters.init.Tricksters;

public class CPhookaGuiPacket {

	private String riddleId;
	private byte answer;

	public CPhookaGuiPacket() { }

	public CPhookaGuiPacket(final String riddle, final byte buttonId) {
		riddleId = riddle;
		answer = buttonId;
	}

	public String getRiddleId() {
		return riddleId;
	}
	
	@Nullable
	public PhookaRiddle getRiddle() {
		return PhookaRiddles.getByName(getRiddleId());
	}
	
	public byte getPlayerAnswer() {
		return answer;
	}

	public void toBytes(final PacketBuffer buf) {
		buf.writeString(riddleId);
		buf.writeByte(answer);
	}

	public static CPhookaGuiPacket fromBytes(final PacketBuffer buf) {
		String riddle = buf.readString();
		byte buttonId = buf.readByte();
		return new CPhookaGuiPacket(riddle, buttonId);
	}
	
	public static boolean handlePacket(final CPhookaGuiPacket message, 
			final Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
            context.enqueueWork(() -> {
            	// BEGIN ENQUEUE
        		Tricksters.LOGGER.info("Processing Phooka GUI packet. Riddle = " + message.getRiddleId() + ", userInput = " + message.getPlayerAnswer());
            	
        		final ServerPlayerEntity player = context.getSender();
            	if(player != null && player.isServerWorld() && !player.getEntityWorld().isRemote) {
            		// make sure player is close enough to a phooka
    				final List<PhookaEntity> phookaList = player.getEntityWorld().getEntitiesWithinAABB(PhookaEntity.class, player.getBoundingBox().grow(3.0D));
    				final PhookaRiddle riddle = message.getRiddle();
    				if(!phookaList.isEmpty() && riddle != null) {
    					// apply effects based on answer
    					if(message.getPlayerAnswer() == riddle.getCorrectAnswer()) {
    						// woohoo!
    						riddle.getBlessing().accept(player);
    						clearCurses(player);
    						Tricksters.LOGGER.info("Answer was correct!");
    					} else {
    						// aw man :(
    						riddle.getCursing().accept(player);
    						Tricksters.LOGGER.info("Answer was wrong!");
    					}
    					// despawn the entity
    					for(final PhookaEntity e : phookaList) {
    						e.setDespawningTicks(1);
    					}
    				}
            	}
            	// END ENQUEUE
            });
		}
		
		return true;
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
		player.removePotionEffect(ModObjects.PHOOKA_CURSE_EGGS);
		player.extinguish();
		
	}

}
