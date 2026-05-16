/**
 * Main application JavaScript
 * Heaven Hotel Reservation System
 */

document.addEventListener('DOMContentLoaded', function() {
    setupGlobalHandlers();
    initializeFormValidation();
});

/**
 * Setup global event handlers
 */
function setupGlobalHandlers() {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            dismissAlert(alert);
        }, 6000);
    });

    document.body.addEventListener('click', function(event) {
        const button = event.target.closest('[data-bs-dismiss="alert"]');
        if (!button) return;

        const alert = button.closest('.alert');
        if (alert) {
            event.preventDefault();
            dismissAlert(alert);
        }
    });

    const deleteButtons = document.querySelectorAll('[data-action="delete"]');
    deleteButtons.forEach(function(btn) {
        btn.addEventListener('click', function(e) {
            if (!confirm('Are you sure you want to delete this item? This action cannot be undone.')) {
                e.preventDefault();
            }
        });
    });
}

/**
 * Dismiss alert element with animation
 */
function dismissAlert(alert) {
    if (!alert || alert.classList.contains('opacity-0')) {
        return;
    }

    alert.classList.add('transition', 'duration-300', 'opacity-0');
    setTimeout(function() {
        alert.remove();
    }, 300);
}

/**
 * Initialize form validation
 */
function initializeFormValidation() {
    const forms = document.querySelectorAll('.needs-validation');
    Array.from(forms).forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });
}

/**
 * Show notification message
 */
function showNotification(message, type = 'info') {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible`;
    alertDiv.role = 'alert';
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close ml-4" data-bs-dismiss="alert" aria-label="Close">×</button>
    `;

    const mainContent = document.querySelector('main');
    if (mainContent) {
        mainContent.insertBefore(alertDiv, mainContent.firstChild);
        setTimeout(function() {
            dismissAlert(alertDiv);
        }, 6000);
    }
}

/**
 * Set button loading state
 */
function setButtonLoading(button, isLoading = true) {
    if (!button) return;
    if (isLoading) {
        button.disabled = true;
        button.dataset.originalText = button.innerHTML;
        button.innerHTML = '<span class="inline-block h-4 w-4 animate-spin rounded-full border-2 border-white border-t-transparent mr-2"></span>Loading...';
    } else {
        button.disabled = false;
        button.innerHTML = button.dataset.originalText || 'Submit';
    }
}

/**
 * Make AJAX request with CSRF token
 */
function ajaxRequest(url, method = 'GET', data = null, callback = null) {
    const csrfToken = document.querySelector('meta[name="_csrf"]')?.getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]')?.getAttribute('content');

    const options = {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        }
    };

    if (csrfHeader && csrfToken) {
        options.headers[csrfHeader] = csrfToken;
    }

    if (data && (method === 'POST' || method === 'PUT' || method === 'DELETE')) {
        options.body = JSON.stringify(data);
    }

    fetch(url, options)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            if (callback) {
                callback(data);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            showNotification('An error occurred. Please try again.', 'danger');
        });
}

/**
 * Format currency
 */
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
    }).format(amount);
}

/**
 * Format date
 */
function formatDate(date) {
    return new Intl.DateTimeFormat('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
    }).format(new Date(date));
}

/**
 * Toggle mobile menu
 */
function toggleMobileMenu() {
    const menu = document.getElementById('mobile-menu');
    if (menu) {
        menu.classList.toggle('hidden');
    }
}

/**
 * Validate email
 */
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

/**
 * Validate phone
 */
function isValidPhone(phone) {
    const phoneRegex = /^[+]?[0-9]{7,15}$/;
    return phoneRegex.test(phone);
}

/**
 * Clear form
 */
function clearForm(formId) {
    const form = document.getElementById(formId);
    if (form) {
        form.reset();
        form.classList.remove('was-validated');
    }
}
