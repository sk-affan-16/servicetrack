package com.servicetrack.controller;

import com.servicetrack.model.SparePart;
import com.servicetrack.service.InventoryService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/inventory")
public class InventoryServlet extends HttpServlet {

    private InventoryService inventoryService;

    @Override
    public void init() {
        inventoryService = new InventoryService();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        loadInventory(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        try {

            if ("add".equals(action)) {
                addSparePart(request);

            } else if ("update".equals(action)) {
                updateStock(request);

            } else if ("delete".equals(action)) {
                deleteSparePart(request);

            } else {
                response.sendError(
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Invalid inventory action."
                );
                return;
            }

            response.sendRedirect(
                    request.getContextPath() + "/admin/inventory"
            );

        } catch (NumberFormatException e) {

            request.setAttribute(
                    "error",
                    "Please enter valid numeric values."
            );

            loadInventory(request, response);

        } catch (IllegalArgumentException e) {

            request.setAttribute(
                    "error",
                    e.getMessage()
            );

            loadInventory(request, response);

        } catch (SQLException e) {

            log("Unable to process inventory operation.", e);

            request.setAttribute(
                    "error",
                    "Unable to process inventory operation."
            );

            loadInventory(request, response);
        }
    }

    private void addSparePart(
            HttpServletRequest request)
            throws SQLException {

        String partName = request.getParameter("partName");
        String partNumber = request.getParameter("partNumber");

        int quantity = Integer.parseInt(
                request.getParameter("quantity")
        );

        BigDecimal unitPrice = new BigDecimal(
                request.getParameter("unitPrice")
        );

        inventoryService.addSparePart(
                partName,
                partNumber,
                quantity,
                unitPrice
        );
    }

    private void updateStock(
            HttpServletRequest request)
            throws SQLException {

        Long sparePartId = Long.parseLong(
                request.getParameter("sparePartId")
        );

        int quantity = Integer.parseInt(
                request.getParameter("quantity")
        );

        inventoryService.updateStock(
                sparePartId,
                quantity
        );
    }

    private void deleteSparePart(
            HttpServletRequest request)
            throws SQLException {

        Long sparePartId = Long.parseLong(
                request.getParameter("sparePartId")
        );

        inventoryService.deleteSparePart(
                sparePartId
        );
    }

    private void loadInventory(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        try {

            List<SparePart> spareParts =
                    inventoryService.getAllSpareParts();

            request.setAttribute(
                    "spareParts",
                    spareParts
            );

            request.getRequestDispatcher(
                    "/WEB-INF/views/admin/inventory.jsp"
            ).forward(request, response);

        } catch (SQLException e) {

            log("Unable to load inventory.", e);

            response.sendError(
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Unable to load inventory."
            );
        }
    }
}