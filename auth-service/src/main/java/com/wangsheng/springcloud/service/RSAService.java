package com.wangsheng.springcloud.service;

import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

@Component
public class RSAService {

    /**
     * jks文件地址
     */
    private final static String JKS_NAME = "wangsheng.jks";

    /**
     * jks别名
     */
    private final static String ALIAS_NAME = "wangsheng";

    /**
     * 密钥口令(私钥的密码)
     */
    private final static String KEY_PASS = "wangsheng";

    /**
     * 密钥库口令(jks文件的密码)
     */
    private final static String STORE_PASS = "wangsheng";


    private PrivateKey privateKey;
    private PublicKey publicKey;

    @PostConstruct
    public void init(){
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(JKS_NAME),STORE_PASS.toCharArray());
        KeyPair keyPair =  keyStoreKeyFactory.getKeyPair(ALIAS_NAME);
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }


    //解码返回byte
    private static byte[] decryptBASE64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    //编码返回字符串
    private static String encryptBASE64(byte[] key) {
        return (new BASE64Encoder()).encodeBuffer(key);
    }


    /**
     * 获取公钥
     * @return
     * @throws Exception
     */
    public String getPublicKey() {
        return encryptBASE64(publicKey.getEncoded());
    }

    public String decrypt(String str) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE,privateKey);
        return new String(cipher.doFinal(decryptBASE64(str)));
    }

    public String encrypt(String str) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE,publicKey);
        return encryptBASE64(cipher.doFinal(str.getBytes()));
    }

    public static void main(String[] args) throws Exception {
        RSAService rsaUtil = new RSAService();
        rsaUtil.init();
        String encrypt = rsaUtil.encrypt("123");
        System.out.println(encrypt);
        String encrypt1 = rsaUtil.encrypt("123");
        System.out.println(encrypt1);
        String decrypt = rsaUtil.decrypt(encrypt);
        System.out.println(decrypt);
    }
}
