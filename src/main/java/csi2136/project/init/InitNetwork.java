package csi2136.project.init;

import csi2136.project.net.packet.*;
import csi2136.project.net.util.PacketRegistry;

public class InitNetwork {

    public static void registerPackets() {
        PacketRegistry.registerPacket(PacketC2SHello.class);
        PacketRegistry.registerPacket(PacketS2CDisconnect.class);
        PacketRegistry.registerPacket(PacketC2SLogin.class);
        PacketRegistry.registerPacket(PacketS2CLogin.class);
        PacketRegistry.registerPacket(PacketC2SRegister.class);
        PacketRegistry.registerPacket(PacketS2CRegister.class);
        PacketRegistry.registerPacket(PacketC2SUser.class);
        PacketRegistry.registerPacket(PacketS2CUser.class);
    }

}
