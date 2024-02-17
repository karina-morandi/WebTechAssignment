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
	    
	    let loginInfo = {
			login: login,
	        password: password,
		}
	
	    // Send login credentials via AJAX
	     $.ajax({
	     type: "POST",
	     url: rootURL + "/login",
	     contentType: 'application/json',
	     data: JSON.stringify(loginInfo),
	     success: function(data) {
	             console.log("Login successful:", login, password);
	             AdminDashboard();
	             findAll();
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

$(document).ready(function(){
    $(document).on("click", '#newWine', function(){
        console.log("Add wine clicked!");
        $('#createWineModal').modal('show');
    });

    // Handle submit button click inside the modal
    $(document).on("click", "#submitWine", function(event) {
        event.preventDefault(); // Prevent the default form submission

		let name = $('#Name').val();
		let grapes = $('#Grapes').val();
		let country = $('#Country').val();
		let year = $('#Year').val();
		let color = $('#Color').val();
		let winery = $('#Winery').val();
		let region = $('#Region').val();
		let picture = $('#Picture').val();
		
		let wine = {
			name: name,
			grapes: grapes,
			country: country,
			year: year,
			color: color,
			winery: winery,
			region: region,
			picture: picture,
		}
		
		$.ajax({
	        type: "POST",		
	        url: rootURL + "/wines/createNewWine",
			contentType: 'application/json',
	        data: JSON.stringify(wine),
	        success: function(data) {
	            console.log("wine added");
	            alert("Wine added successfully")
	            findAll();      
	        },
	        error: function(xhr, status, error) {
	            console.log("Error response:", xhr.responseText);
			    var message = JSON.parse(xhr.responseText);
			    alert(message.errorMessage)
			    $('#errorMsg p').remove();
			    $('#successMsg p').remove();
			    $('#errorMsg').append('<p>'+message.errorMessage+'</p>');
	        }
	    });
		
   
    });
    findAll();
});



$(document).ready(function(){
    $(document).on("click", '#deleteWine', function(){
        console.log("Delete wine clicked!");
        $('#deleteWineModal').modal('show');
    });

    // Handle submit button click inside the modal
    $(document).on("click", "#submitDeleteWine", function(event) {
        event.preventDefault(); // Prevent the default form submission

		let name = $('#deleteName').val();
		
		$.ajax({
	        type: "DELETE",		
	        url: rootURL + "/wines/" + name,
			contentType: 'application/json',
			data: name = name,
	        success: function(data) {
	            console.log("wine deleted");
	            alert("Wine deleted successfully")
	            findAll();      
	        },
	        error: function(xhr, status, error) {
	            console.log("Error response:", xhr.responseText);
			    var message = JSON.parse(xhr.responseText);
			    alert(message.errorMessage)
			    $('#errorMsg p').remove();
			    $('#successMsg p').remove();
			    $('#errorMsg').append('<p>'+message.errorMessage+'</p>');
	        }
	    });
    });
});


$(document).ready(function() {
	// Handle form submission for search
	$('#searchForm').submit(function(event) {
	    event.preventDefault(); // Prevent default form submission behavior
	
	    let searchQuery = $('#searchInput').val().trim(); // Get the search query from the input
	
	    // Send search query via AJAX
	    $.ajax({
	        type: "GET",
	        url: rootURL + "/wines/name/" + searchQuery,
	        success: function(data) {
	            console.log("Search successful:", data);
	            renderSearchResults(data); // Render search results
	        },
	        error: function(xhr, status, error) {
	            console.error("Search failed:", error);
	            // Display appropriate error message to the user
	            alert("Search failed: " + error);
	        }
	    });
	});
	
	
	// Function to render search results
	function renderSearchResults(data) {
	    $('#winesContent').empty(); // Clear previous search results
	
	    if (data.length > 0) {
	        // Render each wine in the search results
	        $.each(data, function(index, wine) {
	            var htmlStr = '<div class="details col-sm-12 mb-3">';
	            htmlStr += '<h1>' + wine.winery + '</h1>';
	            htmlStr += '<p><b>' + wine.name + '</b></p>';
	            htmlStr += '<p>' + wine.country + '</p>';
	            htmlStr += '<button type="button" class="infoButton btn btn-primary" data-id="' + wine.id + '">More Details</button>';
	            $('#winesContent').append(htmlStr);
	        });
	    } else {
	        // If no wines found, display a message to the user
	        $('#winesContent').html('<p>No wines found</p>');
	    }
	}
});
