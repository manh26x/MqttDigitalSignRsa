
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class Decryption {

    public static void main(String[] args) {
        try {
            // Đọc file chứa private key
//            FileInputStream fis = new FileInputStream("C:/privateKey.rsa");
//            byte[] b = new byte[fis.available()];
//            fis.read(b);
//            fis.close();

            // Tạo private key
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(ByteBuffer.allocate(4).putInt(16354397).array());
            KeyFactory factory = KeyFactory.getInstance("RSA");
            PrivateKey priKey = factory.generatePrivate(spec);

            // Giải mã dữ liệu
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.DECRYPT_MODE, priKey);
            byte decryptOut[] = c.doFinal(Base64.getDecoder().decode(
                    String.valueOf(4677067)));
            System.out.println("Dữ liệu sau khi giải mã: " + new String(decryptOut));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}