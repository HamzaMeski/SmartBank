document.addEventListener("DOMContentLoaded", () => {
    const steps = document.querySelectorAll(".step");
    const formSections = document.querySelectorAll("section.loan-form");
    const continueButtons = document.querySelectorAll(".btn-submit");
    const recapContent = document.getElementById("recap-content");
    const stepIndicators = document.querySelectorAll(".steps .step");
    let currentStep = 0;

    // Form data object to capture filled values
    const formData = {};

    // Function to update the recap section
    const updateRecap = () => {
        recapContent.innerHTML = '';  // Clear existing content
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

    // Check if all required fields in a section are filled
    const isSectionValid = (sectionIndex) => {
        const inputs = formSections[sectionIndex].querySelectorAll("input, select, textarea");
        return Array.from(inputs).every(input => input.checkValidity());
    };

    // Enable navigation between sections if all sections are filled
    const toggleStepNavigation = () => {
        stepIndicators.forEach((step, index) => {
            step.classList.toggle("navigable", isSectionValid(index));
        });
    };

    // Real-time form data collection
    const inputs = document.querySelectorAll("input, select, textarea");
    inputs.forEach((input) => {
        input.addEventListener("input", () => {
            const name = input.name;
            if (name) {
                formData[name] = input.value;
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
                alert("Please fill all the fields in this section before continuing.");
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

    // Form submit handler to display data
    /*
    document.getElementById("creditForm").addEventListener("submit", (event) => {
        event.preventDefault();
        if (isSectionValid(formSections.length - 1)) {
            alert(`Form Submitted Successfully!\nForm Data: ${JSON.stringify(formData, null, 2)}`);
        } else {
            alert("Please fill all the fields in the last section before submitting.");
        }
    });
    */

    // Range input handlers for syncing with number inputs
    const amountInput = document.getElementById("montant");
    const amountRange = document.getElementById("amount-range");
    const durationInput = document.getElementById("duree");
    const durationRange = document.getElementById("duration-range");
    const mensualitesInput = document.getElementById("mensualites");
    const mensualitesRange = document.getElementById("monthly-range");

    const syncInputs = (inputElement, rangeElement) => {
        inputElement.addEventListener("input", () => {
            rangeElement.value = inputElement.value;
            formData[inputElement.name] = inputElement.value;
            updateRecap();
        });
        rangeElement.addEventListener("input", () => {
            inputElement.value = rangeElement.value;
            formData[inputElement.name] = rangeElement.value;
            updateRecap();
        });
    };

    syncInputs(amountInput, amountRange);
    syncInputs(durationInput, durationRange);
    syncInputs(mensualitesInput, mensualitesRange);

    // Show/Hide additional fields based on radio selection
    const creditRadioGroup = document.getElementById("credit-radio-group");
    const additionalInputsContainer = document.getElementById("additional-inputs");

    creditRadioGroup.addEventListener("change", (event) => {
        if (event.target.value === "Oui") {
            additionalInputsContainer.style.display = "block";
        } else {
            additionalInputsContainer.style.display = "none";
        }
    });
});
