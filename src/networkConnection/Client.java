package networkConnection;


import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Created by ASUS on 24-May-17.
 */
public class Client extends NetworkConnection {
    String ip;
    private int port;
    public Client(String ip, int port, Consumer<Serializable> onReceiveCallBack) {
        super(onReceiveCallBack);
        this.ip = ip;
        this.port = port;
    }

    @Override
    protected boolean isServer() {
        return false;
    }

    @Override
    protected String getIP() {
        return ip;
    }

    @Override
    protected int getPort() {
        return port;
    }
}
