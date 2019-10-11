package spookyspirits.util;

import java.io.IOException;

import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.IServerPlayNetHandler;

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
		// TODO Auto-generated method stub
		// TODO make sure player is near a Phooka
		// TODO determine which riddle was asked and what the correct answer should be
		// TODO determine if player got the right answer
		// TODO give effects to player based on answer
	}

}
