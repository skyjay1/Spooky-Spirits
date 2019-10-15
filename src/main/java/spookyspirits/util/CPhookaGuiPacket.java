package spookyspirits.util;

import java.io.IOException;
import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.IServerPlayNetHandler;
import net.minecraft.network.play.ServerPlayNetHandler;
import spookyspirits.entity.PhookaEntity;
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
	public void readPacketData(PacketBuffer buf) throws IOException {
		this.riddleId = buf.readString();
		this.answer = buf.readByte();
	}

	@Override
	public void writePacketData(PacketBuffer buf) throws IOException {
		buf.writeString(riddleId);
		buf.writeByte(answer);
	}

	@Override
	public void processPacket(IServerPlayNetHandler handler) {
		SpookySpirits.LOGGER.info("Processing Phooka GUI packet. Riddle = " + this.riddleId + ", answer = " + this.answer);
		if(handler instanceof ServerPlayNetHandler) {
			final PlayerEntity player = ((ServerPlayNetHandler)handler).player;
			final List<PhookaEntity> phookaList = player.getEntityWorld().getEntitiesWithinAABB(PhookaEntity.class, player.getBoundingBox().grow(2.5D));
			if(!phookaList.isEmpty()) {
				final PhookaRiddle riddle = PhookaRiddles.getByName(riddleId);
				if(this.answer == riddle.getCorrectAnswer()) {
					// woohoo!
					riddle.getBlessing().accept(player);
					SpookySpirits.LOGGER.info("Answer was correct!");
				} else {
					// aw man :(
					riddle.getCursing().accept(player);
					SpookySpirits.LOGGER.info("Answer was wrong!");
				}
				for(final PhookaEntity e : phookaList) {
					e.setDespawningTicks(1);
				}
			}
			
		}
		
	}

}
