package csi2136.project.net.util;

import csi2136.project.net.packet.message.Packet;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PacketRegistry {

    private static List<Class<? extends Packet>> CLASSES = new ArrayList<>();

    public static void registerPacket(Class<? extends Packet> packetClass) {
        if(CLASSES.contains(packetClass)) {
            throw new InvalidParameterException("Can't register " + packetClass.getName() + " twice");
        }

        CLASSES.add(packetClass);
    }

    public static Class<Packet> getPacketClass(int id) {
        return (Class<Packet>) CLASSES.get(id);
    }

    public static int getPacketId(Packet packet) {
        return CLASSES.indexOf(packet.getClass());
    }

    public static List<String> getRegistry() {
        return CLASSES.stream().map(Class::getName).collect(Collectors.toList());
    }

}
