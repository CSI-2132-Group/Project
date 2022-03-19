package csi2136.project.net.packet.message;

import csi2136.project.net.context.ClientContext;

public interface S2CMessage {

    Packet onPacketReceived(ClientContext context);

}
