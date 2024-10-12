document.addEventListener("DOMContentLoaded", () => {
    const steps = document.querySelectorAll(".step");
    const formSections = document.querySelectorAll("section.loan-form");
    const continueButtons = document.querySelectorAll(".btn-submit");
    const recapContent = document.getElementById("recap-content");
    const stepIndicators = document.querySelectorAll(".steps .step");
    let currentStep = 0;

    const formData = {};

    // Validation regex patterns
    const validationPatterns = {
        email: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
        telephone: /^0[567]\d{8}$/,
        nom: /^[A-Za-zÀ-ÿ\s'-]{2,50}$/,
        prenom: /^[A-Za-zÀ-ÿ\s'-]{2,50}$/,
        cin: /^[A-Z]{1,2}\d{5,6}$/,
        revenuMensuel: /^\d+(\.\d{1,2})?$/
    };

    // Function to validate input against regex
    const validateInput = (input) => {
        const pattern = validationPatterns[input.name];
        if (pattern) {
            const isValid = pattern.test(input.value);
            input.setCustomValidity(isValid ? "" : "Invalid input");
            return isValid;
        }
        return true;
    };

    // Function to update the recap section
    const updateRecap = () => {
        recapContent.innerHTML = '';
        Object.keys(formData).forEach((key) => {
            const fieldElement = document.createElement('p');
            fieldElement.classList.add("recap-personel");
            fieldElement.innerHTML = `<strong>${key}</strong>: ${formData[key]}`;
            recapContent.appendChild(fieldElement);
        });
    };

    // Function to navigate to a particular section
    const showSection = (index) => {
        formSections.forEach((section, i) => {
            section.style.display = i === index ? "block" : "none";
        });
        steps.forEach((step, i) => {
            step.classList.toggle("active", i === index);
        });
        currentStep = index;
    };

    // Check if all required fields in a section are filled and valid
    const isSectionValid = (sectionIndex) => {
        const inputs = formSections[sectionIndex].querySelectorAll("input, select, textarea");
        return Array.from(inputs).every(input => input.checkValidity() && validateInput(input));
    };

    // Enable navigation between sections if all sections are filled
    const toggleStepNavigation = () => {
        stepIndicators.forEach((step, index) => {
            step.classList.toggle("navigable", isSectionValid(index));
        });
    };

    // Real-time form data collection and validation
    const inputs = document.querySelectorAll("input, select, textarea");
    inputs.forEach((input) => {
        input.addEventListener("input", () => {
            const name = input.name;
            if (name) {
                formData[name] = input.value;
                validateInput(input);
                updateRecap();
                toggleStepNavigation();
            }
        });
    });

    // Initial display
    showSection(currentStep);

    // Continue button event listener for moving to the next section
    continueButtons.forEach((button, index) => {
        button.addEventListener("click", () => {
            if (isSectionValid(currentStep)) {
                if (currentStep < formSections.length - 1) {
                    showSection(currentStep + 1);
                }
            } else {
                alert("Please fill all the fields correctly in this section before continuing.");
            }
        });
    });

    // Step navigation by clicking on step titles
    stepIndicators.forEach((step, index) => {
        step.addEventListener("click", () => {
            if (isSectionValid(index)) {
                showSection(index);
            }
        });
    });

    // Loan calculation logic
    const montantInput = document.getElementById("montant");
    const montantRange = document.getElementById("amount-range");
    const dureeInput = document.getElementById("duree");
    const dureeRange = document.getElementById("duration-range");
    const mensualitesInput = document.getElementById("mensualites");
    const mensualitesRange = document.getElementById("monthly-range");

    const calculateLoan = (changedField) => {
        const montant = parseFloat(montantInput.value);
        const duree = parseInt(dureeInput.value);
        const tauxAnnuel = 0.05; // 5% annual interest rate (adjust as needed)
        const tauxMensuel = tauxAnnuel / 12;

        if (changedField !== 'mensualites') {
            const mensualite = (montant * tauxMensuel) / (1 - Math.pow(1 + tauxMensuel, -duree));
            mensualitesInput.value = mensualitesRange.value = mensualite.toFixed(2);
        } else {
            const mensualite = parseFloat(mensualitesInput.value);
            const calculatedMontant = (mensualite * (1 - Math.pow(1 + tauxMensuel, -duree))) / tauxMensuel;
            montantInput.value = montantRange.value = calculatedMontant.toFixed(2);
        }

        // Update formData
        formData['montant'] = montantInput.value;
        formData['duree'] = dureeInput.value;
        formData['mensualites'] = mensualitesInput.value;

        updateRecap();
    };

    const syncInputs = (inputElement, rangeElement, field) => {
        inputElement.addEventListener("input", () => {
            rangeElement.value = inputElement.value;
            calculateLoan(field);
        });
        rangeElement.addEventListener("input", () => {
            inputElement.value = rangeElement.value;
            calculateLoan(field);
        });
    };

    syncInputs(montantInput, montantRange, 'montant');
    syncInputs(dureeInput, dureeRange, 'duree');
    syncInputs(mensualitesInput, mensualitesRange, 'mensualites');

    // Initial calculation
    calculateLoan('montant');

    // Show/Hide additional fields based on radio selection
    const creditRadioGroup = document.getElementById("credit-radio-group");
    const additionalInputsContainer = document.getElementById("additional-inputs");

    creditRadioGroup.addEventListener("change", (event) => {
        additionalInputsContainer.style.display = event.target.value === "Oui" ? "block" : "none";
    });
});
