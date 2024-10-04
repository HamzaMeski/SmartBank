document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('creditForm');
    const steps = document.querySelectorAll('.step');
    const stepContents = document.querySelectorAll('.step-content');
    const nextButtons = document.querySelectorAll('.next-step');
    const recap = document.getElementById('recap');
    let currentStep = 1;
    const formData = {};

    // Synchronize range inputs with number inputs
    const syncInputs = (inputId, rangeId) => {
        const input = document.getElementById(inputId);
        const range = document.getElementById(rangeId);
        input.addEventListener('input', () => {
            range.value = input.value;
            updateRecap();
        });
        range.addEventListener('input', () => {
            input.value = range.value;
            updateRecap();
        });
    };

    syncInputs('montant', 'montantRange');
    syncInputs('duree', 'dureeRange');
    syncInputs('mensualites', 'mensualitesRange');

    // Update recap
    const updateRecap = () => {
        const allInputs = form.querySelectorAll('input, select');
        allInputs.forEach(input => {
            if (input.name && (input.type !== 'radio' || input.checked)) {
                formData[input.name] = input.value;
            }
        });

        // Remove any empty keys
        Object.keys(formData).forEach(key => {
            if (!key.trim()) {
                delete formData[key];
            }
        });

        recap.innerHTML = Object.entries(formData)
            .map(([key, value]) => `<p><strong>${key}:</strong> ${value}</p>`)
            .join('');
    };

    // Handle next step buttons and step navigation
    nextButtons.forEach(button => {
        button.addEventListener('click', () => {
            if (validateStep(currentStep)) {
                currentStep++;
                updateStepVisibility();
            }
        });
    });

    steps.forEach((step, index) => {
        step.addEventListener('click', () => {
            if (validateStep(currentStep)) {
                currentStep = index + 1;
                updateStepVisibility();
            }
        });
    });

    // Validate step
    const validateStep = (step) => {
        const currentInputs = stepContents[step - 1].querySelectorAll('input, select');
        let isValid = true;
        currentInputs.forEach(input => {
            if (input.required && !input.value) {
                isValid = false;
                input.classList.add('error');
            } else {
                input.classList.remove('error');
            }
        });
        return isValid;
    };

    // Update step visibility
    const updateStepVisibility = () => {
        stepContents.forEach((content, index) => {
            content.style.display = index === currentStep - 1 ? 'block' : 'none';
        });
        steps.forEach((step, index) => {
            step.classList.toggle('active', index === currentStep - 1);
        });
        updateRecap();
    };

    // Handle form submission
    form.addEventListener('submit', (e) => {
        e.preventDefault();
        if (validateStep(currentStep)) {
            console.log('Form submitted:', formData);
            // Here you can send the formData to your server or perform any other action
        }
    });

    // Initialize the form
    updateStepVisibility();

    // Add input event listeners to all form fields
    form.querySelectorAll('input, select').forEach(input => {
        input.addEventListener('input', updateRecap);
    });
});
