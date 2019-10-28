package logic.net;

import java.math.BigInteger;
import java.util.UUID;

public class UDPData {
    public static byte[] encodePort(int port) {
        if(port < 1 || port > 65535) throw new IllegalArgumentException("Wrong port " + port);

        byte[] result = new byte[4];
        result[0] = 1;

        BigInteger portBI = BigInteger.valueOf(port);
        byte[] portBytes = portBI.toByteArray();
        int length = portBytes.length;
        if(length > 3) throw new RuntimeException("How is it possible?");

        for(int i = 1; i <= portBytes.length; i++)
            result[result.length - i] = portBytes[portBytes.length - i];

        return result;
    }


    public static int decodePort(byte[] data) {
        if(data[0] != 1) throw new IllegalArgumentException("Wrong data[]");

        int port = new BigInteger(data, 1, 3).intValueExact();
        if(port < 1 || port > 65535) throw new IllegalArgumentException("Wrong port " + port);
        return port;
    }


    public static byte[] encodeUUID(UUID id) {
        byte[] idBytes = id.toString().getBytes();
        byte[] data = new byte[37];

        data[0] = 0;
        System.arraycopy(idBytes, 0, data, 1, idBytes.length);
        return data;
    }

    public static UUID decodeUUID(byte[] data) {
        if(data[0] != 0) throw new IllegalArgumentException("Wrong data[]");
        return UUID.fromString(new String(data, 1, 36));
    }
}