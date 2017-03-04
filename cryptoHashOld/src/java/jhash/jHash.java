package jhash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author prusakovan
 */
@WebService(serviceName = "jHash")
public class jHash {
    private static final Logger LOG = Logger.getLogger(jHash.class.getName());

    /**
     * Возвращает хэш SHA-512
     *
     * @param txt
     * @return
     * @throws java.security.NoSuchAlgorithmException
     */
    @WebMethod(operationName = "getHash512")
    public String getHash512(@WebParam(name = "beginString") String txt) throws NoSuchAlgorithmException {
        StringBuffer hexString = hashing(txt, "SHA-512");
        return hexString.toString();
    }

    @WebMethod(operationName = "getHash256")
    public String getHash256(@WebParam(name = "beginString") String txt) throws NoSuchAlgorithmException {
        StringBuffer hexString = hashing(txt, "SHA-256");
        return hexString.toString();
    }

    @WebMethod(operationName = "getSHA-1")
    public String getHashSHA1(@WebParam(name = "beginString") String txt) throws NoSuchAlgorithmException {
        StringBuffer hexString = hashing(txt, "SHA-1");
        return hexString.toString();
    }

    @WebMethod(operationName = "getMD5")
    public String getHashMD5(@WebParam(name = "beginString") String txt) throws NoSuchAlgorithmException {
        StringBuffer hexString = hashing(txt, "MD5");
        return hexString.toString();
    }
    
//    @WebMethod(operationName = "getHMacSHA512")
//    public String getHMacSHA512(@WebParam(name = "beginString") String txt) throws NoSuchAlgorithmException {
//        
//        //StringBuffer hexString = hashing(txt, "MD5");
//        return hexString.toString();
//    }    

    private StringBuffer hashing(String txt, String algoritm) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algoritm);
        md.update(txt.getBytes());
        byte byteData[] = md.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            String hex = Integer.toHexString(0xff & byteData[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString;
    }
}
