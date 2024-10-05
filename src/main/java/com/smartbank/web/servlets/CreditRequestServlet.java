package com.smartbank.web.servlets;

import com.smartbank.business.CreditRequestService;
import com.smartbank.business.impl.CreditRequestServiceImpl;
import com.smartbank.dao.impl.CreditRequestDAOImpl;
import com.smartbank.dao.impl.StatusDAOImpl;
import com.smartbank.model.CreditRequest;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@WebServlet("/creditRequest")
public class CreditRequestServlet extends HttpServlet {

    private CreditRequestService creditRequestService;

    @Override
    public void init() throws ServletException {
        creditRequestService = new CreditRequestServiceImpl(new CreditRequestDAOImpl(), new StatusDAOImpl());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            CreditRequest creditRequest = createCreditRequestFromParameters(request);
            creditRequestService.submitCreditRequest(creditRequest);
            response.sendRedirect(request.getContextPath() + "/confirmation.jsp");
        } catch (Exception e) {
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private CreditRequest createCreditRequestFromParameters(HttpServletRequest request) throws ParseException {
        CreditRequest creditRequest = new CreditRequest();

        creditRequest.setProject(request.getParameter("projet"));
        creditRequest.setEmploymentStatus(request.getParameter("status"));
        creditRequest.setAmount(Double.parseDouble(request.getParameter("montant")));
        creditRequest.setDuration(Integer.parseInt(request.getParameter("duree")));
        creditRequest.setMonthlyPayment(Double.parseDouble(request.getParameter("mensualites")));
        creditRequest.setEmail(request.getParameter("email"));
        creditRequest.setPhoneNumber(request.getParameter("telephone"));
        creditRequest.setTitle(request.getParameter("civilite"));
        creditRequest.setLastName(request.getParameter("nom"));
        creditRequest.setFirstName(request.getParameter("prenom"));
        creditRequest.setIdentificationNumber(request.getParameter("cin"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        creditRequest.setDateOfBirth(dateFormat.parse(request.getParameter("dateNaissance")));
        creditRequest.setEmploymentStartDate(dateFormat.parse(request.getParameter("dateEmbauche")));

        creditRequest.setMonthlyIncome(Double.parseDouble(request.getParameter("revenuMensuel")));
        creditRequest.setHasOngoingCredits(request.getParameter("creditEnCours").equals("oui"));

        return creditRequest;
    }
}
