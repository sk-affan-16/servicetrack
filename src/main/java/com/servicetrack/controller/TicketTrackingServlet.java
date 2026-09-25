package com.servicetrack.controller;

import com.servicetrack.model.Customer;
import com.servicetrack.model.Product;
import com.servicetrack.model.ServiceRequest;
import com.servicetrack.model.Ticket;
import com.servicetrack.service.CustomerService;
import com.servicetrack.service.ProductService;
import com.servicetrack.service.ServiceRequestService;
import com.servicetrack.service.TicketService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/customer/ticket")
public class TicketTrackingServlet extends HttpServlet {

    private final CustomerService customerService =
            new CustomerService();

    private final ProductService productService =
            new ProductService();

    private final ServiceRequestService serviceRequestService =
            new ServiceRequestService();

    private final TicketService ticketService =
            new TicketService();

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

        String ticketIdParameter =
                request.getParameter("ticketId");

        try {

            Long ticketId =
                    Long.parseLong(ticketIdParameter);

            Ticket ticket =
                    ticketService.getTicket(ticketId);

            if (ticket == null) {
                request.setAttribute(
                        "error",
                        "Ticket not found."
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/customer/ticket-tracking.jsp"
                ).forward(request, response);

                return;
            }

            ServiceRequest serviceRequest =
                    serviceRequestService.getServiceRequest(
                            ticket.getServiceRequestId()
                    );

            if (serviceRequest == null) {
                request.setAttribute(
                        "error",
                        "Service request not found."
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/customer/ticket-tracking.jsp"
                ).forward(request, response);

                return;
            }

            Customer customer =
                    customerService.getCustomerByUserId(userId);

            if (customer == null) {
                request.setAttribute(
                        "error",
                        "Customer profile not found."
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/customer/ticket-tracking.jsp"
                ).forward(request, response);

                return;
            }

            Product product =
                    productService.getProduct(
                            serviceRequest.getProductId(),
                            userId
                    );

            if (product == null) {
                request.setAttribute(
                        "error",
                        "Product not found."
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/customer/ticket-tracking.jsp"
                ).forward(request, response);

                return;
            }

            request.setAttribute(
                    "ticket",
                    ticket
            );

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

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer/ticket-tracking.jsp"
            ).forward(request, response);

        } catch (NumberFormatException e) {

            request.setAttribute(
                    "error",
                    "Invalid ticket ID."
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer/ticket-tracking.jsp"
            ).forward(request, response);

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/customer/ticket-tracking.jsp"
            ).forward(request, response);

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load ticket details.",
                    e
            );
        }
    }
}