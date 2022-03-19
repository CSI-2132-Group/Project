package csi2136.project.net.packet.message;

import csi2136.project.net.context.ServerContext;

public interface C2SMessage {

    Packet onPacketReceived(ServerContext context);

}
