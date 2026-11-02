package com.servicetrack.controller;

import com.servicetrack.model.Product;
import com.servicetrack.model.Warranty;
import com.servicetrack.service.ProductService;
import com.servicetrack.service.WarrantyService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/customer/warranty")
public class WarrantyServlet extends HttpServlet {

    private WarrantyService warrantyService;
    private ProductService productService;

    private static final String MASTER_PAGE =
            "/WEB-INF/views/common/master.jsp";

    private static final String WARRANTY_PAGE =
            "/WEB-INF/views/customer-warranty.jsp";

    @Override
    public void init() {
        warrantyService = new WarrantyService();
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

        Long userId =
                (Long) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect(
                    request.getContextPath() + "/login"
            );
            return;
        }

        request.setAttribute("contentPage", WARRANTY_PAGE);

        try {

            List<Product> products =
                    productService.getCustomerProducts(userId);

            List<ProductWarranty> productWarranties =
                    new ArrayList<>();

            for (Product product : products) {

                Warranty warranty =
                        warrantyService.getWarranty(
                                userId,
                                product.getProductId()
                        );

                productWarranties.add(
                        new ProductWarranty(
                                product,
                                warranty
                        )
                );
            }

            request.setAttribute(
                    "productWarranties",
                    productWarranties
            );

            request.getRequestDispatcher(MASTER_PAGE)
                    .forward(request, response);

        } catch (SQLException e) {

            log("Unable to load warranty page.", e);

            request.setAttribute(
                    "error",
                    "Unable to load warranty information."
            );

            request.getRequestDispatcher(MASTER_PAGE)
                    .forward(request, response);
        }
    }

    public static class ProductWarranty {

        private final Product product;
        private final Warranty warranty;

        public ProductWarranty(
                Product product,
                Warranty warranty) {

            this.product = product;
            this.warranty = warranty;
        }

        public Product getProduct() {
            return product;
        }

        public Warranty getWarranty() {
            return warranty;
        }

        public String getEffectiveStatus() {
            if (warranty == null) return "NOT AVAILABLE";
            return com.servicetrack.dao.WarrantyDAO.computeEffectiveStatus(
                    warranty.getWarrantyStatus(), warranty.getWarrantyStart(),
                    warranty.getWarrantyEnd(), java.time.LocalDate.now());
        }
    }
}