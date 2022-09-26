import java.math.BigInteger;
import java.security.SecureRandom;

public class RSA
{
    private final static BigInteger one = new BigInteger("1");
    private final static SecureRandom random = new SecureRandom();

    // prime numbers
    private BigInteger p;
    private BigInteger q;

    // modulus
    private BigInteger n;

    // totient
    private BigInteger t;

    // public key
    private BigInteger e;

    // private key
    private BigInteger d;

    private String cipherText;

    /**
     * Constructor for objects of class RSA
     */
    public RSA(int N)
    {
        p = BigInteger.probablePrime(N/2, random);
        q = BigInteger.probablePrime(N/2, random);

        // initialising modulus
        n = p.multiply(q);

        // initialising t by euclid's totient function (p-1)(q-1)
        t = (p.subtract(one)).multiply(q.subtract(one));

        // initialising public key ~ 65537 is common public key
        e = new BigInteger("65537");
    }

    public int generatePrivateKey()
    {
        d = e.modInverse(t);
        return d.intValue();
    }

    public String encrypt(String plainText)
    {
        String encrypted = "";
        int j = 0;
        for(int i = 0; i < plainText.length(); i++){
            char m = plainText.charAt(i);
            BigInteger bi1 = BigInteger.valueOf(m);
            BigInteger bi2 = bi1.modPow(e, n);
            j = bi2.intValue();
            m = (char) j;
            encrypted += m;
        }
        cipherText = encrypted;
        return encrypted;
    }

    public String decrypt()
    {
        String decrypted = "";
        int j = 0;
        for(int i = 0; i < cipherText.length(); i++){
            char c = cipherText.charAt(i);
            BigInteger bi1 = BigInteger.valueOf(c);
            BigInteger bi2 = bi1.modPow(d, n);
            j = bi2.intValue();
            c = (char) j;
            decrypted += c;
        }
        return decrypted;
    }
}