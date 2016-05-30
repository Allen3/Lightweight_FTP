package util;


/**
 * Created by Allen on 4/30/2016.
 */
public class NetworkPairv4 extends  NetworkPair {

    public NetworkPairv4() {
        super();
    }

    public NetworkPairv4(String ipAddress, int port) {
        super(ipAddress, port);
    }

    /**
     * Parse the ipAddress and port from a compressed string whose format should be like, e.g.
     * 127,0,0,1,29,239. The last two numbers together construct the port number which should be the first number
     * multiplies 256 and then pluses the second one. Here, 29 * 256 + 239.
     *
     * @param compressedAddress
     */
    public static NetworkPairv4 parse(String compressedAddress) {
        NetworkPairv4 networkPair = new NetworkPairv4();

        int commaCnt = 0, index;
        for (index = 0;index < compressedAddress.length();index ++) {
            if (compressedAddress.charAt(index) == ',') {
                commaCnt ++;
                if (commaCnt == 4) break;
            }
        }

        networkPair.ipAddress = compressedAddress.substring(0, index).replaceAll(",", ".");
        int portBase = 0;
        for (index = index + 1;index < compressedAddress.length();index ++) {
            if (compressedAddress.charAt(index) != ',') {
                portBase = portBase * 10 + compressedAddress.charAt(index) - '0';
            } else {
                break;
            }
        }

        networkPair.port = portBase * 256 + Integer.valueOf(compressedAddress.substring(index + 1, compressedAddress.length()));

        return networkPair;
    }

    public static String compress(NetworkPairv4 networkPair) {
        StringBuilder result = new StringBuilder(networkPair.ipAddress.replaceAll("\\.", ","));
        result.append(",");
        result.append(networkPair.port / 256);
        result.append(",");
        result.append(networkPair.port % 256);

        return result.toString();
    }
}
