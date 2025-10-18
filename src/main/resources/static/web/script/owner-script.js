// Sidebar Toggle
const sidebarToggle = document.getElementById('sidebarToggle');
const menuToggle = document.getElementById('menuToggle');
const sidebar = document.querySelector('.sidebar');

if (sidebarToggle) {
    sidebarToggle.addEventListener('click', () => {
        sidebar.classList.toggle('active');
    });
}

if (menuToggle) {
    menuToggle.addEventListener('click', () => {
        sidebar.classList.toggle('active');
    });
}

// Close sidebar when clicking outside
document.addEventListener('click', (e) => {
    if (!e.target.closest('.sidebar') && !e.target.closest('.menu-toggle') && !e.target.closest('.sidebar-toggle')) {
        sidebar.classList.remove('active');
    }
});

// Update page title based on current route
function updatePageTitle() {
    const currentPath = window.location.pathname;
    const titleMap = {
        '/owner/dashboard': 'Dashboard',
        '/owner/fields': 'Manage Fields',
        '/owner/bookings': 'Bookings',
        '/owner/vouchers': 'Vouchers',
        '/owner/news': 'News',
        '/owner/tournaments': 'Tournaments'
    };

    for (const [path, title] of Object.entries(titleMap)) {
        if (currentPath.includes(path)) {
            document.getElementById('pageTitle').textContent = title;
            break;
        }
    }
}

updatePageTitle();


// User avatar click
const userAvatar = document.querySelector('.user-avatar');
if (userAvatar) {
    userAvatar.addEventListener('click', () => {
        alert('User profile menu');
    });
}

// Filter functionality
const filterSelects = document.querySelectorAll('.filter-select');
filterSelects.forEach(select => {
    select.addEventListener('change', (e) => {
        console.log('Filter changed:', e.target.value);
        // Add filter logic here
    });
});

// Smooth transitions
window.addEventListener('load', () => {
    document.body.style.opacity = '1';
});

// Export button
const exportBtn = document.querySelector('#exportBtn');
if (exportBtn) {
    exportBtn.addEventListener('click', () => {
        alert('Export data as CSV');
        // Add export functionality here
    });
}

// Responsive adjustments
function handleResize() {
    const width = window.innerWidth;
    if (width > 768) {
        sidebar.classList.remove('active');
    }
}

window.addEventListener('resize', handleResize);

// Form submission handling
const forms = document.querySelectorAll('.form');
forms.forEach(form => {
    form.addEventListener('submit', (e) => {
        // e.preventDefault(); // Keep default submit behavior
        console.log('Form submitted:', new FormData(form));
    });
});

// Keyboard shortcuts
document.addEventListener('keydown', (e) => {
    // Ctrl/Cmd + K for search
    if ((e.ctrlKey || e.metaKey) && e.key === 'k') {
        e.preventDefault();
        if (searchInput) searchInput.focus();
    }

    // Ctrl/Cmd + B for sidebar toggle
    if ((e.ctrlKey || e.metaKey) && e.key === 'b') {
        e.preventDefault();
        sidebar.classList.toggle('active');
    }
});


console.log('Owner Dashboard Script Loaded');