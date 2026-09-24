package com.servicetrack.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter({
        "/customer/*",
        "/technician/*",
        "/admin/*"
})
public class RoleAuthorizationFilter implements Filter {

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest =
                (HttpServletRequest) request;

        HttpServletResponse httpResponse =
                (HttpServletResponse) response;

        HttpSession session =
                httpRequest.getSession(false);

        if (session == null) {
            httpResponse.sendRedirect(
                    httpRequest.getContextPath()
                            + "/login"
            );
            return;
        }

        String role =
                (String) session.getAttribute("role");

        if (role == null) {
            session.invalidate();

            httpResponse.sendRedirect(
                    httpRequest.getContextPath()
                            + "/login"
            );
            return;
        }

        String requestURI = httpRequest.getRequestURI();

        String contextPath =
                httpRequest.getContextPath();

        String path =
                requestURI.substring(
                        contextPath.length()
                );

        if (path.startsWith("/customer/")
                && "CUSTOMER".equals(role)) {

            chain.doFilter(request, response);
            return;
        }

        if (path.startsWith("/technician/")
                && "TECHNICIAN".equals(role)) {

            chain.doFilter(request, response);
            return;
        }

        if (path.startsWith("/admin/")
                && "ADMIN".equals(role)) {

            chain.doFilter(request, response);
            return;
        }

        httpResponse.sendError(
                HttpServletResponse.SC_FORBIDDEN,
                "Access denied."
        );
    }
}