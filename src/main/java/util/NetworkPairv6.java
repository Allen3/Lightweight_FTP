package util;


/**
 * Created by Allen on 2016/5/10.
 */
public class NetworkPairv6 extends NetworkPair {
    public NetworkPairv6() {
    }

    public NetworkPairv6(String ipAddress, int port) {
        super(ipAddress, port);
    }

    /**
     * Parse the net address in the EPRT arg like {@code |2|::1|11065|} for ipv6 or {@code |1|132.235.1.2|6275|} for
     * ipv4. Actually the EPRT is intended for ipv6 while is supports ipv4 format address as well and thus put inside
     * {@code NetworkPairv6} class. This method will not check the legality of compressedAddress.
     *
     * @param compressedAddress The input string.
     * @return  an {@code NetworkPairv4} or an {@code NetworkPairv6} instance.
     */
    public static NetworkPair parse(String compressedAddress) {
        //TODO
        NetworkPair networkPair;
        if (compressedAddress.charAt(1) == '1') {
            //ipv4
            networkPair = new NetworkPairv4();
        } else {
            //ipv6
            networkPair = new NetworkPairv6();
        }
        int index;
        for (index = 3;index < compressedAddress.length();index ++) {
            if (compressedAddress.charAt(index) == '|') {
                break;
            }
        }
        networkPair.setIpAddress(compressedAddress.substring(3, index));
        networkPair.setPort(Integer.parseInt(compressedAddress.substring(index + 1, compressedAddress.length())));

        return networkPair;
    }

    public static String compress(NetworkPairv6 networkPairv6) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("|||");
        stringBuilder.append(networkPairv6.getPort());
        stringBuilder.append("|");

        return stringBuilder.toString();
    }


}
