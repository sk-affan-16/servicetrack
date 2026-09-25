package com.servicetrack.controller;

import com.servicetrack.model.Ticket;
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

@WebServlet("/customer/service-request/create")
public class CreateServiceRequestServlet extends HttpServlet {

    private final ProductService productService =
            new ProductService();

    private final ServiceRequestService serviceRequestService =
            new ServiceRequestService();

    @Override
    protected void doPost(
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

        String productIdParameter =
                request.getParameter("productId");

        String complaintDescription =
                request.getParameter("complaintDescription");

        try {

            Long productId =
                    Long.parseLong(productIdParameter);

            productService.getProduct(
                    productId,
                    userId
            );

            Ticket ticket =
                    serviceRequestService
                            .createServiceRequestWithTicket(
                                    productId,
                                    complaintDescription
                            );

            response.sendRedirect(
                    request.getContextPath()
                            + "/customer/ticket?ticketId="
                            + ticket.getTicketId()
            );

        } catch (NumberFormatException e) {

            request.setAttribute(
                    "error",
                    "Invalid product selected."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer/service-request-form.jsp"
            ).forward(request, response);

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer/service-request-form.jsp"
            ).forward(request, response);

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to create service request.",
                    e
            );
        }
    }
}