// Pizzeria - Main JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // Auto-hide alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.transition = 'opacity 0.5s ease';
            alert.style.opacity = '0';
            setTimeout(() => alert.remove(), 500);
        }, 5000);
    });

    // Confirm delete actions
    const deleteButtons = document.querySelectorAll('[data-confirm]');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            if (!confirm(this.dataset.confirm)) {
                e.preventDefault();
            }
        });
    });

    // Form validation feedback
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            const requiredFields = form.querySelectorAll('[required]');
            let valid = true;
            
            requiredFields.forEach(field => {
                if (!field.value.trim()) {
                    field.classList.add('error-field');
                    valid = false;
                } else {
                    field.classList.remove('error-field');
                }
            });
            
            if (!valid) {
                e.preventDefault();
            }
        });
    });

    // Password confirmation
    const confirmPassword = document.getElementById('confirmPassword');
    const password = document.getElementById('password') || document.getElementById('newPassword');
    
    if (confirmPassword && password) {
        confirmPassword.addEventListener('input', function() {
            if (this.value !== password.value) {
                this.setCustomValidity('Heslá sa nezhodujú');
            } else {
                this.setCustomValidity('');
            }
        });
    }

    // Smooth scroll for anchor links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth'
                });
            }
        });
    });
});
