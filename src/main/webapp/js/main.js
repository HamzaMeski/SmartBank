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

    // Credit calculation function
    function calculateMonthlyPayment(amount, duration, interestRate) {
        const monthlyRate = interestRate / 12 / 100;
        return (amount * monthlyRate * Math.pow(1 + monthlyRate, duration)) / (Math.pow(1 + monthlyRate, duration) - 1);
    }

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

        const amount = parseFloat(document.getElementById('montant').value);
        const duration = parseInt(document.getElementById('duree').value);
        const interestRate = 5; // Example fixed interest rate, adjust as needed

        if (!isNaN(amount) && !isNaN(duration)) {
            const monthlyPayment = calculateMonthlyPayment(amount, duration, interestRate);
            formData['Mensualité calculée'] = monthlyPayment.toFixed(2) + ' DH';
        }

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

    // Function to submit credit request
    function submitCreditRequest() {
        const formData = new FormData(form);
        fetch('/creditSimulation', {
            method: 'POST',
            body: formData
        })
            .then(response => response.json())
            .then(data => {
                alert('Demande de crédit soumise avec succès. Numéro de demande: ' + data.requestNumber);
                loadCreditRequests();
            })
            .catch(error => console.error('Error:', error));
    }

    // Function to load credit requests
    function loadCreditRequests() {
        const date = document.getElementById('filterDate').value;
        const status = document.getElementById('filterStatus').value;

        fetch(`/creditRequests?date=${date}&status=${status}`)
            .then(response => response.json())
            .then(data => {
                const tableBody = document.querySelector('#creditRequestsTable tbody');
                tableBody.innerHTML = '';
                data.forEach(request => {
                    const row = `
                    <tr>
                        <td>${request.numero}</td>
                        <td>${request.date}</td>
                        <td>${request.montant} DH</td>
                        <td>${request.duree} mois</td>
                        <td>${request.etat}</td>
                        <td>
                            <button onclick="showStatusUpdateModal('${request.numero}')">Modifier statut</button>
                            <button onclick="showRequestDetails('${request.numero}')">Détails</button>
                        </td>
                    </tr>
                `;
                    tableBody.innerHTML += row;
                });
            })
            .catch(error => console.error('Error:', error));
    }

    // Function to show status update modal
    function showStatusUpdateModal(requestNumber) {
        const modal = document.getElementById('statusUpdateModal');
        modal.style.display = 'block';
        modal.dataset.requestNumber = requestNumber;
    }

    // Function to update request status
    function updateRequestStatus() {
        const modal = document.getElementById('statusUpdateModal');
        const requestNumber = modal.dataset.requestNumber;
        const newStatus = document.getElementById('newStatus').value;

        fetch('/updateCreditRequestStatus', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ requestNumber, newStatus })
        })
            .then(response => response.json())
            .then(data => {
                alert('Statut mis à jour avec succès');
                modal.style.display = 'none';
                loadCreditRequests();
            })
            .catch(error => console.error('Error:', error));
    }

    // Function to show request details
    function showRequestDetails(requestNumber) {
        fetch(`/creditRequestDetails?requestNumber=${requestNumber}`)
            .then(response => response.json())
            .then(data => {
                alert(`Détails de la demande ${requestNumber}:\n${JSON.stringify(data, null, 2)}`);
            })
            .catch(error => console.error('Error:', error));
    }

    // Event listeners
    document.getElementById('applyFilter').addEventListener('click', loadCreditRequests);
    document.getElementById('updateStatus').addEventListener('click', updateRequestStatus);
    document.getElementById('closeModal').addEventListener('click', () => {
        document.getElementById('statusUpdateModal').style.display = 'none';
    });

    // Handle form submission
    form.addEventListener('submit', (e) => {
        e.preventDefault();
        if (validateStep(currentStep)) {
            submitCreditRequest();
        }
    });

    // Initialize the form
    updateStepVisibility();

    // Add input event listeners to all form fields
    form.querySelectorAll('input, select').forEach(input => {
        input.addEventListener('input', updateRecap);
    });

    // Load credit requests when the page loads
    loadCreditRequests();
});

// These functions are global so they can be called from inline event handlers
window.showStatusUpdateModal = function(requestNumber) {
    const modal = document.getElementById('statusUpdateModal');
    modal.style.display = 'block';
    modal.dataset.requestNumber = requestNumber;
};

window.showRequestDetails = function(requestNumber) {
    fetch(`/creditRequestDetails?requestNumber=${requestNumber}`)
        .then(response => response.json())
        .then(data => {
            alert(`Détails de la demande ${requestNumber}:\n${JSON.stringify(data, null, 2)}`);
        })
        .catch(error => console.error('Error:', error));
};
