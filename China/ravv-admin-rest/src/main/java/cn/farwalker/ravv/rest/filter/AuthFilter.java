package cn.farwalker.ravv.rest.filter;

import cn.farwalker.waka.auth.properties.JwtProperties;
import cn.farwalker.waka.auth.util.JwtTokenUtil;
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
import java.util.ArrayList;
import java.util.List;

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
		logger.info("进入admin.AuthFilter"); 


		final String requestHeader = request.getHeader(jwtProperties.getHeader());

		String authToken = null;

		if (requestHeader != null && requestHeader.startsWith("Bearer ")) {
			authToken = requestHeader.substring(7);
		}
		Tools.web.setContextPathFull(request);
		if(authToken!=null){
			String onlineUid = jwtTokenUtil.getPrivateClaimFromToken(authToken,"memberId");
			Tools.web.setOnlineUser(onlineUid);
		}
		
		List<String> noAuthList = new ArrayList<>();
		noAuthList.add("/" + jwtProperties.getAuthPath());
		noAuthList.add("/register");
		noAuthList.add("/wxpay/notify");
		noAuthList.add("/file/image");
		String realPath = request.getServletPath();
		for(String path : noAuthList) {
			if(StringUtils.startsWithIgnoreCase(realPath, path)) {
				chain.doFilter(request, response);//TODO 可能有bug

				return;
			}
		}

		
		if (request.getServletPath().equals("/swagger-ui.html")) {
			chain.doFilter(request, response);
			return;
		}
		//final String requestHeader = request.getHeader(jwtProperties.getHeader());
		//final String sign = request.getContentType();
		if (authToken != null){
			// 验证token是否过期,包含了验证jwt是否正确
			try {
				boolean flag = jwtTokenUtil.isTokenExpired(authToken);
				if (flag) {					
					RenderUtil.renderJson(response, RavvExceptionEnum.TOKEN_EXPIRED.getMessage());

					return;
				}
			} catch (JwtException e) {
				// 有异常就是token解析失败
				RenderUtil.renderJson(response, RavvExceptionEnum.TOKEN_VERIFICATION_FAILED);
				return;
			}
		} else {
			// header没有带Bearer字段
			RenderUtil.renderJson(response, RavvExceptionEnum.TOKEN_VERIFICATION_FAILED);
			return;
		}
		chain.doFilter(request, response); 
	}

}