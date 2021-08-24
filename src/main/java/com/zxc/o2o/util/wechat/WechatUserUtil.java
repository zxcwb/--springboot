package com.zxc.o2o.util.wechat;

import com.zxc.o2o.entity.PersonInfo;
import com.zxc.o2o.util.DesUtil;
import com.zxc.o2o.util.wechat.message.pojo.UserAccessToken;
import com.zxc.o2o.util.wechat.message.pojo.WechatUser;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Configuration
public class WechatUserUtil {

	private static Logger log = LoggerFactory.getLogger(MenuManager.class);

	private static String wechatappi;
	private static String wechatappsecret;

	@Value(value = "${wechat.wechatappi}")
	public void setWechatappi(String wechatappi) {
		WechatUserUtil.wechatappi = wechatappi;
	}

	@Value(value = "${wechat.wechatappsecret}")
	public void setWechatappsecret(String wechatappsecret) {
		WechatUserUtil.wechatappsecret = wechatappsecret;
	}

	public static void getCode() throws UnsupportedEncodingException {
		// String codeUrl =
		// "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf0e81c3bee622d60&redirect_uri="
		// + URLEncoder.encode("www.cityrun.com", "utf-8")
		// +
		// "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
	}

	//获取UserAccessToken实体类
	public static UserAccessToken getUserAccessToken(String code) throws IOException {
		//Properties pro = new Properties();
		//pro.load(WechatUserUtil.class.getClassLoader().getResourceAsStream("wechat.properties"));
		String appId = DesUtil.getDecryptMessageStr(wechatappi);
		log.debug("appId:" + appId);
		String appsecret = DesUtil.getDecryptMessageStr(wechatappsecret);
		System.out.println("APPID=="+appId);
		System.out.println("appsecret=="+appsecret);
		log.debug("secret:" + appsecret);
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="
				+ appId + "&secret=" + appsecret + "&code=" + code
				+ "&grant_type=authorization_code";

		JSONObject jsonObject = WechatUtil.httpsRequest(url, "GET", null);
		log.debug("userAccessToken:" + jsonObject.toString());
		String accessToken = jsonObject.getString("access_token");

		if (null == accessToken) {
			log.debug("获取用户accessToken失败。");
			return null;
		}

		UserAccessToken token = new UserAccessToken();
		token.setAccessToken(accessToken);
		token.setExpiresIn(jsonObject.getString("expires_in"));
		token.setOpenId(jsonObject.getString("openid"));
		token.setRefreshToken(jsonObject.getString("refresh_token"));
		token.setScope(jsonObject.getString("scope"));
		return token;
	}

	public static WechatUser getUserInfo(String accessToken, String openId) {
		String url = "https://api.weixin.qq.com/sns/userinfo?access_token="
				+ accessToken + "&openid=" + openId + "&lang=zh_CN";
		JSONObject jsonObject = WechatUtil.httpsRequest(url, "GET", null);
		WechatUser user = new WechatUser();
		String openid = jsonObject.getString("openid");
		if (openid == null) {
			log.debug("获取用户信息失败。");
			return null;
		}
		user.setOpenId(openid);
		user.setNickName(jsonObject.getString("nickname"));
		user.setSex(jsonObject.getInt("sex"));
		user.setProvince(jsonObject.getString("province"));
		user.setCity(jsonObject.getString("city"));
		user.setCountry(jsonObject.getString("country"));
		user.setHeadimgurl(jsonObject.getString("headimgurl"));
		user.setPrivilege(null);
		// user.setUnionid(jsonObject.getString("unionid"));
		return user;
	}

	public static boolean validAccessToken(String accessToken, String openId) {
		String url = "https://api.weixin.qq.com/sns/auth?access_token="
				+ accessToken + "&openid=" + openId;
		JSONObject jsonObject = WechatUtil.httpsRequest(url, "GET", null);
		int errcode = jsonObject.getInt("errcode");
		if (errcode == 0) {
			return true;
		} else {
			return false;
		}
	}

	//将WechatUser里的信息转换成PersonInfo的信息，并返回PersonInfo
	public static PersonInfo getPersonInfoFromRequest(WechatUser user) {
		PersonInfo personInfo = new PersonInfo();
		personInfo.setName(user.getNickName());
		personInfo.setGender(user.getSex() + "");
		personInfo.setProfileImg(user.getHeadimgurl());
		personInfo.setEnableStatus(1);
		return personInfo;
	}
}
