package util;

/**
 * Created by Allen on 2016/5/10.
 *
 * Base class of network pair(ip, port) for {@code NetworkPairv4} and {@code NetworkPairv6}.
 */
public abstract class NetworkPair {
    String ipAddress;
    int port;

    public NetworkPair() {
    }

    public NetworkPair(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
