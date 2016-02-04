

import   java.io.IOException;
import java.security.SecureRandom;
 
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
 
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
 
public class DesUtil {
 
    private final static String DES = "DES";
    private final static String DATACHASET = "UTF-8";
    
    public static void main(String[] args) throws Exception {
        String data = "{\"retailOrderId\":\"15538430T8393205\",\"orderId\":\"AC9WSY4H1H00\",\"orderSuccess\":true,\"orderAmount\":305,\"cheCi\":\"K1237\",\"fromStationCode\":\"BZH\",\"fromStationName\":\"亳州\",\"toStationCode\":\"RZH\",\"toStationName\":\"温州\",\"trainDate\":\"2015-08-29\",\"startTime\":\"15:57:00\",\"arriveTime\":\"11:40:00\",\"orderNumber\":\"E010400222\",\"passengers\":[{\"reason\":0,\"price\":\"152.5\",\"passengerId\":33292562,\"ticketNo\":\"E0104002221053148\",\"zwCode\":\"1\",\"cxin\":\"05车厢,无座\",\"passportTypeName\":\"二代身份证\",\"passportNo\":\"341222197608254405\",\"zwName\":\"硬座\",\"piaoType\":\"1\",\"passengerName\":\"张秀勤\",\"passportTypeId\":\"1\",\"piaoTypeName\":\"成人票\"}]}";
        String key = "QT5VF0NWBKpWaocYGw";
        String e1 = encrypt(data, key);
        String e2 = decrypt(data, key);
        System.out.println(e1);
//        System.out.println(e2);

    }
     
    /**
     * Description 根据键值进行加密
     * @param data 
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        byte[] bt = encrypt(data.getBytes(DATACHASET), key.getBytes(DATACHASET));
        String strs = new BASE64Encoder().encode(bt);
        return strs;
    }
 
    /**
     * Description 根据键值进行解密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws IOException
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws IOException,
            Exception {
        if (data == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buf = decoder.decodeBuffer(data);
        byte[] bt = decrypt(buf,key.getBytes());
        return new String(bt,DATACHASET);
    }
 
    /**
     * Description 根据键值进行加密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
 
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
 
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
 
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);
 
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
 
        return cipher.doFinal(data);
    }
     
     
    /**
     * Description 根据键值进行解密
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
 
        // 从原始密钥数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
 
        // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
 
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);
 
        // 用密钥初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
 
        return cipher.doFinal(data);
    }
}