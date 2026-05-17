/**
 * Main application JavaScript
 * Heaven Hotel Reservation System
 */

document.addEventListener('DOMContentLoaded', function() {
    setupGlobalHandlers();
    initializeFormValidation();
    initScrollAnimations();
    initParallax();
    initCounters();
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
 * Scroll-triggered reveal animations (Framer Motion-style)
 */
function initScrollAnimations() {
    const els = document.querySelectorAll('[data-animate]');
    if (!els.length) return;

    const observer = new IntersectionObserver(function(entries) {
        entries.forEach(function(entry) {
            if (entry.isIntersecting) {
                entry.target.classList.add('is-visible');
                observer.unobserve(entry.target);
            }
        });
    }, { threshold: 0.12, rootMargin: '0px 0px -40px 0px' });

    els.forEach(function(el) { observer.observe(el); });

    /* Auto-stagger direct children of [data-stagger] grids */
    document.querySelectorAll('[data-stagger]').forEach(function(grid) {
        Array.from(grid.children).forEach(function(child, i) {
            child.setAttribute('data-animate', 'fade-up');
            child.setAttribute('data-delay', String(Math.min(i * 100, 500)));
            observer.observe(child);
        });
    });
}

/**
 * Cinematic parallax on elements with data-parallax
 */
function initParallax() {
    const hero = document.querySelector('[data-parallax]');
    if (!hero) return;
    window.addEventListener('scroll', function() {
        const scrolled = window.pageYOffset;
        const speed = parseFloat(hero.dataset.parallax) || 0.35;
        hero.style.transform = 'translateY(' + (scrolled * speed) + 'px)';
    }, { passive: true });
}

/**
 * Animated number counter for elements with data-count-to
 */
function initCounters() {
    const counters = document.querySelectorAll('[data-count-to]');
    if (!counters.length) return;

    const observer = new IntersectionObserver(function(entries) {
        entries.forEach(function(entry) {
            if (!entry.isIntersecting) return;
            const el = entry.target;
            const target = parseFloat(el.dataset.countTo);
            const suffix = el.dataset.countSuffix || '';
            const duration = 1400;
            const start = performance.now();

            function update(now) {
                const elapsed = now - start;
                const progress = Math.min(elapsed / duration, 1);
                const eased = 1 - Math.pow(1 - progress, 3);
                const value = Math.round(eased * target);
                el.textContent = value + suffix;
                if (progress < 1) requestAnimationFrame(update);
            }
            requestAnimationFrame(update);
            observer.unobserve(el);
        });
    }, { threshold: 0.5 });

    counters.forEach(function(el) { observer.observe(el); });
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
