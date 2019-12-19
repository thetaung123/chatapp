package networkConnection;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Created by ASUS on 24-May-17.
 */
public abstract class NetworkConnection {
    private ConnectionThread connectionThread = new ConnectionThread();
    private Consumer<Serializable> onReceiveCallBack;

    public NetworkConnection(Consumer<Serializable> onReceiveCallBack){
        this.onReceiveCallBack=onReceiveCallBack;
        connectionThread.setDaemon(true);
    }

    public void startConnection()throws Exception{

        connectionThread.start();
    }
    public void send(Serializable data)throws  Exception{

        connectionThread.out.writeObject(data);
    }
    public void closeConnection()throws Exception{

        connectionThread.socket.close();
    }

    protected abstract boolean isServer();
    protected abstract String getIP();
    protected abstract int getPort();

    private class ConnectionThread extends Thread{
        private Socket socket;
        private ObjectOutputStream out;
        public void run() {
           try(ServerSocket server = isServer() ? new ServerSocket(getPort()) : null;
           Socket socket = isServer() ? server.accept() : new Socket(getIP(),getPort());
               ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
               ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

               this.socket = socket;
               this.out = out;
               socket.setTcpNoDelay(true);

               while (true){
                   Serializable data = (Serializable) in.readObject() ;
                   onReceiveCallBack.accept(data);
               }

           } catch (Exception e) {
               onReceiveCallBack.accept("socialMedia.DBconnect closed");
           }
        }
    }
}
