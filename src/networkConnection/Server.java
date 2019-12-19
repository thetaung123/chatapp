package networkConnection;


import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Created by ASUS on 24-May-17.
 */
public class Server extends NetworkConnection{
    private int port;
    public Server(int port,Consumer<Serializable> onReceiveCallBack) {
        super(onReceiveCallBack);
        this.port = port;
    }

    @Override
    protected boolean isServer() {
        return true;
    }

    @Override
    protected String getIP() {
        return null;
    }

    @Override
    protected int getPort() {
        return port;
    }
}
