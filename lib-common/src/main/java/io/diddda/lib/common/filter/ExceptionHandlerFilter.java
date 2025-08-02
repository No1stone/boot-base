package io.diddda.lib.common.filter;

import io.diddda.lib.common.exception.DidddaErrorException;
import io.diddda.lib.common.exception.DidddaWarnException;
import io.diddda.lib.common.web.CommonResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import io.diddda.lib.common.util.CommonResponseUtils;
import io.diddda.lib.common.util.TransformUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class ExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (DidddaErrorException e) {
            handleException(request, response, e);
        } catch (DidddaWarnException e) {
            handleException(request, response, e);
        }
    }

    private void write(CommonResponse<?> commonResponse, HttpServletResponse response) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(TransformUtils.stringify(commonResponse));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void handleException(HttpServletRequest request, HttpServletResponse response, DidddaErrorException e) {
        response.setStatus(e.getHttpStatus().value());
        log.error(e.getErrorMessage(), e);
        write(CommonResponseUtils.response(e.getResponseTypeInterface()), response);
    }

    private void handleException(HttpServletRequest request, HttpServletResponse response, DidddaWarnException e) {
        response.setStatus(e.getHttpStatus().value());
        log.warn(e.getErrorMessage(), e);
        write(CommonResponseUtils.response(e.getResponseTypeInterface()), response);
    }


}

