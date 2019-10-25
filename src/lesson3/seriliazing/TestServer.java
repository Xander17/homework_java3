package lesson3.seriliazing;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TestServer {
    public static void main(String[] args) {
        try {
            new TestServer();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ServerSocket server;
    private Socket socket = null;

    private TestServer() throws IOException, ClassNotFoundException {
        server = new ServerSocket(12345);
        while (socket == null) {
            socket = server.accept();
        }
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        TestObject testObject = (TestObject) in.readObject();
        System.out.println(testObject.getField1());
        System.out.println(testObject.getField2());
        System.out.println(testObject.getList());

        ObjectOutputStream out = new ObjectOutputStream(System.out);
        out.writeObject(testObject);

        in.close();
        out.close();
    }
}
