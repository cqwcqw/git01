package util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Security;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CallApi {

    private static final String ENCODING = "UTF-8";
    public static final String ALGORIGTHM_NAME = "SM4";
    public static final String ALGORITHM_NAME_ECB_PADDING = "SM4/ECB/PKCS7Padding";
    public CallApi() {
    }

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     *  @Description:生成ecb暗号
     */
    private static Cipher generateEcbCipher(String algorithmName, int mode, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithmName,BouncyCastleProvider.PROVIDER_NAME);
        Key sm4Key = new SecretKeySpec(key, ALGORIGTHM_NAME);
        cipher.init(mode, sm4Key);
        return cipher;
    }

    /**
     *  @Description:加密
     */
    public static String encryptEcb(String hexKey, String paramStr) throws Exception {
        String cipherText = "";
        if (null != paramStr && !"".equals(paramStr)) {
            byte[] keyData = ByteUtils.fromHexString(hexKey);

            byte[] srcData = paramStr.getBytes(ENCODING);
            byte[] cipherArray = encrypt_Ecb_Padding(keyData, srcData);
            cipherText = ByteUtils.toHexString(cipherArray);
        }
        return cipherText;
    }

    /**
     *  @Description:加密模式之ecb
     */
    public static byte[] encrypt_Ecb_Padding(byte[] key, byte[] data) throws Exception {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.ENCRYPT_MODE, key);
        byte[] bs = cipher.doFinal(data);
        return bs;
    }

    /**
     *  @Description:sm4解密
     */
    public static String decryptEcb(String hexKey, String cipherText) throws Exception {
        String decryptStr = "";
        byte[] keyData = ByteUtils.fromHexString(hexKey);
        byte[] cipherData = ByteUtils.fromHexString(cipherText);
        byte[] srcData = decrypt_Ecb_Padding(keyData, cipherData);
        decryptStr = new String(srcData, ENCODING);
        return decryptStr;
    }

    /**
     *  @Description:解密
     */
    public static byte[] decrypt_Ecb_Padding(byte[] key, byte[] cipherText) throws Exception {
        Cipher cipher = generateEcbCipher(ALGORITHM_NAME_ECB_PADDING, Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(cipherText);
    }

    /**
     *  @Description:密码校验
     */
    public static boolean verifyEcb(String hexKey,String cipherText,String paramStr) throws Exception {
        boolean flag = false;
        byte[] keyData = ByteUtils.fromHexString(hexKey);
        byte[] cipherData = ByteUtils.fromHexString(cipherText);
        byte[] decryptData = decrypt_Ecb_Padding(keyData,cipherData);
        byte[] srcData = paramStr.getBytes(ENCODING);
        flag = Arrays.equals(decryptData,srcData);
        return flag;
    }
    /**
     *
     * @title: sendHttp
     * @description: Http请求模拟
     * @param: @param url
     * @param: @param params
     * @param: @return
     * @return: String
     * @date: 2019年7月15日
     */
    private static String sendHttp(String url, Map<String, Object> params) {
        URL u = null;
        HttpURLConnection conn = null;
        StringBuffer sb = new StringBuffer();
        StringBuffer buffer = new StringBuffer();
        if (params != null) {
            for (Map.Entry<String, Object> e : params.entrySet()) {
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue());
                sb.append("&");
            }
        }
        System.out.println("send_url:" + url);
        System.out.println("send_data:" + sb.toString());
        try {
            u = new URL(url);
            conn = (HttpURLConnection) u.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(30000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            osw.write(sb.toString());
            osw.flush();
            osw.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String temp;
            while ((temp = br.readLine()) != null) {
                buffer.append(temp);
                buffer.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return buffer.toString();
    }
    /**
     *  @Description:测试类
     */
    public String test() throws Exception {
        String result=null;
        for (int i = 0; i < 3 ; i++) {
            Map<String, Object> param = new HashMap<>();
            param.put("pageSize", "10");
            param.put("currentPage", "1");
            param.put("TONGYIXINYONGDAIMA", "91532622582396593J");
            String json = param.toString();
            Map<String, Object> map = new HashMap<>();
            map.put("appID", "5492d86b175d479695f7b31cbaf126a0");
//             map.put("appID", "8ff9dc0208a24c97bcfa08afe26674b4");
            map.put("uniqueIdentifies", "intelligenceData_022");
            // 所分配得密钥key
            String key = "a0ca00f603a64be69d367763e62e1ea0";
//             String key = "c74c9bd2fcb143e482acf4cd63f464aa";
            String cipher = CallApi.encryptEcb(key, json);
            //System.out.println(cipher);
            map.put("sm4token", cipher);
            //校验加密是否正确
            //System.out.println(CallApi.verifyEcb(key, cipher, json));
            result = sendHttp("http://192.168.155.213:10021/service-web/api/callResource", map);
//            json = CallApi.decryptEcb(key1, cipher);
            System.out.println(result);
        }
        return result;
    }
    public String test(String tym) throws Exception {
        String result=null;
        for (int i = 0; i < 3 ; i++) {
            Map<String, Object> param = new HashMap<>();
            param.put("pageSize", "10");
            param.put("currentPage", "1");
            param.put("TONGYIXINYONGDAIMA", tym);
            String json = param.toString();
            Map<String, Object> map = new HashMap<>();
            map.put("appID", "5492d86b175d479695f7b31cbaf126a0");
//             map.put("appID", "8ff9dc0208a24c97bcfa08afe26674b4");
            map.put("uniqueIdentifies", "intelligenceData_021");
            // 所分配得密钥key
            String key = "a0ca00f603a64be69d367763e62e1ea0";
//             String key = "c74c9bd2fcb143e482acf4cd63f464aa";
            String cipher = CallApi.encryptEcb(key, json);
            //System.out.println(cipher);
            map.put("sm4token", cipher);
            //校验加密是否正确
            //System.out.println(CallApi.verifyEcb(key, cipher, json));

            System.out.println(new Date());
            result = sendHttp("http://192.168.155.213:10021/service-web/api/callResource", map);
            System.out.println(new Date());
//            json = CallApi.decryptEcb(key1, cipher);
        }
        return result;
    }
    public static void main(String[] args) {
        try {
            for (int i = 1; i < 2; i++){
                Map<String,Object> param =new HashMap<>();
                param.put("pageSize","10");
                param.put("currentPage","1");
                param.put("TONGYIXINYONGDAIMA","91532622582396593J");
                String json=param.toString();
                Map<String,Object> map =new HashMap<>();
                map.put("appID", "5492d86b175d479695f7b31cbaf126a0");
//             map.put("appID", "8ff9dc0208a24c97bcfa08afe26674b4");
                map.put("uniqueIdentifies", "intelligenceData_021");
                // 所分配得密钥key
                String key = "a0ca00f603a64be69d367763e62e1ea0";
//             String key = "c74c9bd2fcb143e482acf4cd63f464aa";
                String cipher = CallApi.encryptEcb(key, json);
                //System.out.println(cipher);
                map.put("sm4token",cipher);
                //校验加密是否正确
                //System.out.println(CallApi.verifyEcb(key, cipher, json));

                String result = sendHttp("http://11.23.2.170:10021/service-web/api/callResource", map);
//            json = CallApi.decryptEcb(key1, cipher);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
