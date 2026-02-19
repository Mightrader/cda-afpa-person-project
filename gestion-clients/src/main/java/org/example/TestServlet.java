package org.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.println("<html><body>");
        out.println("<h1>Liste des clients</h1>");

        ClientDAO dao = new ClientDAO();
        List<String> clients = dao.getAllClients();

        if (clients.isEmpty()) {
            out.println("<p>Aucun client en base.</p>");
        } else {
            out.println("<ul>");
            for (String c : clients) {
                out.println("<li>" + c + "</li>");
            }
            out.println("</ul>");
        }

        out.println("</body></html>");
    }
}
