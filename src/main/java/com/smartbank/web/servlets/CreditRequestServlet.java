package com.smartbank.web.servlets;

import com.smartbank.business.CreditRequestService;
import com.smartbank.business.impl.CreditRequestServiceImpl;
import com.smartbank.dao.impl.CreditRequestDAOImpl;
import com.smartbank.dao.impl.StatusDAOImpl;
import com.smartbank.exception.ValidationException;
import com.smartbank.model.CreditRequest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/creditRequest")
public class CreditRequestServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(CreditRequestServlet.class.getName());
    private CreditRequestService creditRequestService;

    @Override
    public void init() throws ServletException {
        LOGGER.info("Servlet Initialization");
        creditRequestService = new CreditRequestServiceImpl(new CreditRequestDAOImpl(), new StatusDAOImpl());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("Servlet POST method called");
        logFormFields(request);
        try {
            CreditRequest creditRequest = createCreditRequestFromParameters(request);
            creditRequestService.submitCreditRequest(creditRequest);
            response.sendRedirect(request.getContextPath() + "/jsp/success.jsp");
        } catch(ValidationException ve) {
            LOGGER.info("Validation error occured: "+ ve.getErrors());
            request.setAttribute("errors", ve.getErrors());
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception occurred: ", e);
            request.setAttribute("error", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/jsp/error.jsp").forward(request, response);
        }
    }

    private void logFormFields(HttpServletRequest request) {
        LOGGER.info("Form Fields:");
        request.getParameterMap().forEach((key, value) ->
                LOGGER.info(key + ": " + String.join(", ", value))
        );
    }

    private CreditRequest createCreditRequestFromParameters(HttpServletRequest request) throws DateTimeParseException {
        CreditRequest creditRequest = new CreditRequest();

        creditRequest.setProject(request.getParameter("projet"));
        creditRequest.setEmploymentStatus(request.getParameter("status"));
        creditRequest.setAmount(parseDoubleOrDefault(request.getParameter("montant"), 0.0));
        creditRequest.setDuration(parseIntOrDefault(request.getParameter("duree"), 0));
        creditRequest.setMonthlyPayment(parseDoubleOrDefault(request.getParameter("mensualites"), 0.0));
        creditRequest.setEmail(request.getParameter("email"));
        creditRequest.setPhoneNumber(request.getParameter("telephone"));
        creditRequest.setTitle(request.getParameter("civilite"));
        creditRequest.setLastName(request.getParameter("nom"));
        creditRequest.setFirstName(request.getParameter("prenom"));
        creditRequest.setIdentificationNumber(request.getParameter("cin"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String dateNaissanceStr = request.getParameter("dateNaissance");
        String dateEmbaucheStr = request.getParameter("dateEmbauche");

        if (dateNaissanceStr != null && !dateNaissanceStr.isEmpty()) {
            creditRequest.setDateOfBirth(LocalDate.parse(dateNaissanceStr, formatter));
        }
        if (dateEmbaucheStr != null && !dateEmbaucheStr.isEmpty()) {
            creditRequest.setEmploymentStartDate(LocalDate.parse(dateEmbaucheStr, formatter));
        }

        creditRequest.setMonthlyIncome(parseDoubleOrDefault(request.getParameter("revenuMensuel"), 0.0));
        creditRequest.setHasOngoingCredits("oui".equalsIgnoreCase(request.getParameter("creditEnCours")));

        return creditRequest;
    }

    private double parseDoubleOrDefault(String value, double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            LOGGER.warning("Failed to parse double value: " + value);
            return defaultValue;
        }
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            LOGGER.warning("Failed to parse int value: " + value);
            return defaultValue;
        }
    }
}
