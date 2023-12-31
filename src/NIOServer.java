import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws Exception {
        
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress address = new InetSocketAddress("localhost", 9999);

        serverSocketChannel.bind(address);
        serverSocketChannel.configureBlocking(false);

        int ops = serverSocketChannel.validOps();
        SelectionKey selectionKey = serverSocketChannel.register(selector, ops, null);

        log("[SERVER]: Waiting for clients...");

        while(true){

            selector.select();

            Set<SelectionKey> keyset = selector.keys();
            Set<SelectionKey> modifiableKeySet = new HashSet<>(keyset);
            Iterator<SelectionKey> iterator = modifiableKeySet.iterator();
            
            while(iterator.hasNext()){
                SelectionKey myKey = iterator.next();

                if(myKey.isAcceptable()){
                    SocketChannel client = serverSocketChannel.accept();
                    if (client != null) {
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_READ);

                        log("[SERVER] Connection accepted: " + client.getLocalAddress());
                    }
                }else if(myKey.isReadable()){

                    SocketChannel client = (SocketChannel) myKey.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(256);
                    int bytesRead = client.read(buffer);

                    if (bytesRead == -1) {
                        // Connection closed by client
                        log("[SERVER] Connection closed by client.");
                        myKey.cancel();
                        client.close();
                    } else if (bytesRead > 0) {
                        buffer.flip();
                        String result = new String(buffer.array()).trim();
                        log("[SERVER] Message received: " + result);

                        if (result.equals("end")) {
                            log("[SERVER] Closing connection...");
                            myKey.cancel();
                            client.close();
                        }
                    }
                }
                iterator.remove();
            }
            
        }
    }

    private static void log(String s){
        System.out.println(s);
    }
}