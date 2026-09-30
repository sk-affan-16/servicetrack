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
import java.time.LocalDate;
import java.util.List;

@WebServlet("/customer/products")
public class ProductServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() {
        productService = new ProductService();
    }

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

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        try {
            List<Product> products =
                    productService.getCustomerProducts(userId);

            request.setAttribute("products", products);

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer-products.jsp"
            ).forward(request, response);

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer-products.jsp"
            ).forward(request, response);

        } catch (SQLException e) {

            log("Unable to load customer products.", e);

            request.setAttribute(
                    "error",
                    "Unable to load products."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer-products.jsp"
            ).forward(request, response);
        }
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        String productName =
                request.getParameter("productName");

        String brand =
                request.getParameter("brand");

        String modelNumber =
                request.getParameter("modelNumber");

        String serialNumber =
                request.getParameter("serialNumber");

        String purchaseDateText =
                request.getParameter("purchaseDate");

        try {

            LocalDate purchaseDate =
                    LocalDate.parse(purchaseDateText);

            productService.registerProduct(
                    userId,
                    productName,
                    brand,
                    modelNumber,
                    serialNumber,
                    purchaseDate
            );

            response.sendRedirect(
                    request.getContextPath()
                            + "/customer/products"
            );

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            loadProductsAndForward(
                    request,
                    response,
                    userId
            );

        } catch (SQLException e) {

            log("Unable to register product.", e);

            request.setAttribute(
                    "error",
                    "Unable to register product."
            );

            loadProductsAndForward(
                    request,
                    response,
                    userId
            );
        }
    }

    private void loadProductsAndForward(
            HttpServletRequest request,
            HttpServletResponse response,
            Long userId)
            throws ServletException, IOException {

        try {

            List<Product> products =
                    productService.getCustomerProducts(userId);

            request.setAttribute(
                    "products",
                    products
            );

        } catch (SQLException e) {

            log("Unable to reload products.", e);

            request.setAttribute(
                    "error",
                    "Unable to load products."
            );
        }

        request.getRequestDispatcher(
                "/WEB-INF/views/customer-products.jsp"
        ).forward(request, response);
    }
}