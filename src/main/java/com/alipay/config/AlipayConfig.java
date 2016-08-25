package com.alipay.config;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *版本：3.3
 *日期：2012-08-10
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
	
 *提示：如何获取安全校验码和合作身份者ID
 *1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *2.点击“商家服务”(https://b.alipay.com/order/myOrder.htm)
 *3.点击“查询合作者身份(PID)”、“查询安全校验码(Key)”

 *安全校验码查看时，输入支付密码后，页面呈灰色的现象，怎么办？
 *解决方法：
 *1、检查浏览器配置，不让浏览器做弹框屏蔽设置
 *2、更换浏览器或电脑，重新登录查询。
 */

public class AlipayConfig {
	
	//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088221471342703";
	// 商户的私钥,使用支付宝自带的openssl工具生成。
	public static String private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALlYAcO2CloCO/QtRtOy5CtD+r8x7s3P1saTMq1MSFFB5QGTLZWGgNeGlsPXcU0nTt2EzuQZOC6kT3B+RGWea93KMA/cF8zBqFi88tb9IQj4SLJ2jsZ+VA2yys3qe+UTJ7tZnn4R6+fbZ6hboqfvyPRGiOEU1hjcEtv8xHBZiPsTAgMBAAECgYEAm61fNgsGoNm1kAAjuJdO7URZLhT712VWqk5jR4qGR7JIb003AZwLRnymz/J7GWoF5SyzNoY0WYDrtLpqKsypHh5kl219CpQks9C/vRmO4oPRf3C2fA0cJmnrZGYNhwBlK8/bcX7C1AAUIWf/IyligtvrS3TSugJSeGBK/Hgr7NkCQQD0cYdZ4bt/Ilh+PWWDYCNJsFo5jvPfmiljwFj6ADgku+7SoAadPULC0HhiLMnVGhi+7QMYM7GstSocFyJJvweNAkEAwhszjAbAj/HhZUcsnPtNql9QQvYxR5ZjDhsXJQQslwIV+UHiYqtGO/BfsYEWtDMt2iV/waVL55BHzvrYUoqVHwJASCw1AG7Lw0/KvQs3q8B46srs+M2iZ8TGr7MdEkN6UC5E519kVIy/53FR+fqrZQYdhFHKdhzdOtcKpdyWvRtwCQJAFF1+PwbGVVPvbgzVpXLIZ0OwzBXAfd5NbkFxYDTb6MwTOh+rfOaza76Ai8MberST7vtYOBz7pcQBhLnU0HQxyQJAamf+VY3g0Y3E0HVSE6GD2IK8snXkdwMDtISkXhqh77QBeUIbhZtYvkRL1/GpnWLTabgziRcZ6sMhealUB84Rkw==";
	
	// 支付宝的公钥，无需修改该值
	public static String ali_public_key  = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";
	
	public static String seller = "developer@olaxueyuan.com";

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "UTF-8";
	
	// 签名方式 不需修改
	public static String sign_type = "RSA";

}
