package com.servicetrack.controller;

import com.servicetrack.model.Customer;
import com.servicetrack.model.Product;
import com.servicetrack.model.ServiceRequest;
import com.servicetrack.model.Ticket;
import com.servicetrack.model.User;
import com.servicetrack.service.AuthService;
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

    private static final String MASTER_PAGE =
            "/WEB-INF/views/common/master.jsp";

    private static final String TICKET_PAGE =
            "/WEB-INF/views/customer/ticket-tracking.jsp";

    private final AuthService authService =
            new AuthService();

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

        request.setAttribute("contentPage", TICKET_PAGE);

        String ticketIdParameter =
                request.getParameter("ticketId");

        try {

            if (ticketIdParameter == null
                    || ticketIdParameter.trim().isEmpty()) {

                throw new IllegalArgumentException(
                        "Ticket ID is required."
                );
            }

            Long ticketId =
                    Long.parseLong(ticketIdParameter);

            Ticket ticket =
                    ticketService.getTicket(ticketId);

            if (ticket == null) {
                forwardWithError(
                        request,
                        response,
                        "Ticket not found."
                );
                return;
            }

            ServiceRequest serviceRequest =
                    serviceRequestService.getServiceRequest(
                            ticket.getServiceRequestId()
                    );

            if (serviceRequest == null) {
                forwardWithError(
                        request,
                        response,
                        "Service request not found."
                );
                return;
            }

            // Verify customer profile.
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

            // Load logged-in customer details.
            User user =
                    authService.getUserById(userId);

            if (user == null) {
                forwardWithError(
                        request,
                        response,
                        "User information not found."
                );
                return;
            }

            // Verify that the product belongs to this customer.
            Product product =
                    productService.getProduct(
                            serviceRequest.getProductId(),
                            userId
                    );

            if (product == null) {
                forwardWithError(
                        request,
                        response,
                        "Ticket information not found for your account."
                );
                return;
            }

            request.setAttribute("ticket", ticket);
            request.setAttribute("serviceRequest", serviceRequest);
            request.setAttribute("product", product);
            request.setAttribute("customer", customer);
            request.setAttribute("user", user);

            request.getRequestDispatcher(MASTER_PAGE)
                    .forward(request, response);

        } catch (NumberFormatException e) {

            forwardWithError(
                    request,
                    response,
                    "Invalid ticket ID."
            );

        } catch (IllegalArgumentException e) {

            forwardWithError(
                    request,
                    response,
                    e.getMessage()
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Unable to load ticket details.",
                    e
            );
        }
    }

    private void forwardWithError(
            HttpServletRequest request,
            HttpServletResponse response,
            String error)
            throws ServletException, IOException {

        request.setAttribute("contentPage", TICKET_PAGE);
        request.setAttribute("error", error);

        request.getRequestDispatcher(MASTER_PAGE)
                .forward(request, response);
    }
}