package csi2136.project.net.packet.message;

import csi2136.project.net.packet.buffer.IByteSerializable;
import csi2136.project.net.util.Side;

public abstract class Packet implements IByteSerializable<Packet> {

    public boolean canSendFrom(Side side) {
        if(side == Side.CLIENT) {
            return this instanceof C2SMessage;
        } else if(side == Side.SERVER) {
            return this instanceof S2CMessage;
        }

        return false;
    }

}
