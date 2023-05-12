package com.svalero.servlets;
import com.svalero.dao.Database;
import com.svalero.dao.PlayerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

@WebServlet("/addedit-player")
public class AddEditPlayerServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String firstName = request.getParameter("firstName").trim();
        String lastName = request.getParameter("lastName").trim();
        String nickname = request.getParameter("nickname").trim();
        String email = request.getParameter("email").trim();
        LocalDate registrationDate = LocalDate.parse(request.getParameter("registrationDate"));

        String id, idTemp = null;
        String action = request.getParameter("action");
        if (action.equals("edit")) {
            idTemp = request.getParameter("id");
        }
        id = idTemp;

        try {
            Database.connect();
            if (action.equals("edit")) {
                Database.jdbi.withExtension(PlayerDAO.class, dao -> {
                    dao.modifyPlayer(firstName, lastName, nickname, email, registrationDate, id);
                    return null;
                });
                out.println("<div style='margin-top: 20px;' class='alert alert-success' role='alert'>Player <%= firstName %> <%= lastName %> successfully modified.</div>");
            } else {
                Database.jdbi.withExtension(PlayerDAO.class, dao -> {
                    dao.addPlayer(firstName, lastName, nickname, email, registrationDate);
                    return null;
                });
                out.println("<div style='margin-top: 20px;' class='alert alert-success' role='alert'>Player <%= firstName %> <%= lastName %> successfully added to database.</div>");
            }
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }
}
