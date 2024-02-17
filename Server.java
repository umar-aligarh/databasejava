import java.io.*;
import java.net.*;

class MultithreadingSocket extends Thread {
    private Socket clientSocket;
    public MultithreadingSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    public void run()
    {
        try 
        {
            int continueConnection=1;
            while (continueConnection==1) 
            {
                continueConnection=Server.handleClient(clientSocket);
            }
        }
        catch (Exception e) 
        {
            System.out.println("Exception is caught");
        }
        finally 
        {
            try
            {
                clientSocket.close();
            }
            catch (IOException e) 
            {
                e.printStackTrace();
            } 
        }
    }
}
public class Server {
    private static final int MAX_MESSAGE_SIZE = 4096;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(3000)) {
            System.out.println("Server is listening on port 3000");
            while(true)
            {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                MultithreadingSocket object = new MultithreadingSocket(clientSocket);
                object.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int handleClient(Socket clientSocket) throws IOException {
        try (InputStream inputStream = clientSocket.getInputStream();
             OutputStream outputStream = clientSocket.getOutputStream()) {

            byte[] headerBytes = readFully(inputStream, 4);
            if (headerBytes == null) {
                System.out.println("EOF");
                return 1;
            }

            int requestLength = byteArrayToInt(headerBytes);

            if (requestLength > MAX_MESSAGE_SIZE) {
                System.out.println("Request too long");
                return 1;
            }

            byte[] requestBytes = readFully(inputStream, requestLength);
            if (requestBytes == null) {
                System.out.println("EOF");
                return 1;
            }

            String request = new String(requestBytes, "UTF-8");
            System.out.println("Client says: " + request);
            return 1;

            // String reply = "world";
            // byte[] replyBytes = reply.getBytes("UTF-8");
            // byte[] replyHeaderBytes = intToByteArray(replyBytes.length);

            // outputStream.write(replyHeaderBytes);
            // outputStream.write(replyBytes);
            // outputStream.flush();
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



