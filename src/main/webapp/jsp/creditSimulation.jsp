<%--
  Created by IntelliJ IDEA.
  User: hamza
  Date: 10/2/24
  Time: 4:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Simulateur de Crédit</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">
</head>
<body>
<div class="container">
    <div class="form-container">
        <nav class="steps">
            <div class="step active" data-step="1">Simuler mon crédit</div>
            <div class="step" data-step="2">Mes coordonnées</div>
            <div class="step" data-step="3">Mes infos personnelles</div>
        </nav>

        <form id="creditForm" action="${pageContext.request.contextPath}/creditRequest" method="POST">
            <div class="step-content" id="step1">
                <h2>Simuler mon crédit</h2>
                <div class="form-group">
                    <label for="projet">Mon projet:</label>
                    <select id="projet" name="projet" required>
                        <option value="">Sélectionnez votre projet</option>
                        <option value="immobilier">Immobilier</option>
                        <option value="automobile">Automobile</option>
                        <option value="personnel">Personnel</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="status">Je suis:</label>
                    <select id="status" name="status" required>
                        <option value="">Sélectionnez votre statut</option>
                        <option value="fonctionnaire">Fonctionnaire</option>
                        <option value="salarie">Salarié</option>
                        <option value="independant">Indépendant</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="montant">Montant (en DH):</label>
                    <input type="number" id="montant" name="montant" required min="0">
                    <input type="range" id="montantRange" min="0" max="1000000" step="1000">
                </div>
                <div class="form-group">
                    <label for="duree">Durée (en mois):</label>
                    <input type="number" id="duree" name="duree" required min="1">
                    <input type="range" id="dureeRange" min="1" max="120" step="1">
                </div>
                <div class="form-group">
                    <label for="mensualites">Mensualités (en DH):</label>
                    <input type="number" id="mensualites" name="mensualites" required min="0">
                    <input type="range" id="mensualitesRange" min="0" max="100000" step="100">
                </div>
                <button type="button" class="next-step">Continuer sans engagement</button>
            </div>

            <div class="step-content" id="step2" style="display: none;">
                <h2>Mes coordonnées</h2>
                <div class="form-group">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" required>
                </div>
                <div class="form-group">
                    <label for="telephone">Téléphone mobile:</label>
                    <input type="tel" id="telephone" name="telephone" required>
                </div>
                <button type="button" class="next-step">Continuer sans engagement</button>
            </div>

            <div class="step-content" id="step3" style="display: none;">
                <h2>Mes infos personnelles</h2>
                <div class="form-group">
                    <label>Civilité:</label>
                    <div class="radio-group">
                        <input type="radio" id="madame" name="civilite" value="madame" required>
                        <label for="madame">Madame</label>
                        <input type="radio" id="mademoiselle" name="civilite" value="mademoiselle">
                        <label for="mademoiselle">Mademoiselle</label>
                        <input type="radio" id="monsieur" name="civilite" value="monsieur">
                        <label for="monsieur">Monsieur</label>
                    </div>
                </div>
                <div class="form-group">
                    <label for="nom">Nom:</label>
                    <input type="text" id="nom" name="nom" required>
                </div>
                <div class="form-group">
                    <label for="prenom">Prénom:</label>
                    <input type="text" id="prenom" name="prenom" required>
                </div>
                <div class="form-group">
                    <label for="cin">Numéro CIN / Carte de séjour:</label>
                    <input type="text" id="cin" name="cin" required>
                </div>
                <div class="form-group">
                    <label for="dateNaissance">Date de naissance:</label>
                    <input type="date" id="dateNaissance" name="dateNaissance" required>
                </div>
                <div class="form-group">
                    <label for="dateEmbauche">Date d'embauche/début de l'activité:</label>
                    <input type="date" id="dateEmbauche" name="dateEmbauche" required>
                </div>
                <div class="form-group">
                    <label for="revenuMensuel">Total revenus mensuels (net en DH):</label>
                    <input type="number" id="revenuMensuel" name="revenuMensuel" required min="0">
                </div>
                <div class="form-group">
                    <label>Avez-vous des crédits en cours ?</label>
                    <div class="radio-group">
                        <input type="radio" id="creditOui" name="creditEnCours" value="oui" required>
                        <label for="creditOui">Oui</label>
                        <input type="radio" id="creditNon" name="creditEnCours" value="non">
                        <label for="creditNon">Non</label>
                    </div>
                </div>
                <button type="submit">Demander ce crédit</button>
            </div>
        </form>
        <!-- listing loan demands -->
        <div class="credit-requests-container" style="display: none;">
            <h2>Liste des demandes de crédit</h2>
            <div class="filter-controls">
                <input type="date" id="filterDate" name="filterDate">
                <select id="filterStatus" name="filterStatus">
                    <option value="">Tous les statuts</option>
                    <option value="PENDING">En attente</option>
                    <option value="APPROVED">Approuvé</option>
                    <option value="REJECTED">Rejeté</option>
                </select>
                <button id="applyFilter">Appliquer le filtre</button>
            </div>
            <table id="creditRequestsTable">
                <thead>
                <tr>
                    <th>Numéro</th>
                    <th>Date</th>
                    <th>Montant</th>
                    <th>Durée</th>
                    <th>Statut</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <!-- Credit requests will be populated here -->
                </tbody>
            </table>
        </div>

        <div id="statusUpdateModal" class="modal" style="display: none;">
            <div class="modal-content">
                <h3>Mettre à jour le statut</h3>
                <select id="newStatus">
                    <option value="PENDING">En attente</option>
                    <option value="APPROVED">Approuvé</option>
                    <option value="REJECTED">Rejeté</option>
                </select>
                <button id="updateStatus">Mettre à jour</button>
                <button id="closeModal">Fermer</button>
            </div>
        </div>
    </div>

    <div class="recap-container">
        <h2>Mon récapitulatif</h2>
        <div id="recap"></div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
