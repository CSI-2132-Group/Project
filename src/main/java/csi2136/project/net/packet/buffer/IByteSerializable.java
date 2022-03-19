package csi2136.project.net.packet.buffer;

import java.io.IOException;

public interface IByteSerializable<T> {

	T write(ByteBuffer buf) throws IOException;

	T read(ByteBuffer buf) throws IOException;

}
