package com.servicetrack.controller;

import com.servicetrack.model.Customer;
import com.servicetrack.model.ServiceRequest;
import com.servicetrack.service.CustomerService;
import com.servicetrack.service.ProductService;
import com.servicetrack.service.ServiceRequestService;
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

@WebServlet("/customer/service-requests")
public class ServiceRequestListServlet extends HttpServlet {

    private final CustomerService customerService =
            new CustomerService();

    private final ProductService productService =
            new ProductService();

    private final ServiceRequestService serviceRequestService =
            new ServiceRequestService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

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

        try {

            Customer customer =
                    customerService.getCustomerByUserId(userId);

            if (customer == null) {
                request.setAttribute(
                        "error",
                        "Customer profile not found."
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/customer/service-requests.jsp"
                ).forward(request, response);

                return;
            }

            List<ServiceRequest> serviceRequests =
                    new ArrayList<>();

            var products =
                    productService.getCustomerProducts(userId);

            for (var product : products) {

                List<ServiceRequest> productRequests =
                        serviceRequestService
                                .getServiceRequestsByProduct(
                                        product.getProductId()
                                );

                if (productRequests != null) {
                    serviceRequests.addAll(
                            productRequests
                    );
                }
            }

            request.setAttribute(
                    "serviceRequests",
                    serviceRequests
            );

            request.setAttribute(
                    "products",
                    products
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer/service-requests.jsp"
            ).forward(request, response);

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load service requests.",
                    e
            );
        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer/service-requests.jsp"
            ).forward(request, response);
        }
    }
}