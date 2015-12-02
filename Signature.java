import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;


/**
 * API签名工具类
 * 
 * @author wangfei6<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2015年5月29日 <br>
 */
public class Signature {
	public static final String API_SIGNATURE_KEY = "sign";

    public static String getSignature(JSONObject data, String secretKey, paramVo vo) throws Exception {
    	System.out.println("入参2是："+data.toString());
        // 第一步：获取所有值非空的参数，忽略签名
        List<String> keyList = new ArrayList<String>();
        for (Object key : data.keySet()) {
            // key为null或者key是签名
            if (key == null || API_SIGNATURE_KEY.equalsIgnoreCase(key.toString())) {
                continue;
            }
            // value为null或者为空
            if (data.get(key) == null || StringUtils.isBlank(data.get(key).toString())) {
                continue;
            }
            keyList.add(key.toString());
        }
        
        // 第二步：按名称排序并拼接成字符串
        String[] arrayToSort = keyList.toArray(new String[keyList.size()]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);

        StringBuilder sb = new StringBuilder(secretKey);
        for (String key : arrayToSort) {
        	if(key.equals("data")){
        		sb.append("data");
        		sb.append(vo.getData().toString());
            
        	}
        	else
        	{
        		sb.append(key);
                sb.append(data.get(key));
        	}
        }
        sb.append(secretKey);
        System.out.println("第二步："+sb);
        // 第三步：MD5加密并转换成大写的16进制
        MessageDigest md = MessageDigest.getInstance("MD5");
        return byte2hex(md.digest(sb.toString().getBytes()));
    }

/*    public static boolean checkIsSignatureValid(String requestData, String secretKey) throws Exception {
        JSONObject obj = JSONObject.fromObject(requestData);
        String requestSign = obj.getString(API_SIGNATURE_KEY);
        if (StringUtils.isBlank(requestSign)) {
            return false;
        }
        return requestSign.equals(getSignature(obj, secretKey));
    }
*/
    private static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }

    public static void main(String[] args) throws Exception {
    	paramVo vo = new paramVo();
		vo.setApiKey("Q2A07N1IOG6hmdaNfO");
		vo.setTimestamp("2015-10-30 14:50:27");
		vo.setData("{\"trainCode\":\"G11\",\"trainDate\":\"2015-11-24\"}");
		String sign = Signature.getSignature(net.sf.json.JSONObject.fromObject(vo), "7erA3HWzcylvqmcQx0v7",vo);
		vo.setSign(sign);
        System.out.println("结果是："+JSON.toJSONString(vo));
    }

}
