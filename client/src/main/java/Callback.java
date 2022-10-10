import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Callback implements Demo.Callback {
    private BufferedWriter writer;

    public Callback() {
        this.writer = new BufferedWriter(
                new OutputStreamWriter(System.out));

    }

    public void response(String msg, com.zeroc.Ice.Current current) {
        try {
            writer.write("Server: " + msg + "\n");
            writer.write("\nYou: ");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}