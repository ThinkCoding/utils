
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tuniu.nbi.common.util.HttpUtil;
import com.tuniu.nbi.dto.security.SecurityParam;
import com.tuniu.nbipb.web.dto.demo.DemoListGetParam;
import com.tuniu.nbipb.web.dto.demo.DemoWishPostParam;


/**
 * 客户端demo，包含get和post请求
 * 
 * @author gulingfeng<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2015-5-13 <br>
 */
public class ClientDemo {

	public static void main(String[] args) throws Exception {
		System.out.println();
		try {
			ClientDemo c = new ClientDemo();
			System.out.println(Base64
					.encodeBase64String("{\"vendorId\":\"10000\"}".getBytes()));
			c.demoCheck();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String data(String apiKey, Object param,String key) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = df.format(new Date());
		paramVo vo = new paramVo();
		vo.setApiKey(apiKey);
		vo.setTimestamp("2015-11-09 15:35:57");
		vo.setData(param);
		System.out.println("入参1是："+JSON.toJSONString(vo));
		String sign;
		try {
			sign = Signature.getSignature(net.sf.json.JSONObject.fromObject(vo), key,vo);
			System.out.println("签名是：" + sign);
			vo.setSign(sign);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return JSON.toJSONString(vo);
	}

	private static void securityCheck() throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("apiKey", "abcdefg");
		map.put("type", "1");
		map.put("name", "Gulingfeng");
		map.put("timestamp", String.valueOf(System.currentTimeMillis()));
		map.put("sign",
				getSignature(JSON.parseObject(JSON.toJSONString(map)), "123"));
		SecurityParam securityParam = new SecurityParam(JSON.toJSONString(map),
				"127.0.0.1", "/demo/1.0/wish", HttpUtil.POST);
		String result = HttpUtil.invoke(
				"http://127.0.0.1:8081/nbi-as/security/1.0/check",
				HttpUtil.POST, JSON.toJSONString(securityParam), true);
		System.out.println(result);
	}

	private void demoCheck() throws Exception {
		HttpClient client = new HttpClient();
		client.getHttpConnectionManager().getParams()
				.setConnectionTimeout(10000);
		client.getParams().setSoTimeout(60000);

//		 HttpMethod method = post();
//		 HttpMethod method = postbook();
//		 HttpMethod method = postconfirm();
//		 HttpMethod method = postcancel();
//		 HttpMethod method = postreturn();
//		 HttpMethod method = postvalidate();
		 HttpMethod method = postqueryStations();

		// contentType决定了请求的返回值是xml类型还是json类型
		// method.setRequestHeader("Content-Type",
		// "application/xml;charset=GBK");
		method.setRequestHeader("Content-Type",
				"application/json; charset=utf-8");
		try {
			client.executeMethod(method);

			BufferedReader br = new BufferedReader(new InputStreamReader(
					method.getResponseBodyAsStream()));
			StringBuffer stringBuffer = new StringBuffer();
			String str = "";
			while ((str = br.readLine()) != null) {
				stringBuffer.append(str);
			}
			System.out.println("result is:" + stringBuffer.toString());
			// System.out.println("result is:" + new
			// String(Base64.decodeBase64(stringBuffer.toString())));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
	}

	/**
	 * get请求，入参需要是json格式并进行base64加密(为了兼容老应用传输方式)
	 * 
	 * @author gulingfeng<br>
	 * @taskId <br>
	 * @return <br>
	 */
	private static HttpMethod get() {
		GetMethod method = new GetMethod(
				"http://127.0.0.1:8082/nbi-pb/rest/demo/1.0/list");

		DemoListGetParam param = new DemoListGetParam();
		param.setApiKey("key2222");
		param.setSign("gulingfeng");
		param.setPageNo(2);
		param.setPageSize(10);

		try {
			System.out.println(URLEncoder.encode(JSON.toJSONString(param),
					"utf-8"));
			method.setQueryString(URLEncoder.encode(JSON.toJSONString(param),
					"utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return method;
	}

	/**
	 * post请求，入参需要是json格式并进行base64加密(为了兼容老应用传输方式)
	 * 
	 * @author gulingfeng<br>
	 * @taskId <br>
	 * @return <br>
	 */
	private static HttpMethod post() {
		PostMethod method = new PostMethod(
				"http://10.10.30.86:13521/train/search");
		try {
			String data = "{\"fromStation\":\"BJP\",\"toStation\":\"SJP\",\"trainDate\":\"2015-12-11\"}";
			String apiKey = "QT5VF0NWBKpWaocYGw";
			String key ="43Gz1WKX5FhUJT2TnOeC";
			String param = data(apiKey, JSONObject.parse(data),key);
			System.out.println("参数是："+param);
			method.setRequestEntity(new StringRequestEntity(param,
					"application/json", "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return method;
	}

	HttpMethod postbook() throws Exception {
		PostMethod method = new PostMethod(
				"http://10.10.30.86:13521/train/return");
		try {
			String data = "{\"retailOrderId\":\"1111\",\"cheCi\":\"K1237\",\"fromStationCode\":\"BZH\",\"fromStationName\":\"亳州\",\"toStationCode\":\"RZH\",\"toStationName\":\"温州\",\"trainDate\":\"2015-12-29\",\"callBackUrl\":\"\",\"contact\":\"王飞\",\"phone\":\"13728784623\",\"insureCode \":\"123\",\"passengers\":[{\"passengerId\":33292562,\"ticketNo\":null,\"passengerName\":\"王飞\",\"passportNo\":\"130682199108203190\",\"passportTypeId\":\"1\",\"passportTypeName\":\"二代身份证\",\"piaoType\":\"1\",\"piaoTypeName\":\"成人票\",\"zwCode\":\"1\",\"zwName\":\"硬座\",\"cxin\":null,\"price\":\"1234\",\"reason\":0,\"provinceCode\":null,\"schoolCode\":null,\"schoolName\":null,\"studentNo\":null,\"schoolSystem\":null,\"enterYear\":null,\"preferenceFromStationName\":null,\"preferenceFromStationCode\":null,\"preferenceToStationName\":null,\"preferenceToStationCode\":null}]}";
			String apiKey = "Q2A07N1IOG6hmdaNfO";
			String key ="7erA3HWzcylvqmcQx0v7";
			String param = data(apiKey, DesUtil.encrypt(data, apiKey),key);
			System.out.println("参数是："+param);
			method.setRequestEntity(new StringRequestEntity(param,
					"application/json", "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return method;
	}

	HttpMethod postconfirm() {
		PostMethod method = new PostMethod(
				"https://open.tuniu.cn/train/confirm");
		try {
			String data = "{\"retailOrderId\":\"15538430T8393300\",\"orderId\":\"486099879\",\"callBackUrl\":\"\"}";
			String apiKey = "CilH5aOmlnN9XTHD1l";
			String key ="pylmGPynVRskYC4phQCB";
			String param = data(apiKey, JSONObject.parse(data),key);
			System.out.println("参数是："+param);
			method.setRequestEntity(new StringRequestEntity(param,
					"application/json", "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return method;
	}

	HttpMethod postcancel() {
		PostMethod method = new PostMethod(
				"https://10.10.30.86:13521/train/cancel");
		try {
			String data = "{\"retailOrderId\":\"15538430T8393300\",\"orderId\":\"486099554\",\"callBackUrl\":\"\"}";
			String apiKey = "Q2A07N1IOG6hmdaNfO";
			String key ="7erA3HWzcylvqmcQx0v7";
			String param = data(apiKey, JSONObject.parse(data),key);
			System.out.println("参数是："+param);
			method.setRequestEntity(new StringRequestEntity(param,
					"application/json", "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return method;
	}

	HttpMethod postreturn() throws Exception {
		PostMethod method = new PostMethod(
				"https://open.tuniu.cn/train/return");
		try {
			String data = "{\"retailOrderId\":\"15538430T8393300\",\"orderId\":\"486099879\",\"orderNumber\":\"E893019972\",\"callBackUrl\":\"\",\"tickets\":[{\"ticketNo\":\"E8930199721140074\",\"passengerName\":\"王飞\",\"passportTypeId\":\"1\",\"passportNo\":\"130682199108203190\"}]}";
			String apiKey = "CilH5aOmlnN9XTHD1l";
			String key ="pylmGPynVRskYC4phQCB";
			String param = data(apiKey, DesUtil.encrypt(data, apiKey),key);
			System.out.println("参数是："+param);
			method.setRequestEntity(new StringRequestEntity(param,
					"application/json", "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return method;
	}

	HttpMethod postqueryStations() {
		PostMethod method = new PostMethod(
				"http://10.10.30.86:13521/train/queryStations");
		try {
			String data = "{\"trainDate\":\"2015-11-15\",\"trainCode\":\"G11\"}";
			String apiKey = "Q2A07N1IOG6hmdaNfO";
			String key ="7erA3HWzcylvqmcQx0v7";
			String param = data(apiKey, JSONObject.parse(data),key);
			System.out.println("参数是："+param);
			method.setRequestEntity(new StringRequestEntity(param,
					"application/json", "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return method;
	}

	/**
	 * get请求，入参需要是json格式并进行base64加密(为了兼容老应用传输方式)
	 * 
	 * @author gulingfeng<br>
	 * @taskId <br>
	 * @return <br>
	 */

	HttpMethod postvalidate() {
		PostMethod method = new PostMethod(
				"https://open.tuniu.cn/train/validate");
		try {
			String data = "[{\"name\":\"lizhimu\",\"identityType\":1,\"identityCard\":\"130682199108203190\"}]";
			String apiKey = "CilH5aOmlnN9XTHD1l";
			String key ="pylmGPynVRskYC4phQCB";
			String param = data(apiKey, JSONObject.parse(data),key);
			System.out.println("参数是："+param);
			method.setRequestEntity(new StringRequestEntity(param,
					"application/json", "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return method;
	}

	HttpMethod getInner() {
		GetMethod method = new GetMethod(
				"http://127.0.0.1:8082/nbi-pb/rest/innerdemo/1.0/list");

		DemoListGetParam param = new DemoListGetParam();
		param.setApiKey("key2222");
		param.setSign("gulingfeng");
		param.setPageNo(2);
		param.setPageSize(10);

		method.setQueryString(new String(Base64.encodeBase64(JSON.toJSONString(
				param).getBytes())));
		return method;
	}

	/**
	 * post请求，入参需要是json格式并进行base64加密(为了兼容老应用传输方式)
	 * 
	 * @author gulingfeng<br>
	 * @taskId <br>
	 * @return <br>
	 */
	HttpMethod postInner() {
		PostMethod method = new PostMethod(
				"http://127.0.0.1:8082/nbi-pb/rest/innerdemo/1.0/wish");

		DemoWishPostParam param = new DemoWishPostParam();
		param.setApiKey("key2222");
		param.setSign("gulingfeng");
		param.setWish("我要换凳子");

		try {
			method.setRequestEntity(new StringRequestEntity(new String(Base64
					.encodeBase64(JSON.toJSONString(param).getBytes())),
					"application/json", "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return method;
	}

	public static String getSignature(JSONObject data, String secretKey)
			throws Exception {
		// 第一步：获取所有值非空的参数，忽略签名
		List<String> keyList = new ArrayList<String>();
		for (String key : data.keySet()) {
			if (data.get(key) != null && !"sign".equalsIgnoreCase(key)) {
				keyList.add(key);
			}
		}

		// 第二步：按名称排序并拼接成字符串
		String[] arrayToSort = keyList.toArray(new String[keyList.size()]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);

		StringBuilder sb = new StringBuilder(secretKey);
		for (String key : arrayToSort) {
			sb.append(key);
			sb.append(data.get(key));
		}
		sb.append(secretKey);

		// 第三步：MD5加密并转换成大写的16进制
		MessageDigest md = MessageDigest.getInstance("MD5");
		return byte2hex(md.digest(sb.toString().getBytes()));
	}

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

}
