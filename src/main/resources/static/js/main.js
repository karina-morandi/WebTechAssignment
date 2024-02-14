function HomePageDashboard() {
    $('#homePage').show();
    $('#loginForm').hide();
    $('#registrationForm').hide();
    $('#admin').hide();
}

function LoginDashboard() {
    $('#homePage').hide();
    $('#loginForm').show();
    $('#registrationForm').hide();
    $('#admin').hide();
}

function RegistrationDashboard() {
	console.log("Showing registration form");
    $('#homePage').hide();
    $('#loginForm').hide();
    $('#registrationForm').show();
    $('#admin').hide();
}

function AdminDashboard() {
	console.log("Showing admin dashboard");
    $('#homePage').hide();
    $('#loginForm').hide();
    $('#registrationForm').hide();
    $('#admin').show();
}

// Function to check if the user is authenticated
//var login = "root";
function checkAuthentication(login) {
    // Make an AJAX call to the server to check if the user is authenticated
    $.ajax({
        type: 'GET',
        url: rootURL + '/user/name/' + login,
        success: function(response) {
            // User is authenticated
            return true;
        },
        error: function(xhr, status, error) {
            // User is not authenticated, redirect to login page
            window.location.href = "/login";
            return false;
        }
    });
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
//            $('#dashboard_title').text("All Products");
            renderContent();
        },
        error: function(xhr, status, error) {
            // Handle redirection to login page
            if (xhr.status == 302 && xhr.getResponseHeader('Location') == '/login') {
                console.log("Redirected to login page");
                window.location.href = "/login"; // Redirect to login page
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
        findAll();
     });
    $('#listView, #gridView').on('change', function() {
        currentViewType = $(this).val();
        renderContent(); // Render content based on the current view type
    });

	// Handle click on login button in the home page
	$(document).on("click", "#loginButtonHome", function () {
	    console.log("Login button on home page clicked");
	    LoginDashboard(); // Show the login form
	});
	    
	// Handle click on login button in the login form
	$('#loginForm').submit(function (event) {
		event.preventDefault(); // Prevent default form submission behavior
	
	    console.log("Login button clicked");
	    var username = $("#username").val().trim();
	    var password = $("#password").val().trim();
	
	    if (!username || !password) {
	        console.error("Username or password cannot be empty");
	        return;
	    }
	
	    // Send login credentials via AJAX
	    $.ajax({
	        type: "GET",
	        url: rootURL + "/login" + login,
	        data: {
	            username: username,
	            password: password
	        },
	        success: function(data) {
				console.log("Login successful:", data);
	            // Redirect to admin dashboard upon successful login
	            window.location.href = "/admin";
	            aAdminDashboard();
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
	    RegistrationDashboard(); // Show the registration form
	});
	
	// Handle form submission for registration
	$(document).on("submit", "#registrationForm form", function(event) {
		event.preventDefault(); // Prevent default form submission behavior

	
	    console.log("Registration form submitted");
	    var login = $("#usernameReg").val().trim();
	    console.log("Username:", login);
	    var email = $("#emailReg").val().trim();
	    console.log("Email:", email)
	    var password = $("#passwordReg").val().trim();
	    console.log("Password:", password);
	    var confirmpassword = $("#confirmpasswordReg").val().trim();
	    console.log("Password:", password);
	    
	    if (!login || !email || !password || !confirmpassword) {
	        console.error("All fields are required");
	        return;
	    }
	    
	    if (password !== confirmpassword) {
	        console.error("Passwords do not match");
	        return;
	    }
	    
	    // Send registration data via AJAX
	    $.ajax({
	        type: "POST",
	        url: rootURL + "/user/register",
	        data: {
	            login: login,
	            email: email,
	            password: password
	        },
	        success: function(data) {
	            console.log("Registration successful:", data);
	            alert("Registration successful!!!!!!!!!!!!!!!!!!!");
	           	AdminDashboard();          
	        },
	        error: function(xhr, status, error) {
	            console.error("Registration failed:", error);
	            alert("Registration failed: " + error);
	        }
	    });
	});
});