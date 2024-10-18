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

    const validateAge = (dateOfBirth) => {
        const birthDate = new Date(dateOfBirth);
        const age = (new Date().getFullYear()) - birthDate.getFullYear();
        return age >= 18 && age <= 65;
    };

    const validateEmploymentDuration = (employmentStartDate) => {
        const startDate = new Date(employmentStartDate);
        const yearsEmployed = (new Date().getFullYear()) - startDate.getFullYear();
        return yearsEmployed >= 1;
    };

    const validateInput = (input) => {
        const pattern = validationPatterns[input.name];
        let isValid = true;
        let errorMessage = "";

        if (pattern) {
            isValid = pattern.test(input.value);
            errorMessage = isValid ? "" : `Invalid ${input.name}`;
        } else if (input.name === "dateNaissance") {
            isValid = validateAge(input.value);
            errorMessage = isValid ? "" : "Age must be between 18 and 65";
        } else if (input.name === "dateEmbauche") {
            isValid = validateEmploymentDuration(input.value);
            errorMessage = isValid ? "" : "Minimum employment duration is 1 year";
        } else if (input.name === "montant") {
            const amount = parseFloat(input.value);
            isValid = amount >= 1000 && amount <= 50000;
            errorMessage = isValid ? "" : "Amount must be between 1,000 and 50,000";
        } else if (input.name === "duree") {
            const duration = parseInt(input.value);
            isValid = duration >= 12 && duration <= 60;
            errorMessage = isValid ? "" : "Duration must be between 12 and 60 months";
        }

        input.setCustomValidity(errorMessage);
        if (!isValid) {
            input.reportValidity();
        }
        return isValid;
    };

    const updateRecap = () => {
        recapContent.innerHTML = '';
        Object.keys(formData).forEach((key) => {
            const fieldElement = document.createElement('p');
            fieldElement.classList.add("recap-personel");
            fieldElement.innerHTML = `<strong>${key}</strong>: ${formData[key]}`;
            recapContent.appendChild(fieldElement);
        });
    };

    const showSection = (index) => {
        formSections.forEach((section, i) => {
            section.style.display = i === index ? "block" : "none";
        });
        steps.forEach((step, i) => {
            step.classList.toggle("active", i === index);
        });
        currentStep = index;
    };

    const isSectionValid = (sectionIndex) => {
        const inputs = formSections[sectionIndex].querySelectorAll("input, select, textarea");
        return Array.from(inputs).every(input => {
            if (input.type === 'radio') {
                const name = input.name;
                const radioGroup = document.querySelectorAll(`input[name="${name}"]`);
                return Array.from(radioGroup).some(radio => radio.checked);
            }
            return input.checkValidity() && validateInput(input);
        });
    };

    const toggleStepNavigation = () => {
        stepIndicators.forEach((step, index) => {
            step.classList.toggle("navigable", isSectionValid(index));
        });
    };

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

    showSection(currentStep);

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

    stepIndicators.forEach((step, index) => {
        step.addEventListener("click", () => {
            if (isSectionValid(index)) {
                showSection(index);
            }
        });
    });

    const montantInput = document.getElementById("montant");
    const montantRange = document.getElementById("amount-range");
    const dureeInput = document.getElementById("duree");
    const dureeRange = document.getElementById("duration-range");
    const mensualitesInput = document.getElementById("mensualites");
    const mensualitesRange = document.getElementById("monthly-range");

    const calculateLoan = (changedField) => {
        const montant = parseFloat(montantInput.value);
        const duree = parseInt(dureeInput.value);
        const tauxAnnuel = 0.05;
        const tauxMensuel = tauxAnnuel / 12;

        if (changedField !== 'mensualites') {
            const mensualite = (montant * tauxMensuel) / (1 - Math.pow(1 + tauxMensuel, -duree));
            mensualitesInput.value = mensualitesRange.value = mensualite.toFixed(2);
        } else {
            const mensualite = parseFloat(mensualitesInput.value);
            const calculatedMontant = (mensualite * (1 - Math.pow(1 + tauxMensuel, -duree))) / tauxMensuel;
            montantInput.value = montantRange.value = calculatedMontant.toFixed(2);
        }

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

    calculateLoan('montant');

    const creditRadioGroup = document.getElementById("credit-radio-group");
    const additionalInputsContainer = document.getElementById("additional-inputs");

    creditRadioGroup.addEventListener("change", (event) => {
        additionalInputsContainer.style.display = event.target.value === "Oui" ? "block" : "none";
    });

    const form = document.getElementById('creditForm');
    form.addEventListener('submit', function(event) {
        if (!isFormValid()) {
            event.preventDefault();
            alert('Please fill all fields correctly before submitting.');
        }
    });

    function isFormValid() {
        const allInputs = form.querySelectorAll('input, select, textarea');
        for (let input of allInputs) {
            if (!validateInput(input)) {
                return false;
            }
        }
        return true;
    }
});
