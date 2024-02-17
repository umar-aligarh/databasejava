import java.io.*;
import java.net.*;

public class Practice {
    public static void main(String[] args) {
        int x = 1057;
        byte[] headerBytes = intToByteArray(x);
        int y = byteArrayToInt(headerBytes);
        System.out.print(headerBytes[0]+" ");
        System.out.print(headerBytes[1]+" ");
        System.out.print(headerBytes[2]+ " ");
        System.out.print(headerBytes[3]+" ");
        System.out.print(y+" ");
    }

    


    private static byte[] intToByteArray(int value) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (value >>> 24);
        bytes[1] = (byte) (value >>> 16);
        bytes[2] = (byte) (value >>> 8);
        bytes[3] = (byte) value;
        return bytes;
    }
    private static int byteArrayToInt(byte[] bytes) {
        return (bytes[0] << 24) | ((bytes[1] & 0xFF) << 16) | ((bytes[2] & 0xFF) << 8) | (bytes[3] & 0xFF);
    }

    
}

