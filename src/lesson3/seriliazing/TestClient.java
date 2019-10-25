package lesson3.seriliazing;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class TestClient {
    public static void main(String[] args) {
        try {
            new TestClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Socket socket;

    private TestClient() throws IOException {
        socket = new Socket("localhost", 12345);

        String[] s = {"A", "B", "C", "D", "E"};
        TestObject testObject = new TestObject(100, "200", new ArrayList<>(Arrays.asList(s)));
        try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
            out.writeObject(testObject);
        }
    }
}
