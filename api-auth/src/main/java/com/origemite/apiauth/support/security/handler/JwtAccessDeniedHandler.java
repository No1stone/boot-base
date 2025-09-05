/*
 * Copyright (C) Hanwha Systems Ltd., 2021. All rights reserved.
 *
 * This software is covered by the license agreement between
 * the end user and Hanwha Systems Ltd., and may be
 * used and copied only in accordance with the terms of the
 * said agreement.
 *
 * Hanwha Systems Ltd., assumes no responsibility or
 * liability for any errors or inaccuracies in this software,
 * or any consequential, incidental or indirect damage arising
 * out of the use of the software.
 */
package com.origemite.apiauth.support.security.handler;

import io.origemite.lib.common.exception.BizErrorException;
import io.origemite.lib.common.web.ResponseType;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 *  AccessDecisionManager(Voter) 결정 후, 권한이 없는 리소스에 접근시 AccessDenied된 이벤트에 대한 핸들링을 처리한다.
 */
@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

	//private final ObjectMapper objectMapper;


//	public JwtAccessDeniedHandler(ObjectMapper objectMapper) {
//		this.objectMapper = objectMapper;
//	}


	@Override
	public void handle(HttpServletRequest request
					 , HttpServletResponse response
					 , AccessDeniedException accessDeniedException) throws IOException, ServletException {

//		log.error("[JwtAccessDeniedHandler] : Access denied.");
//		// 403
//		response.setStatus(HttpStatus.FORBIDDEN.value());
//		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//		response.setCharacterEncoding("UTF-8");
//
//		Map<String, Object> deniedResponse = new HashMap<String, Object>();
//		deniedResponse.put("code",    INVALID_ACCESS.getCode());
//		deniedResponse.put("message", "Forbidden Resource. 권한이 없는 리소스에 접근을 시도하였습니다.");
//		objectMapper.writeValue(response.getWriter(), deniedResponse);
		throw new BizErrorException(ResponseType.FORBIDDEN);
	}

}
