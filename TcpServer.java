import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class TcpServer {

    private void doSomethingHttp(Socket socket) {
        try (InputStream inputStream = socket.getInputStream()) {
            byte[] inputBytes = new byte[1024];
            int bytesRead = inputStream.read(inputBytes);
            System.out.println("Bytes Read: " + bytesRead);
            String input = new String(inputBytes);
            System.out.println(input);
            OutputStream outputStream = socket.getOutputStream();
            String output = "HTTP/1.1 200 OK\r\n\r\nHello, World!\r\n";
            outputStream.write(output.getBytes());
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doSomethingTcp(Socket socket) {
        try (InputStream inputStream = socket.getInputStream()) {
            byte[] inputBytes = new byte[1024];
            //Blocking code
            int bytesRead = inputStream.read(inputBytes);
            System.out.println("Bytes Read: " + bytesRead);
            String input = new String(inputBytes);
            System.out.println(input);
            OutputStream outputStream = socket.getOutputStream();
            String output = "HTTP/1.1 200 OK\r\n\r\nHello, World!\r\n";
            outputStream.write(output.getBytes());
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int port = 8080;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started running on port: " + port);
            while(true) {
                Socket socket = serverSocket.accept();
                //Making server multi-threaded
                Runnable task = () -> new TcpServer().doSomethingTcp(socket);
                Thread thread = new Thread(task);
                thread.start();
            } 
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}