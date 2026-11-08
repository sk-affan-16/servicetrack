
package com.servicetrack.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/technician/")
public class TechnicianHomeServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute(
                "contentPage",
                "/WEB-INF/views/technician/home.jsp"
        );

        request.getRequestDispatcher(
                "/WEB-INF/views/common/master.jsp"
        ).forward(request, response);
    }
}
