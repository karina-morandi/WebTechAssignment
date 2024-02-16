function HomePageDashboard() {
    $('#homePage').show();
    $('#loginFormDiv').hide();
    $('#registrationForm').hide();
    $('#admin').hide();
}

function LoginDashboard() {
    $('#homePage').hide();
    $('#loginFormDiv').show();
    $('#registrationForm').hide();
    $('#admin').hide();
}

function RegistrationDashboard() {
//	console.log("Showing registration form");
    $('#homePage').hide();
    $('#loginFormDiv').hide();
    $('#registrationForm').show();
    $('#admin').hide();
}

function AdminDashboard() {
//	console.log("Showing admin dashboard");
    $('#homePage').hide();
    $('#loginFormDiv').hide();
    $('#registrationForm').hide();
    $('#admin').show();
}

let rootURL = "http://localhost:9090";
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
                console.error("AJAX request error:", error);
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
        var htmlStr = '<div class="details col-sm-4 text-center p-3">';
        var image = "../images/"+wine.picture;
        
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
	    let login = $("#loginUsername").val().trim();
	    console.log("login= ",login)
	    let password = $("#loginPassword").val().trim();
	    console.log("password= ", password)
	
	    if (!login || !password) {
	        console.error("Username or password cannot be empty");
	        return;
	    }
	
	    // Send login credentials via AJAX
	     $.ajax({
	     type: "POST",
	     url: rootURL + "/login",
	     data: {
	         login: login,
	         password: password
	     },
	     success: function(data) {
	         if (data && data.message) {
	             console.log("Login successful:", login, password);
	             AdminDashboard();
	             findAll();
	         } else {
	             console.error("Login failed:", data.error || "Unknown error");
	             // Display appropriate error message to the user
	             alert("Login failed: " + (data.error || "Unknown error"));
	         }
	     },
	     error: function(xhr, status, error) {
	         console.error("Login failed:", error);
	         // Display appropriate error message to the user
	         alert("Login failed: " + error);
	     }
	 });
});

	
	 // Handle click on register button in the login form
	$(document).on("click", "#registerButton", function () { 
	    console.log("Register button clicked");
	    RegistrationDashboard(); // Show the registration form
	});
	
	// Handle form submission for registration
	$(document).on("submit", "#registerForm", function(event) {
		event.preventDefault(); // Prevent default form submission behavior

	
	    console.log("Registration form submitted");
	    let login = $("#usernameReg").val();
	    console.log("Login:", login);
	    let email = $("#emailReg").val();
	    console.log("Email:", email)
	    let password = $("#passwordReg").val();
	    console.log("Password:", password);
	    let confirmpassword = $("#confirmpasswordReg").val();
	    console.log("Confirm password:", confirmpassword);
	    
	    if (!login || !email || !password || !confirmpassword) {
	        console.error("All fields are required");
	        return;
	    }
	    
	    if (password !== confirmpassword) {
	        console.error("Passwords do not match");
	        return;
	    }
	    
	    let admin = {
	        login: login,
	        email: email,
	        password: password,
	    };
		    
	    // Send registration data via AJAX
	    $.ajax({
	        type: "POST",
	        url: rootURL + "/user/register",
	        contentType: 'application/json',
	        data: JSON.stringify(admin),
	        success: function(data) {
	            console.log("Registration successful:", login, email, password);
	           	AdminDashboard();          
	        },
	        error: function(xhr, status, error) {
	            console.error("Registration failed:", error);
	            alert("Registration failed: " + error);
	        }
	    });
	});
});

function logout() {
	console.log('Logout');
  //	window.location.href = rootURL + "/logout";
    HomePageDashboard();
}
