// ========================================
// PIZZERIA - Hlavny JavaScript
// ========================================

document.addEventListener('DOMContentLoaded', function() {
    
    // Auto-hide alerts po 5 sekundach
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            alert.style.transition = 'opacity 0.5s';
            alert.style.opacity = '0';
            setTimeout(function() {
                alert.remove();
            }, 500);
        }, 5000);
    });

    // Potvrdenie vymazania
    const deleteButtons = document.querySelectorAll('[data-confirm]');
    deleteButtons.forEach(function(button) {
        button.addEventListener('click', function(e) {
            const message = this.getAttribute('data-confirm') || 'Naozaj chcete vykonat tuto akciu?';
            if (!confirm(message)) {
                e.preventDefault();
            }
        });
    });

    // Automaticke odoslanie formulara pri zmene selectu
    const autoSubmitSelects = document.querySelectorAll('select[data-auto-submit]');
    autoSubmitSelects.forEach(function(select) {
        select.addEventListener('change', function() {
            this.form.submit();
        });
    });

    // Zvyraznenie aktivneho menu
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.navbar-nav a');
    navLinks.forEach(function(link) {
        if (link.getAttribute('href') === currentPath) {
            link.classList.add('active');
        }
    });

    // Validacia formularov
    const forms = document.querySelectorAll('form[data-validate]');
    forms.forEach(function(form) {
        form.addEventListener('submit', function(e) {
            const requiredFields = form.querySelectorAll('[required]');
            let valid = true;
            
            requiredFields.forEach(function(field) {
                if (!field.value.trim()) {
                    valid = false;
                    field.classList.add('error');
                } else {
                    field.classList.remove('error');
                }
            });
            
            if (!valid) {
                e.preventDefault();
                alert('Prosim vyplnte vsetky povinne polia.');
            }
        });
    });

    // Pocitadlo znakov pre textarea
    const textareas = document.querySelectorAll('textarea[maxlength]');
    textareas.forEach(function(textarea) {
        const maxLength = textarea.getAttribute('maxlength');
        const counter = document.createElement('small');
        counter.className = 'char-counter';
        counter.textContent = '0 / ' + maxLength;
        textarea.parentNode.appendChild(counter);
        
        textarea.addEventListener('input', function() {
            counter.textContent = this.value.length + ' / ' + maxLength;
        });
    });

    // Zobrazenie nahladov obrazkov
    const imageInputs = document.querySelectorAll('input[type="url"][name*="image"], input[type="url"][name*="Image"]');
    imageInputs.forEach(function(input) {
        input.addEventListener('change', function() {
            const url = this.value;
            if (url) {
                let preview = this.parentNode.querySelector('.image-preview');
                if (!preview) {
                    preview = document.createElement('img');
                    preview.className = 'image-preview';
                    preview.style.maxWidth = '200px';
                    preview.style.marginTop = '10px';
                    this.parentNode.appendChild(preview);
                }
                preview.src = url;
            }
        });
    });

    // Refresh stranky pre kuchynu a rozvoz (kazdu minutu)
    if (window.location.pathname === '/kuchyna' || window.location.pathname === '/rozvoz') {
        setTimeout(function() {
            location.reload();
        }, 60000); // 60 sekund
    }

});

// Pomocna funkcia pre formatovanie ceny
function formatPrice(price) {
    return parseFloat(price).toFixed(2) + ' EUR';
}

// Pomocna funkcia pre potvrdenie akcie
function confirmAction(message) {
    return confirm(message || 'Naozaj chcete vykonat tuto akciu?');
}
