function HomePageDashboard() {
	var homePageElement = document.getElementById('homePage');
    if (homePageElement) {
        homePageElement.style.display = "block";
    }
    
    var adminElement = document.getElementById('admin');
    if (adminElement) {
        adminElement.style.display = 'none';
    }
}

function showLoginForm() {
    var homePageElement = document.getElementById('homePage');
    var loginFormElement = document.getElementById('loginFormContainer');
    
    if (homePageElement && loginFormElement) {
        homePageElement.style.display = "none"; // Hide the homepage
        loginFormElement.style.display = "block"; // Show the login form
    }
}

var rootURL = "http://localhost:9090";
var currentViewType = 'list'; // Default view type
var wineData;

var findAll = function(){
    console.log("Find all wines");
    $.ajax({
        type: 'GET',
        url: rootURL + '/wines',
        dataType: "json",
        success: function(data){
            console.log("AJAX request successful - Raw response:", data);
            wineData = data;
            $('#dashboard_title').text("All Products");
            renderContent();
        },
        error: function(xhr, status, error) {
            // Handle redirection to login page
            if (xhr.status == 302 && xhr.getResponseHeader('Location') == '/loginpage') {
                console.log("Redirected to login page");
                window.location.href = "/loginpage"; // Redirect to login page
            } else {
                // Log the error message
                console.error("AJAX request error:", error);
            }
        }
    });
}


var renderContent = function() {
    if (currentViewType === 'list') {
        renderList();
    } else if (currentViewType === 'grid') {
        renderGrid();
    }
}

$('#listView, #gridView').on('change', function() {
    console.log("View type changed");
    currentViewType = $(this).val();
    console.log("Current view type:", currentViewType);
    renderContent(); // Render content based on the current view type
});


var renderList = function() {
    $('.details').remove();
    var list = wineData == null ? [] : (wineData instanceof Array ? wineData : [wineData]);
    $.each(list, function(index, wine){
        var htmlStr = '<div class="details col-sm-12 mb-3">';
        htmlStr += '<h1>' + wine.winery + '</h1>';
        htmlStr += '<p><b>' + wine.name + '</b></p>';
        htmlStr += '<p>' + wine.country + '</p>';
        htmlStr += '<button type="button" class="infoButton btn btn-primary" data-id="' + wine.id + '">More Details</button>';
        
        $('#winesContent').append(htmlStr);
    });
}

var renderGrid = function() {
    $('.details').remove();
    var list = wineData == null ? [] : (wineData instanceof Array ? wineData : [wineData]);
    $.each(list, function(index, wine){
        var image = "images/" + wine.picture;
        var htmlStr = '<div class="details col-sm-4 text-center p-3">';
        htmlStr += '<h1>' + wine.winery + '</h1>';
        htmlStr += '<img src="' + image + '" class=displayCenter>';
        htmlStr += '<p><b>' + wine.name + '</b></p>';
        htmlStr += '<p>' + wine.country + '</p>';
        htmlStr += '<button type="button" class="infoButton btn btn-primary" data-id="' + wine.id + '">More Details</button>';
        
        $('#winesContent').append(htmlStr);
    });
}

//When DOM is ready
$(document).ready(function () {
	$(document).on("click", "#homeButton", function () { 
        console.log("Home button clicked - list all");
        HomePageDashboard();
     });
     $(document).on("click", "#findallWine", function () { 
        console.log("findallWine button clicked - list all");
        // Check if the user is authenticated before calling findAll
        if (checkAuthentication()) {
            findAll();
        } else {
            // Redirect to login page if not authenticated
            window.location.href = "/loginpage";
        }
     });
    $('#listView, #gridView').on('change', function() {
        currentViewType = $(this).val();
        renderContent(); // Render content based on the current view type
    });

    // Call checkAuthentication on page load to handle initial authentication state
    checkAuthentication();
});

function checkAuthentication() {
    // You can implement your authentication logic here
    // For example, you might want to check if the user is logged in
    
    // Here, I'm using a simple example where I check if there's a cookie named "session"
    // You should replace this with your actual authentication mechanism
    
    // Check if the session cookie exists
    var sessionCookie = getCookie("session");
    
    // If sessionCookie is not null or empty, assume the user is authenticated
    return sessionCookie != null && sessionCookie !== "";
}

// Function to get a cookie value by name
function getCookie(name) {
    var cookies = document.cookie.split(';');
    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i].trim();
        if (cookie.startsWith(name + '=')) {
            return cookie.substring(name.length + 1);
        }
    }
    return null;
}

$(document).ready(function () {
    // Handle click on login button in the home page
    $(document).on("click", "#loginButtonHome", function () { 
        console.log("Login button on home page clicked");
        showLoginForm(); // Show the login form
    });
    
    // Handle click on login button in the login form
    $(document).on("click", "#loginButton", function(event) {
        event.preventDefault(); // Prevent default form submission behavior

        console.log("Login button clicked");
        var username = $("#username").val();
        var password = $("#password").val();

        // Verify if username and password are not empty
        if (username.trim() === "" || password.trim() === "") {
            // Handle case where username or password is empty
            console.error("Username or password cannot be empty");
            return;
        }

        // Send login credentials via AJAX
        $.ajax({
            type: "POST",
            url: rootURL + "/loginpage",
            data: {
                username: username,
                password: password
            },
            success: function(data) {
                // Redirect to admin dashboard upon successful login
                window.location.href = "/admin";
                
                // Call findAll function after successful login
                findAll();
            },
            error: function(xhr, status, error) {
                // Handle login error, display error message, etc.
                console.error("Login failed:", error);
            }
        });
    });
    
     // Handle click on register button in the login form
    $(document).on("click", "#registerButton", function () { 
        console.log("Register button clicked");
        showRegistrationForm(); // Show the registration form
    });
    
    // Handle form submission for registration
    $(document).on("submit", "#registrationForm", function(event) {
        event.preventDefault(); // Prevent default form submission behavior

        console.log("Registration form submitted");
        var username = $("#usernameReg").val();
        console.log("Username:", username);
        var password = $("#passwordReg").val();
        console.log("Password:", password);

        // Verify if username and password are not empty
        if (username.trim() === "" || password.trim() === "") {
            // Handle case where username or password is empty
            console.error("Username or password cannot be empty");
            return;
        }
        // Send registration data via AJAX
        $.ajax({
            type: "POST",
            url: rootURL + "/register",
            data: {
                username: username,
                password: password
            },
            success: function(data) {
                console.log("Registration successful:", data);
                // Redirect to a success page or handle accordingly
                event.preventDefault(); // Prevent default form submission behavior
                window.location.href = "/loginpage";
                
            },
            error: function(xhr, status, error) {
                console.error("Registration failed:", error);
                // Handle registration error, display error message, etc.
            }
        });
    });
});

function showRegistrationForm() {
    var homePageElement = document.getElementById('homePage');
    var registrationFormElement = document.getElementById('registrationFormContainer');
    
    if (homePageElement && registrationFormElement) {
        homePageElement.style.display = "none"; // Hide the homepage
        registrationFormElement.style.display = "block"; // Show the registration form
    }
    var loginFormContainer = document.getElementById('loginFormContainer');
    if (loginFormContainer) {
        loginFormContainer.style.display = 'none';
    }
}
