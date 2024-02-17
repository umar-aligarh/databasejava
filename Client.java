import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        final String serverAddress = "127.0.0.1"; 
        final int serverPort = 3000; 

        try {
            for (int i = 0; i < 3; i++) {
                Socket socket = new Socket(serverAddress, serverPort);
                sendRequest(socket, "Message " + (i + 1));
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendRequest(Socket socket, String message) throws IOException {
        try (OutputStream outputStream = socket.getOutputStream();
             InputStream inputStream = socket.getInputStream()) {

            byte[] messageBytes = message.getBytes("UTF-8");
            byte[] headerBytes = intToByteArray(messageBytes.length);

            outputStream.write(headerBytes);
            outputStream.write(messageBytes);
            outputStream.flush();

            byte[] replyHeaderBytes = readFully(inputStream, 4);
            int replyLength = byteArrayToInt(replyHeaderBytes);

            byte[] replyBytes = readFully(inputStream, replyLength);
            String reply = new String(replyBytes, "UTF-8");
            System.out.println("Server says: " + reply);
        }
    }

    private static byte[] readFully(InputStream inputStream, int length) throws IOException {
        byte[] buffer = new byte[length];
        int bytesRead = 0;
        while (bytesRead < length) {
            int read = inputStream.read(buffer, bytesRead, length - bytesRead);
            if (read == -1) {
                return null;  // EOF
            }
            bytesRead += read;
        }
        return buffer;
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

