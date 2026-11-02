package com.servicetrack.controller;

import com.servicetrack.model.Customer;
import com.servicetrack.model.Product;
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

@WebServlet("/customer/service-request/details")
public class ServiceRequestDetailsServlet extends HttpServlet {

    private static final String MASTER_PAGE =
            "/WEB-INF/views/common/master.jsp";

    private static final String DETAILS_PAGE =
            "/WEB-INF/views/customer/service-request-details.jsp";

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

        request.setAttribute("contentPage", DETAILS_PAGE);

        String serviceRequestIdParameter =
                request.getParameter("serviceRequestId");

        try {

            if (serviceRequestIdParameter == null
                    || serviceRequestIdParameter.trim().isEmpty()) {

                throw new IllegalArgumentException(
                        "Service request ID is required."
                );
            }

            Long serviceRequestId =
                    Long.parseLong(serviceRequestIdParameter);

            ServiceRequest serviceRequest =
                    serviceRequestService.getServiceRequest(
                            serviceRequestId
                    );

            if (serviceRequest == null) {
                forwardWithError(
                        request,
                        response,
                        "Service request not found."
                );
                return;
            }

            Customer customer =
                    customerService.getCustomerByUserId(userId);

            if (customer == null) {
                forwardWithError(
                        request,
                        response,
                        "Customer profile not found."
                );
                return;
            }

            // Verify that this product belongs to logged-in customer.
            Product product =
                    productService.getProduct(
                            serviceRequest.getProductId(),
                            userId
                    );

            if (product == null) {
                forwardWithError(
                        request,
                        response,
                        "Service request not found for your account."
                );
                return;
            }

            request.setAttribute(
                    "serviceRequest",
                    serviceRequest
            );

            request.setAttribute(
                    "product",
                    product
            );

            request.setAttribute(
                    "customer",
                    customer
            );

            request.getRequestDispatcher(MASTER_PAGE)
                    .forward(request, response);

        } catch (NumberFormatException e) {

            forwardWithError(
                    request,
                    response,
                    "Invalid service request ID."
            );

        } catch (IllegalArgumentException e) {

            forwardWithError(
                    request,
                    response,
                    e.getMessage()
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load service request details.",
                    e
            );
        }
    }

    private void forwardWithError(
            HttpServletRequest request,
            HttpServletResponse response,
            String error)
            throws ServletException, IOException {

        request.setAttribute("contentPage", DETAILS_PAGE);
        request.setAttribute("error", error);

        request.getRequestDispatcher(MASTER_PAGE)
                .forward(request, response);
    }
}