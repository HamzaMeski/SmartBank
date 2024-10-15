// Get the modal
var modal = document.getElementById("detailsModal");
var span = document.getElementsByClassName("close")[0];

console.log(span)

function updateStatus(requestId) {
    const statusModal = document.createElement('div');
    statusModal.className = 'status-modal';
    statusModal.innerHTML = `
        <div class="status-modal-content">
            <h2>Update Status</h2>
            <button onclick="submitStatus('${requestId}', 'APPROVED')">APPROVED</button>
            <button onclick="submitStatus('${requestId}', 'REJECTED')">REJECTED</button>
            <button onclick="submitStatus('${requestId}', 'PENDING')">PENDING</button>
            <button onclick="closeStatusModal()">Cancel</button>
        </div>
    `;
    document.body.appendChild(statusModal);
}

function submitStatus(requestId, newStatus) {
    fetch(`${contextPath}/updateStatus`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `id=${requestId}&status=${newStatus}`
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert("Status updated successfully");
                location.reload(); // Reload the page to reflect changes
            } else {
                alert("Failed to update status: " + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert("An error occurred while updating the status");
        })
        .finally(() => {
            closeStatusModal();
        });
}

function closeStatusModal() {
    const statusModal = document.querySelector('.status-modal');
    if (statusModal) {
        statusModal.remove();
    }
}


function showDetails(requestId) {
    fetch(`${contextPath}/requestDetails?id=${requestId}`)
        .then(response => response.json())
        .then(data => {
            const detailsContent = document.getElementById("detailsContent");
            detailsContent.innerHTML = `
            <p><strong>Request Number:</strong> ${data.requestNumber}</p>
            <p><strong>Name:</strong> ${data.firstName} ${data.lastName}</p>
            <p><strong>Email:</strong> ${data.email}</p>
            <p><strong>Phone:</strong> ${data.phoneNumber}</p>
            <p><strong>Amount:</strong> ${data.amount}</p>
            <p><strong>Duration:</strong> ${data.duration} months</p>
            <p><strong>Monthly Payment:</strong> ${data.monthlyPayment}</p>
            <p><strong>Project:</strong> ${data.project}</p>
            <p><strong>Employment Status:</strong> ${data.employmentStatus}</p>
            <h3>Status History</h3>
            <ul>
                ${data.requestStatuses.map(status => `
                    <li>${status.statusName} - ${new Date(status.modificationDate).toLocaleString()}</li>
                `).join('')}
            </ul>
        `;
            modal.style.display = "block";
        })
        .catch(error => {
            console.error('Error:', error);
            alert("An error occurred while fetching request details");
        });
}

// Close the modal
span.onclick = function() {
    modal.style.display = "none";
}

// Close the modal if clicked outside
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}
