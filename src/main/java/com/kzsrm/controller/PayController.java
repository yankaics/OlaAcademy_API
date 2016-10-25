package com.kzsrm.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipayCore;
import com.alipay.util.AlipayNotify;
import com.kzsrm.model.CoinHistory;
import com.kzsrm.model.Course;
import com.kzsrm.model.OrderInfo;
import com.kzsrm.model.User;
import com.kzsrm.service.CoinHistoryService;
import com.kzsrm.service.GoodsService;
import com.kzsrm.service.OrderInfoService;
import com.kzsrm.service.UserService;
import com.kzsrm.utils.DateUtil;
import com.kzsrm.utils.MapResult;
import com.wxpay.config.WXPayConfig;
import com.wxpay.util.CommonUtil;
import com.wxpay.util.PayCommonUtil;
import com.wxpay.util.TenpayUtil;
import com.wxpay.util.XMLUtil;

@Controller
@RequestMapping("/pay")
public class PayController {
	private static Logger logger = LoggerFactory.getLogger(Course.class);
	@Resource
	private OrderInfoService orderInfoService;
	@Resource
	private UserService userService;
	@Resource
	private GoodsService goodsService;
	@Resource
	private CoinHistoryService coinHistoryService;
	/**
	 * 是否显示支付模块(苹果审核)
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/showPayModule")
	public Map<String, Object> showPayModule() throws Exception {
		Map<String, Object> ret = MapResult.initMap();
		ret.put("result", 1);
		return ret;
	}

	/**
	 * 是否显示支付模块(苹果审核) 跟版本关联，1.2.2以后版本使用
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/showPayModuleWithVersion")
	public Map<String, Object> showPayModuleWithVersion() throws Exception {
		Map<String, Object> ret = MapResult.initMap();
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("version", "1.2.5");
		jsonObj.put("thirdPay", "0");
		ret.put("result", jsonObj);
		return ret;
	}

	/**
	 * 支付宝订单信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getAliOrderInfo")
	public Map<String, Object> getAliOrderInfo(
			@RequestParam(required = true) String userId,
			@RequestParam(required = true) String type,
			@RequestParam(required = true) String goodsId,
			@RequestParam(defaultValue="0") int coin) throws Exception {
		
		User u = userService.selectUser(Integer.parseInt(userId));
		if(type.equals("3")&&Integer.parseInt(u.getCoin())<coin){
			return MapResult.initMap(10001, "欧拉币不足");
		}

		String out_trade_no = generateOrderNo();

		try {
			// 数据库中创建订单信息
			createOrder(out_trade_no, userId, type, goodsId,coin);

			String price = "0.01";
			String body = "欧拉会员";
			if (type.equals("1")) { // 月度会员
				price = "30";
			} else if (type.equals("2")) { // 年度会员
				price = "158";
			} else {
				float discountPrice = goodsService.getById(goodsId).getPrice();
				if(coin>0){ //欧拉币兑换，最多抵10%
					discountPrice -= coin*0.05;
				}
				price = discountPrice + "";
				body = goodsService.getById(goodsId).getName();
			}

			String orderInfo = AlipayCore.getOrderInfo(body, "订单号："
					+ out_trade_no, price, out_trade_no);
			String sign = AlipayCore.sign(orderInfo, AlipayConfig.private_key);
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");

			String orderString = orderInfo + "&sign=\"" + sign + "\"&"
					+ AlipayCore.getSignType();

			Map<String, Object> ret = MapResult.initMap();

			Map<String, String> infoMap = new HashMap<String, String>();
			infoMap.put("orderInfo", orderString);
			infoMap.put("orderNo", out_trade_no);

			ret.put("result", infoMap);
			return ret;

		} catch (Exception e) {
			return MapResult.failMap();
		}
	}

	/**
	 * 微信支付订单信息
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getWXPayReq")
	public Map<String, Object> getWXPayReq(
			@RequestParam(required = true) String userId,
			@RequestParam(required = true) String type,
			@RequestParam(required = true) String goodsId,
			@RequestParam(defaultValue="0") int coin) throws Exception {
		
		User u = userService.selectUser(Integer.parseInt(userId));
		if(type.equals("3")&&Integer.parseInt(u.getCoin())<coin){
			return MapResult.initMap(10001, "欧拉币不足");
		}

		String out_trade_no = generateOrderNo();

		// 数据库中创建订单信息
		if (createOrder(out_trade_no, userId, type, goodsId,coin) != 1) {
			return MapResult.failMap();
		}

		String noncestr = PayCommonUtil.CreateNoncestr();
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);

		SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
		parameters.put("appid", WXPayConfig.APPID);
		parameters.put("mch_id", WXPayConfig.MCH_ID);
		parameters.put("nonce_str", noncestr);
		parameters.put("out_trade_no", out_trade_no);
		if (type.equals("1")) { // 月度会员
			parameters.put("total_fee", "3000");
			parameters.put("body", "欧拉会员");
		} else if (type.equals("2")) { // 年度会员
			parameters.put("total_fee", "15800");
			parameters.put("body", "欧拉会员");
		} else {
			float discountPrice = goodsService.getById(goodsId).getPrice();
			if(coin>0){ //欧拉币兑换，最多抵10%
				discountPrice -= coin*0.05;
			}
			parameters.put("total_fee", (int)(discountPrice * 100) + "");
			parameters.put("body", goodsService.getById(goodsId).getName());
		}

		parameters.put("spbill_create_ip", InetAddress.getLocalHost()
				.getHostAddress());
		parameters.put("notify_url", WXPayConfig.NOTIFY_URL);
		parameters.put("trade_type", "APP");
		String sign = PayCommonUtil.createSign("UTF-8", parameters);
		parameters.put("sign", sign);
		String requestXML = PayCommonUtil.getRequestXml(parameters);
		// 以POST方式调用微信统一支付接口 取得预支付id
		String result = CommonUtil.httpsRequest(WXPayConfig.UNIFIED_ORDER_URL,
				"POST", requestXML);
		// 解析微信返回的信息，以Map形式存储便于取值
		Map<String, String> map = XMLUtil.doXMLParse(result);

		// 获取prepayId
		String prepayid = map.get("prepay_id");
		logger.info("获取prepayid------值 " + prepayid);

		// 吐回给客户端的参数
		if (null != prepayid && !"".equals(prepayid)) {
			SortedMap<Object, Object> params = new TreeMap<Object, Object>();
			params.put("appid", WXPayConfig.APPID);
			params.put("partnerid", WXPayConfig.MCH_ID);
			params.put("prepayid", prepayid);
			params.put("timestamp", timestamp);
			params.put("noncestr", noncestr);
			params.put("package", "Sign=WXPay");
			params.put("sign", PayCommonUtil.createSign("UTF-8", params));

			Map<String, Object> ret = MapResult.initMap();
			ret.put("result", JSONObject.fromObject(params));
			return ret;
		} else {
			return MapResult.failMap();
		}
	}

	/**
	 * 生成用于支付的订单号
	 */
	private String generateOrderNo() {
		// 当前时间 yyyyMMddHHmmss
		String currTime = TenpayUtil.getCurrTime();
		// 8位日期
		String strTime = currTime.substring(8, currTime.length());
		// 四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		// 10位序列号,可以自行调整。
		String strReq = strTime + strRandom;
		// 订单号，此处用时间加随机数生成，商户根据自己情况调整，只要保持全局唯一就行

		return strReq;
	}

	/**
	 * 数据库中生成订单
	 * 
	 * @param orderNo
	 *            订单号
	 * @param userId
	 *            userId
	 */
	private int createOrder(String tradeNo, String userId, String type,
			String goodsId,int coin) {
		// 更新购买数量(提交订单，购买量即＋1)
		if ("3".equals(type)) {
			goodsService.updateGoods(goodsId);
		}
		// 创建订单
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setTradeNo(tradeNo);
		orderInfo.setUserId(Integer.parseInt(userId));
		orderInfo.setType(Integer.parseInt(type));
		orderInfo.setGoodsId(goodsId);
		orderInfo.setStatus(0);
		orderInfo.setCoin(coin);
		orderInfo.setCreateTime(new Date());
		return orderInfoService.createOrderInfo(orderInfo);
	}

	/**
	 * 微信支付成功回调
	 * 
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/wxPayCallBack")
	private void wxPayCallBack(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			InputStream inStream = request.getInputStream();
			ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = inStream.read(buffer)) != -1) {
				outSteam.write(buffer, 0, len);
			}
			outSteam.close();
			inStream.close();
			String result = new String(outSteam.toByteArray(), "utf-8");// 获取微信调用我们notify_url的返回信息
			Map<Object, Object> map = XMLUtil.doXMLParse(result);
			for (Object keyValue : map.keySet()) {
				System.out.println(keyValue + "=" + map.get(keyValue));
			}
			if (map.get("result_code").toString().equalsIgnoreCase("SUCCESS")) {
				// TODO 对数据库的操作
				int code = updateOrderInfoAndVIPTime(map.get("out_trade_no")
						.toString());
				if (code == 1) {
					// 告诉微信服务器，我收到信息了，不要在调用回调action了
					response.getWriter().write("success");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * 支付宝支付成功回调
	 */
	@ResponseBody
	@RequestMapping(value = "/aliPayCallBack")
	private void aliPayCallBack(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			// 获取支付宝POST过来反馈信息
			Map<String, String> params = new HashMap<String, String>();
			Map requestParams = request.getParameterMap();
			for (Iterator iter = requestParams.keySet().iterator(); iter
					.hasNext();) {
				String name = (String) iter.next();
				String[] values = (String[]) requestParams.get(name);
				String valueStr = "";
				for (int i = 0; i < values.length; i++) {
					valueStr = (i == values.length - 1) ? valueStr + values[i]
							: valueStr + values[i] + ",";
				}
				// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
				// valueStr = new String(valueStr.getBytes("ISO-8859-1"),
				// "gbk");
				params.put(name, valueStr);
			}

			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
			String order_no = request.getParameter("out_trade_no"); // 获取订单号
			String trade_no = request.getParameter("trade_no"); // 支付宝交易号
			String total_fee = request.getParameter("total_fee"); // 获取总金额
			String subject = new String(request.getParameter("subject")
					.getBytes("ISO-8859-1"), "gbk");// 商品名称、订单名称
			String body = "";
			if (request.getParameter("body") != null) {
				body = new String(request.getParameter("body").getBytes(
						"ISO-8859-1"), "gbk");// 商品描述、订单备注、描述
			}
			String buyer_email = request.getParameter("buyer_email"); // 买家支付宝账号
			String trade_status = request.getParameter("trade_status"); // 交易状态
			// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

			if (AlipayNotify.verify(params)) {// 验证成功
				// TODO 对数据库的操作
				if (trade_status.equals("TRADE_FINISHED")
						|| trade_status.equals("TRADE_SUCCESS")) {
					int code = updateOrderInfoAndVIPTime(order_no);
					if (code == 1) {
						response.getWriter().write("success");
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/**
	 * IAP支付回调
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/updateVIPByIAP")
	public Map<String, Object> updateVIPByIAP(
			@RequestParam(required = true) int userId,
			@RequestParam(required = true) String productId,
			@RequestParam(required = true) String receipt) throws Exception {
		Map<String, Object> ret = MapResult.initMap();
		// if(checkStatus(receipt).equals("0")); // 从苹果服务器校验支付状态,后续完善
		User user = userService.selectUser(userId);
		Calendar c = Calendar.getInstance();
		try {
			// 更新会员状态
			if (user.getVipTime() == null
					|| DateUtil.getDifferSec(new Date(), user.getVipTime()) < 0) {
				Date date = new Date(); // 当前日期
				c.setTime(date);
				if (productId.equals("olaxueyuan1001")) {
					c.add(Calendar.MONTH, 1);
				} else {
					c.add(Calendar.MONTH, 6);
				}
			} else {
				c.setTimeInMillis(user.getVipTime().getTime()); // 会员有效日期
				if (productId.equals("olaxueyuan1001")) {
					c.add(Calendar.MONTH, 1);
				} else {
					c.add(Calendar.MONTH, 6);
				}
			}
			userService.updateVipTime(userId, c.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return MapResult.failMap();
		}
		return ret;
	}

	// 该方法目前仅提供思路，未进行验证
	private String checkStatus(String receipt) {
		HttpURLConnection conn = null;
		try {
			// String url = "https://buy.itunes.apple.com/verifyReceipt";
			// //正式服务器校验
			String url = "https://sandbox.itunes.apple.com/verifyReceipt"; // sandbox
																			// 用于苹果审核时校验
			// 创建一个URL对象
			URL mURL = new URL(url);
			// 调用URL的openConnection()方法,获取HttpURLConnection对象
			conn = (HttpURLConnection) mURL.openConnection();

			conn.setRequestMethod("POST");// 设置请求方法为post
			conn.setReadTimeout(5000);// 设置读取超时为5秒
			conn.setConnectTimeout(10000);// 设置连接网络超时为10秒
			conn.setDoOutput(true);// 设置此方法,允许向服务器输出内容

			// post请求的参数
			String data = "receipt-data=" + receipt;
			// 获得一个输出流,向服务器写数据,默认情况下,系统不允许向服务器输出内容
			OutputStream out = conn.getOutputStream();// 获得一个输出流,向服务器写数据
			out.write(data.getBytes());
			out.flush();
			out.close();

			int responseCode = conn.getResponseCode();// 调用此方法就不必再使用conn.connect()方法
			if (responseCode == 200) {
				InputStream is = conn.getInputStream();
				String state = getStringFromInputStream(is);
				return state;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();// 关闭连接
			}
		}
		return null;
	}

	// 更新订单状态及会员信息
	private int updateOrderInfoAndVIPTime(String tradeNo) {
		OrderInfo orderInfo = orderInfoService.getInfoByTradeNo(tradeNo);
		orderInfo.setStatus(1);
		orderInfoService.updateOrderInfo(orderInfo); // 更新订单状态
		User user = userService.selectUser(orderInfo.getUserId());
		if (orderInfo.getType() == 3) {
			if(orderInfo.getCoin()>0){
				// 欧拉币明细记录
				updateCoinHistory("购买课程抵用",orderInfo.getUserId(),7,-orderInfo.getCoin());
			}
			// 首次购买课程赠送150欧拉币
			if(coinHistoryService.validateFirstPay(orderInfo.getUserId(), 5)==1){
				updateCoinHistory("首次购买课程赠送",orderInfo.getUserId(),5,150);
			}
			return 1;
		}
		Calendar c = Calendar.getInstance();
		try {
			// 更新会员状态
			if (user.getVipTime() == null
					|| DateUtil.getDifferSec(new Date(), user.getVipTime()) < 0) {
				Date date = new Date(); // 当前日期
				c.setTime(date);
				if (orderInfo.getType() == 1) {
					c.add(Calendar.MONTH, 1);
				} else {
					c.add(Calendar.MONTH, 6);
				}
			} else {
				c.setTimeInMillis(user.getVipTime().getTime()); // 会员有效日期
				if (orderInfo.getType() == 1) {
					c.add(Calendar.MONTH, 1);
				} else {
					c.add(Calendar.MONTH, 6);
				}
			}
			// 首次购买会员赠送100欧拉币
			if(coinHistoryService.validateFirstPay(orderInfo.getUserId(), 4)==1){
				updateCoinHistory("首次购买会员",orderInfo.getUserId(),4,100);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userService.updateVipTime(orderInfo.getUserId(), c.getTime());
	}
	
	// 更新欧拉币使用明细
	private void updateCoinHistory(String name,int userId,int type,int dealNum){
		CoinHistory dailyAct = new CoinHistory();
		dailyAct.setUserId(userId);
		dailyAct.setType(type);
		dailyAct.setDealNum(dealNum);
		dailyAct.setName(name);
		SimpleDateFormat form = new SimpleDateFormat("yyyy-MM-dd");
		dailyAct.setDate(form.format(new Date()));
		coinHistoryService.insertData(dailyAct);
	}

	/**
	 * 根据流返回一个字符串信息 *
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	private static String getStringFromInputStream(InputStream is)
			throws IOException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		// 模板代码 必须熟练
		byte[] buffer = new byte[1024];
		int len = -1;
		// 一定要写len=is.read(buffer)
		// 如果while((is.read(buffer))!=-1)则无法将数据写入buffer中
		while ((len = is.read(buffer)) != -1) {
			os.write(buffer, 0, len);
		}
		is.close();
		String state = os.toString();// 把流中的数据转换成字符串,采用的编码是utf-8(模拟器默认编码)
		os.close();
		return state;
	}

}
