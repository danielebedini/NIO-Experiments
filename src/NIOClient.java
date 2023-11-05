import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class NIOClient {
    public static void main(String[] args) throws Exception {
        
        SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 9999));

        log("[CLIENT] I just started.");
        
        ArrayList<String> data = new ArrayList<>();
        data.add("begin");
        data.add("1");
        data.add("2");
        data.add("3");
        data.add("4");
        data.add("5");
        data.add("6");
        data.add("7");
        data.add("end");

        ByteBuffer buffer = ByteBuffer.allocate(256);
        
        for(String s : data){
            
            byte[] message = new String(s).getBytes();
            buffer = ByteBuffer.wrap(message);
            client.write(buffer);
            log("[CLIENT] I'm sending "+ s + " to the server.");
            buffer.clear();

            Thread.sleep(1000);
        }
        
        buffer.clear();
        client.close();
    }

    private static void log(String s){
        System.out.println(s);
    }
}
