package io.diddda.lib.common.util;

import io.diddda.lib.common.exception.RequestNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Slf4j
public class WebUtils {

    /**
     * 컨텍스트 영역에있는 리퀘스트를 조회
     */
    public static HttpServletRequest getRequest() throws RequestNotFoundException {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            throw new RequestNotFoundException("RequestAttributes is null");
        }
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    /**
     * 파라미터 값을 가져옴
     */
    public static <T> T getParameter(String name) throws RequestNotFoundException {
        return getParameter(getRequest(), name);
    }


    /**
     * 파라미터 값을 가져옴
     */
    public static <T> T getParameter(HttpServletRequest request, String name) {
        if (request == null || request.getParameter(name) == null) return null;
        return (T) request.getParameter(name);
    }

    /**
     * 사용자 에이전트 조회
     *
     * @return 유저에이전트 문자열
     */
    public static String getUserAgent() throws RequestNotFoundException {
        return getRequest().getHeader("user-agent");
    }

    /**
     * 클라이언트 아이피 조회
     *
     * @return IP
     */
    public static String getRemoteAddr() throws RequestNotFoundException {
        return getRemoteAddr(getRequest());
    }

    /**
     * 클라이언트 아이피 조회
     *
     * @param request 리퀘스트
     * @return IP
     */
    public static String getRemoteAddr(HttpServletRequest request) {

        if (request == null) return null;

        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null) {
            ip = request.getHeader("X-RealIP");
        }
        if (ip == null) {
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null) {
            ip = request.getRemoteAddr();
        }
        if (StringUtils.contains(ip, ",")) {
            ip = StringUtils.splitAt(ip, ",", 0);
        }
        if (StringUtils.equals(ip, "0:0:0:0:0:0:0:1")) {
            ip = "127.0.0.1";
        }
        return ip;
    }


    /**
     * URI에서 채널 조회
     *
     * @return
     */
    public static String getChannel() {
        try {
            return getRequest().getHeader("DRP-CHANNEL");
        } catch (RequestNotFoundException e) {
            return null;
        }
    }

    /**
     * URI에서 채널 조회
     *
     * @return
     */
    public static String getChannelUrl() {
        try {
            return StringUtils.matchesGet(getRequest().getRequestURI(), "\\/[a-z-]+\\/(api-[a-z-]+)\\/[a-z-/]+", 1);
        } catch (RequestNotFoundException e) {
            return null;
        }
    }

    /**
     * HEADER에서 서비스 조회
     *
     * @return
     */
    public static String getServiceId() {
        try {
            return getRequest().getHeader("DRP-SERVICE-ID");
        } catch (RequestNotFoundException e) {
            return null;
        }
    }

    public static String getHeader(HttpServletRequest request, String name) {
        if (request == null || request.getHeader(name) == null) return null;
        return request.getHeader(name);
    }



}
