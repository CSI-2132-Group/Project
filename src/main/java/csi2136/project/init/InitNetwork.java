package csi2136.project.init;

import csi2136.project.net.packet.PacketC2SHello;
import csi2136.project.net.packet.PacketC2SLogin;
import csi2136.project.net.packet.PacketS2CDisconnect;
import csi2136.project.net.packet.PacketS2CLogin;
import csi2136.project.net.util.PacketRegistry;

public class InitNetwork {

    public static void registerPackets() {
        PacketRegistry.registerPacket(PacketC2SHello.class);
        PacketRegistry.registerPacket(PacketS2CDisconnect.class);
        PacketRegistry.registerPacket(PacketC2SLogin.class);
        PacketRegistry.registerPacket(PacketS2CLogin.class);
    }

}
