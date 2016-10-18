package com.cherry.cm.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;


/**
 * Created by guangling.hjh on 15-3-11.
 */
public class EncryptUtil {

    private static final String HEADER_PARAM_PREFIX = "header_";
    private static final String TOP_SIGN_LIST = "top_sign_list";

    /**
     *
     * @param headerMap http请求中header中的参数，包括”top_sign_list“域，这个域中用”,"连接了所有需要参与sign的header参数；
     *                  如果某参数没有值，也需要放入headerMap中，相应的values为""
     * @param queryMap http请求中query中的参数
     * @param body 从http请求中取出的输入流(可参考getBody方法）
     * @param secret 签名密钥
     * @param encode 服务的编码方式（在配置服务url的页面中可以看到)
     * @return
     * @throws Exception
     */
    public static String sign(Map<String,String> headerMap,Map<String,String> queryMap,String body,String secret,String encode) throws Exception {

        //取出header中的top_sign_list，它的内容是用","隔开的参数名，这些参数需要参与sign
        //这么做的原因是header有可能会被改写
        String topSignList = headerMap.get(TOP_SIGN_LIST);
        if(isNotEmpty(topSignList)) {
            String[] headerSignParams = topSignList.split(",");

            for(int i = 0;i < headerSignParams.length; i++){
                String headerSignParam = headerSignParams[i];

                String value = headerMap.get(headerSignParam);
                //为了避免与query中的参数同名，加上前辍
                queryMap.put(HEADER_PARAM_PREFIX + headerSignParam,value);
            }

        }

        String sign = signatureForSpi(queryMap, secret, true, false, body, encode);
        return sign;
    }

    private static String signatureForSpi(Map<String, String> params, String secret,
                                          boolean appendSecret, boolean isHMac, String body ,String encode) throws Exception {
        StringBuilder sb = new StringBuilder();
        // append if not hmac
        if (!isHMac) {
            sb.append(secret);
        }
        if (params != null && !params.isEmpty()) {
            String[] names = params.keySet().toArray(new String[0]);
            Arrays.sort(names);
            for (int i = 0; i < names.length; i++) {
                String name = names[i];
                sb.append(name);
                sb.append(params.get(name));
            }
        }
        if (isNotEmpty(body)) {
            sb.append(body);
        }
        if (appendSecret && !isHMac) {
            sb.append(secret);
        }
        String sign = null;
        try {
            //md5
            sign = DigestUtils.md5Hex(sb.toString().getBytes(encode)).toUpperCase();

        } catch (Exception e) {
            throw new java.lang.RuntimeException(e);
        }
        return sign;
    }

    private static String date2ymdhms(Date date) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return f.format(date);
    }

    private static boolean isNotEmpty(String str){
        return ((str != null) && (str.length() > 0));
    }
}