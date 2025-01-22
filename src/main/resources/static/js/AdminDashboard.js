document.addEventListener('DOMContentLoaded', function() {
    // Toggle sidebar visibility
    document.querySelector('.menu-btn').addEventListener('click', function () {
        document.querySelector('.sidebar').classList.toggle('hidden');
        document.querySelector('.main-content').classList.toggle('expanded');
    });

   // Get elements
    
   // Toggle options menu
    document.getElementById('create-btn').addEventListener('click', function() {
        const optionsMenu = document.getElementById('options-menu');
        optionsMenu.classList.toggle('show');
    });

    // Show task popup
    document.querySelector('.options-menu button').addEventListener('click', function() {
        document.getElementById('task-popup').style.display = 'flex';
    });

    // Hide task popup
    document.getElementById('close-btn').addEventListener('click', function() {
        document.getElementById('task-popup').style.display = 'none';
    });

    // Close task popup when 'Save Task' button is clicked
    document.querySelector('#task-popup button[type="button"]').addEventListener('click', function() {
        document.getElementById('task-popup').style.display = 'none';
    });







    // Show profile dropdown
    document.querySelector('.profile-btn').addEventListener('click', function() {
        const profileMenu = document.getElementById('profile-menu');
        profileMenu.classList.toggle('show');
    });

   




    // Close options menu when clicking outside
    document.addEventListener('click', function (event) {
        if (!optionsMenu.contains(event.target) && !createBtn.contains(event.target)) {
            optionsMenu.classList.remove('show');
        }
    });

    // Initialize Bootstrap modal
    $('.add-icon').click(function() {
        $('#createTeamModal').modal('show');
    });
    $(document).ready(function() {
        $('#teamMembers').select2({
            placeholder: "Select team members",
            allowClear: true,
            closeOnSelect: false // Keeps the dropdown open after selection
        });
    });
});

function showTab(tabName) {
    var tabs = document.getElementsByClassName('task-content');
    for (var i = 0; i < tabs.length; i++) {
        tabs[i].style.display = 'none';
    }
    document.getElementById(tabName).style.display = 'block';
}
const profileBtn = document.querySelector('.profile-btn');
const profileMenu = document.getElementById('profile-menu');

profileBtn.addEventListener('click', function() {
    profileMenu.classList.toggle('show');
});

document.addEventListener('click', function(event) {
    if (!profileBtn.contains(event.target) && !profileMenu.contains(event.target)) {
        profileMenu.classList.remove('show');
    }
});
 // Display current date
var dateDisplay = document.getElementById('date-display');
var today = new Date();
var options = { year: 'numeric', month: 'long', day: 'numeric' };
dateDisplay.textContent = today.toLocaleDateString(undefined, options);



function showTab(tab) {
console.log("Showing tab: " + tab); // Debugging line
// Hide all task content sections
document.querySelectorAll('.task-content').forEach(function(content) {
    content.style.display = 'none';
});
// Show the selected tab
document.getElementById(tab).style.display = 'block';
}