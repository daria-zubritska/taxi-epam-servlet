package controller;

import model.UserDAO;
import model.entity.User;
import utils.RequestUtils;
import utils.Security;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "LoginServlet", value = "/logIn")
public class LoginServlet extends HttpServlet {

    private static String PATH = "/WEB-INF/pages/login.jsp";
    private final static String USER_ATTRIBUTE = "user";
    private final static String ERROR_ATTRIBUTE = "error";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //if user is already logged in send them to order page
        if (RequestUtils.getSessionAttribute(req, USER_ATTRIBUTE, User.class) != null) {
            resp.sendRedirect("/makeOrder");
        } else {
            req.getRequestDispatcher(PATH).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (RequestUtils.getSessionAttribute(req, USER_ATTRIBUTE, User.class) != null) {
            resp.sendRedirect("/");
            return;
        }

        UserDAO userDAO = UserDAO.getInstance();

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        Map<String, String> viewAttributes = new HashMap<>();
        viewAttributes.put("email", email);

        //check if values are valid
        if(!Security.isEmailValid(email)) {
            viewAttributes.put(ERROR_ATTRIBUTE, "emailNotValid");
            passErrorToView(req, resp, viewAttributes);
            return;
        }
        if(!Security.isPasswordValid(password)) {
            viewAttributes.put(ERROR_ATTRIBUTE, "passwordNotValid");
            passErrorToView(req, resp, viewAttributes);
            return;
        }

        User user = userDAO.findUserByEmail(email);

        //check if user exists
        if(user == null) {
            viewAttributes.put(ERROR_ATTRIBUTE, "userNull");
            passErrorToView(req, resp, viewAttributes);
            return;
        }
        try {
            //check if password is correct
            if(!Security.isPasswordCorrect(password, user.getPassword())) {
                viewAttributes.put(ERROR_ATTRIBUTE, "passwordIncorrect");
                passErrorToView(req, resp, viewAttributes);
                return;
            }
        } catch (Exception e) {
            viewAttributes.put(ERROR_ATTRIBUTE, "somethingWrong");
            passErrorToView(req, resp, viewAttributes);
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute(USER_ATTRIBUTE, user);

        //set session and redirect user to page according to their role
        session.setMaxInactiveInterval(1800); // 30 minutes

        if(user.getRole().isUser()) {
            resp.sendRedirect("/makeOrder");
        }else{
            resp.sendRedirect("/statistics");
        }
    }

    private void passErrorToView(HttpServletRequest req, HttpServletResponse resp, Map<String, String> viewAttributes) throws ServletException, IOException {
        for(Map.Entry<String, String> entry : viewAttributes.entrySet())
            req.setAttribute(entry.getKey(), entry.getValue());

        req.getRequestDispatcher(PATH).forward(req, resp);
    }
}

