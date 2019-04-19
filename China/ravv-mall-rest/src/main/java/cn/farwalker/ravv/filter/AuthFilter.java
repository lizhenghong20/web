package cn.farwalker.ravv.filter;

import cn.farwalker.waka.auth.properties.JwtProperties;
import cn.farwalker.waka.auth.util.JwtTokenUtil;
import cn.farwalker.waka.core.JsonResult;
import cn.farwalker.waka.core.RavvExceptionEnum;
import cn.farwalker.waka.core.RenderUtil;
import cn.farwalker.waka.util.Tools;
import io.jsonwebtoken.JwtException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 对客户端请求的jwt token验证过滤器
 *
 * @author Jason Chen
 * @Date 2018/02/12 14:04
 */
public class AuthFilter extends OncePerRequestFilter {

	private final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private JwtProperties jwtProperties;


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		logger.info("进入AuthFilter");
		Tools.web.setContextPathFull(request);
		String realPath = request.getServletPath();
		logger.info("请求路径: "+  realPath);

		//不进行token验证的请求集合,不用写全路径
		Set<String> releaseSet = new HashSet<>();
		releaseSet.add("/auth");
		releaseSet.add("/goods");
		releaseSet.add("/web");
		releaseSet.add("/test");
		releaseSet.add("/flash_sale");
		releaseSet.add("/youtube_live/main_page");
		releaseSet.add("/qiniu/token");
		releaseSet.add("/style");
		releaseSet.add("/style_control.css");
		releaseSet.add("/order_pay_notify");

		//必须进行token验证的请求集合，必须写全路径。
		Set<String> filterSet = new HashSet<>();
		filterSet.add("/youtube_live/main_page/follow");
		filterSet.add("/pay/get_paypal_url");
		filterSet.add("/pay/get_paypal_url_by_order");
		filterSet.add("/pay/is_pay_success");
		filterSet.add("/pay/refund");
		filterSet.add("/auth/validator");

		////可登录可不登录的请求集合，必须写全路径。
		Set<String> middleSet = new HashSet<>();
		middleSet.add("/goods/details");
		middleSet.add("/goods/question_detail");
		middleSet.add("/goods/get_keyword_history");
		middleSet.add("/youtube_live/main_page/get_suggested_streamers");
		middleSet.add("/youtube_live/main_page/get_one_streamer");
		middleSet.add("/youtube_live/main_page/video_play_info");
		middleSet.add("/goods/search");
		middleSet.add("/web/get_search_key");
		middleSet.add("/paypal_callback/pay_return");


		if(filterSet.contains(realPath)){
			mustLogin (request, response,chain);
			return;
		}else if(middleSet.contains(realPath)){
			notHaveToLogin (request,response,chain);
			return;
		}else{
			for(String path : releaseSet) {
				if(StringUtils.startsWithIgnoreCase(realPath, path)) {
					chain.doFilter(request, response);
					return;
				}
			}
			mustLogin (request, response,chain);
			return;
		}

	}

	//一定要登录
	private void mustLogin (HttpServletRequest request, HttpServletResponse response,FilterChain chain)
			throws IOException, ServletException{
		logger.info("进入token过滤器，必须登录");
		final String requestHeader = request.getHeader(jwtProperties.getHeader());
		String authToken = null;
		if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
			authToken = requestHeader.substring(7);
		}

		if (authToken != null){
			// 验证token是否过期,包含了验证jwt是否正确
			try {
				boolean flag = jwtTokenUtil.isTokenExpired(authToken);
				if (flag) {
					RenderUtil.renderJson(response, JsonResult.newFail(RavvExceptionEnum.TOKEN_EXPIRED.getCode(),
							RavvExceptionEnum.TOKEN_EXPIRED.getMessage()));
					return;
				}
				String userId = jwtTokenUtil.getPrivateClaimFromToken(authToken,"memberId");
				Long memberId = Tools.web.setOnlineUser(userId);
				request.getSession().setAttribute("memberId",memberId);
			} catch (JwtException e) {
				// 有异常就是token解析失败
				RenderUtil.renderJson(response, JsonResult.newFail(RavvExceptionEnum.TOKEN_VERIFICATION_FAILED.getCode(),
						RavvExceptionEnum.TOKEN_VERIFICATION_FAILED.getMessage()));
				return;
			}
		} else {
			RenderUtil.renderJson(response,RavvExceptionEnum.TOKEN_VERIFICATION_FAILED);
			return;
		}
		chain.doFilter(request, response);
	}

	//可以不登录
	private void notHaveToLogin (HttpServletRequest request, HttpServletResponse response,FilterChain chain)
			throws IOException, ServletException{
		logger.info("进入token过滤器，可以不登录");
		final String requestHeader = request.getHeader(jwtProperties.getHeader());
		String authToken = null;
		if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
			logger.info("取到token");
			authToken = requestHeader.substring(7);
		}
		if(authToken == null){
			logger.info("未取到token");
			//如果取不到token，代表用户已经退出登录，则把memberId 置null
			if(request.getSession().getAttribute("memberId") != null)
				request.getSession().setAttribute("memberId" ,null);
			chain.doFilter(request, response);
			return;
		} else{
			// 验证token是否过期,包含了验证jwt是否正确,如果不正确，或者过期，则向下过滤
			try {
				boolean flag = jwtTokenUtil.isTokenExpired(authToken);
				if (flag) {
					//若token过期则把memberId 置null
					if(request.getSession().getAttribute("memberId") != null)
						request.getSession().setAttribute("memberId" ,null);
					chain.doFilter(request, response);
					return;
				}
				long memberId =Long.parseLong(jwtTokenUtil.getPrivateClaimFromToken(authToken,"memberId"));
				request.getSession().setAttribute("memberId",memberId);
				logger.info("token 验证通过");
				chain.doFilter(request, response);
				return;
			} catch (JwtException e) {
				// 有异常向下过滤,清除登录信息
				if(request.getSession().getAttribute("memberId") != null)
					request.getSession().setAttribute("memberId" ,null);
				chain.doFilter(request, response);
				return;
			}
		}

	}




}