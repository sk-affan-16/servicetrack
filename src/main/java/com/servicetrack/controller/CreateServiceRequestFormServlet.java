package com.servicetrack.controller;

import com.servicetrack.model.Product;
import com.servicetrack.service.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/customer/service-request/new")
public class CreateServiceRequestFormServlet extends HttpServlet {

    private final ProductService productService =
            new ProductService();

    private static final String MASTER_PAGE =
            "/WEB-INF/views/common/master.jsp";

    private static final String FORM_PAGE =
            "/WEB-INF/views/customer/service-request-form.jsp";

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        request.setAttribute("contentPage", FORM_PAGE);

        try {

            List<Product> products =
                    productService.getCustomerProducts(userId);

            request.setAttribute("products", products);

            request.getRequestDispatcher(MASTER_PAGE)
                    .forward(request, response);

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load customer products.",
                    e
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute("error", e.getMessage());

            request.getRequestDispatcher(MASTER_PAGE)
                    .forward(request, response);
        }
    }
}