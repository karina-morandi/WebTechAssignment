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
       // htmlStr += '<button type="button" class="infoButton btn btn-primary" data-id="' + wine.id + '">More Details</button>';
        htmlStr += '<button type="button" class="editButton btn btn-secondary" data-id="' + wine.id + '">Edit</button>'; // Add edit button
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
       // htmlStr += '<button type="button" class="infoButton btn btn-primary" data-id="' + wine.id + '">More Details</button>';
        htmlStr += '<button type="button" class="editButton btn btn-secondary" data-id="' + wine.id + '">Edit</button>'; // Add edit button
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
	     url: rootURL + "/login?login=" + login + "&password=" + password,
	  //   contentType: 'application/json',
	//     data: JSON.stringify(loginInfo),
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
		
		var formWine = new FormData(); // Use FormData for handling file uploads
	    formWine.append('name', $('#Name').val());
	    formWine.append('grapes', $('#Grapes').val());
	    formWine.append('country', $('#Country').val());
	    formWine.append('year', $('#Year').val());
	    formWine.append('color', $('#Color').val());
	    formWine.append('winery', $('#Winery').val());
	    formWine.append('region', $('#Region').val());
	    formWine.append('pictureFile', $('#Picture')[0].files[0]); // Append the picture file to the form data
		
		$.ajax({
	        type: "POST",		
	        url: rootURL + "/wines/createNewWine",
			contentType: false, // Set contentType to false when using FormData
		    processData: false, // Set processData to false when using FormData
		    data: formWine,
	        success: function(response) {
	            console.log("wine added", response);
	            alert("Wine added successfully");
	            AdminDashboard();
	            findAll();      
	        },
	        error: function(xhr, status, error) {
	            console.error("Error adding wine:", error);
	          //  var message = JSON.parse(xhr.responseText);
	          //  alert(message.errorMessage);
	          //  $('#errorMsg p').remove();
	          //  $('#successMsg p').remove();
	          //  $('#errorMsg').append('<p>'+message.errorMessage+'</p>');
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

/*// Function to handle click on "Edit" button for a wine
$(document).on("click", ".editButton", function() {
    let wineId = $(this).data('id');
    console.log("Edit wine clicked for ID:", wineId);
    // Fetch wine details by ID
    $.ajax({
        type: "GET",
        url: rootURL + "/wines/" + wineId,
        success: function(wine) {
            console.log("Fetched wine details:", wine);
            // Populate the edit modal with current wine information
            $('#editName').val(wine.name);
            $('#editGrapes').val(wine.grapes);
            $('#editCountry').val(wine.country);
            $('#editYear').val(wine.year);
            $('#editColor').val(wine.color);
            $('#editWinery').val(wine.winery);
            $('#editRegion').val(wine.region);
            
            // Display the current image
            $('#currentPicture').attr('src', '../images/' + wine.picture);
            
             // Set data-id attribute for the submit button to identify which wine is being edited
            $('#submitEditWine').data('id', wineId);
            
            // Show the edit modal
            $('#editWineModal').modal('show');
        },
        error: function(xhr, status, error) {
            console.error("Error fetching wine details:", error);
            alert("Error fetching wine details: " + error);
        }
    });
});

// Function to handle submit button click inside the edit wine modal
$(document).on("click", "#submitEditWine", function(event) {
    event.preventDefault(); // Prevent default form submission
    let wineId = $(this).data('id'); // This line might be causing the issue
    
    // Get updated wine information from the modal fields
    let updatedWine = {
        name: $('#editName').val(),
        grapes: $('#editGrapes').val(),
        country: $('#editCountry').val(),
        year: $('#editYear').val(),
        color: $('#editColor').val(),
        winery: $('#editWinery').val(),
        region: $('#editRegion').val(),
        pictureFile: $('#editPicture')[0].files[0]
    };
    
    // Send AJAX request to update the wine
    $.ajax({
        type: "PUT",
        url: rootURL + "/wines/" + wineId,
        contentType: false, // Set contentType to false when using FormData
        processData: false, // Set processData to false when using FormData
        data: updatedWine,
        success: function(data) {
            console.log("Wine updated successfully:", data);
            $('#editWineModal').modal('hide'); // Hide the edit modal
            findAll(); // Refresh wine list or perform necessary updates
        },
        error: function(xhr, status, error) {
            console.error("Error updating wine:", error);
            alert("Error updating wine: " + error);
        }
    });
});
*/


// Function to handle submit button click inside the edit wine modal
$(document).on("click", ".editButton", function(event) {
    event.preventDefault(); // Prevent default form submission
    let wineId = $(this).data('id'); // Get wine ID from the button clicked
    
    // Fetch wine details by ID
    $.ajax({
        type: "GET",
        url: rootURL + "/wines/" + wineId,
        success: function(data) {
            console.log("Fetched wine details:", data);
            
            // Populate modal fields with existing wine data
            $('#editName').val(data.name);
            $('#editGrapes').val(data.grapes);
            $('#editCountry').val(data.country);
            $('#editYear').val(data.year);
            $('#editColor').val(data.color);
            $('#editWinery').val(data.winery);
            $('#editRegion').val(data.region);
            // Assuming you have a picture preview element in your modal
            $('#editPicturePreview').attr('src', rootURL + '/images/' + data.picture);

            // Store wine ID in modal data attribute
            $('#editWineModal').data('wineId', wineId);

            // Show the edit modal
            $('#editWineModal').modal('show');
        },
        error: function(xhr, status, error) {
            console.error("Error fetching wine details:", error);
            alert("Error fetching wine details: " + error);
        }
    });
});
/*
// Function to handle submit button click inside the edit wine modal
$(document).on("click", "#submitEditWine", function(event) {
    event.preventDefault(); // Prevent default form submission
    
    // Retrieve wine ID from modal data attribute
    let wineId = $('#editWineModal').data('wineId');
    
    // Get updated wine information from the modal fields
    let updatedWine = {
        name: $('#editName').val(),
        grapes: $('#editGrapes').val(),
        country: $('#editCountry').val(),
        year: $('#editYear').val(),
        color: $('#editColor').val(),
        winery: $('#editWinery').val(),
        region: $('#editRegion').val(),
        pictureFile: $('#editPicture')[0].files[0]
    };
    
    // Send AJAX request to update the wine
    $.ajax({
        type: "PUT",
        url: rootURL + "/wines/" + wineId,
        contentType: false, // Set contentType to false when using FormData
        processData: false, // Set processData to false when using FormData
        data: updatedWine,
        success: function(data) {
            console.log("Wine updated successfully:", data);
            $('#editWineModal').modal('hide'); // Hide the edit modal
            findAll(); // Refresh wine list or perform necessary updates
        },
        error: function(xhr, status, error) {
            console.error("Error updating wine:", error);
            alert("Error updating wine: " + error);
        }
    });
});
*/

/*// Function to handle submit button click inside the edit wine modal
$(document).on("click", "#submitEditWine", function(event) {
    event.preventDefault(); // Prevent default form submission
    
    // Retrieve wine ID from modal data attribute
    let wineId = $('#editWineModal').data('wineId');
    
    // Get updated wine information from the modal fields
    let updatedWine = {
        name: $('#editName').val(),
        grapes: $('#editGrapes').val(),
        country: $('#editCountry').val(),
        year: $('#editYear').val(),
        color: $('#editColor').val(),
        winery: $('#editWinery').val(),
        region: $('#editRegion').val(),
        pictureFile: $('#editPicture')[0].files[0]
    };
    
    var updatedWine = new FormData(); // Use FormData for handling file uploads
	    updatedWine.append('editName', $('#editName').val());
	    updatedWine.append('editGrapes', $('#editGrapes').val());
	    updatedWine.append('editCountry', $('#editCountry').val());
	    updatedWine.append('editYear', $('#editYear').val());
	    updatedWine.append('editColor', $('#editColor').val());
	    updatedWine.append('editWinery', $('#editWinery').val());
	    updatedWine.append('editRegion', $('#editRegion').val());
//	    updatedWine.append('editPictureFile', $('#editPicture')[0].files[0]); // Append the picture file to the form data
	
		var pictureFile = $('#editPicture')[0].files[0];
		if (pictureFile) {
		    updatedWine.append('editPictureFile', pictureFile);
		}	
	console.log(updatedWine);
    
    // Send AJAX request to update the wine
    $.ajax({
        type: "PUT",
        url: rootURL + "/wines/" + wineId,
        contentType: "application/json", // Set contentType to false when using FormData
        processData: false, // Set processData to false when using FormData
        data: updatedWine,
        success: function(data) {
            console.log("Wine updated successfully:", data);
            console.log("AJAX response:", data); // Log the entire response object
            $('#editWineModal').modal('hide'); // Hide the edit modal
            findAll(); // Refresh wine list or perform necessary updates
        },
        error: function(xhr, status, error) {
            console.error("Error updating wine:", error);
            alert("Error updating wine: " + error);
        }
    });
});*/


// Function to handle image upload
function uploadImage(file) {
    var formData = new FormData();
    formData.append('file', file);
    
    return $.ajax({
        type: "POST",
        url: rootURL + "/uploadImage",
        contentType: false,
        processData: false,
        data: formData
    });
}

// Function to handle submit button click inside the edit wine modal
$(document).on("click", "#submitEditWine", function(event) {
    event.preventDefault(); // Prevent default form submission

    var pictureFile = $('#editPicture')[0].files[0];
    var wineId = $('#editWineModal').data('wineId');
    
    if (pictureFile) {
        uploadImage(pictureFile).then(function(response) {
            var imagePath = response; // Assuming the response contains the image path
            updateWineDetails(wineId, imagePath); // Update wine details with the image path
        }).catch(function(error) {
            console.error("Error uploading image:", error);
            alert("Error uploading image: " + error);
        });
    } else {
        // If no new image selected, update wine details without changing the image
        var imagePath = $('#editPicturePreview').attr('src');
        updateWineDetails(wineId, imagePath);
    }
});

// Function to update wine details
function updateWineDetails(wineId, imagePath) {
    // Get updated wine information from the modal fields
    var updatedWine = {
	    name: $('#editName').val(),
	    grapes: $('#editGrapes').val(),
	    country: $('#editCountry').val(),
	    year: $('#editYear').val(),
	    color: $('#editColor').val(),
	    winery: $('#editWinery').val(),
	    region: $('#editRegion').val(),
    };
    updatedWine.picture = imagePath; // Update the picture property with the new image path

    // Send AJAX request to update the wine
    $.ajax({
        type: "PUT",
        url: rootURL + "/wines/" + wineId,
        contentType: "application/json",
        data: JSON.stringify(updatedWine),
        success: function(data) {
            console.log("Wine updated successfully:", data);
            $('#editWineModal').modal('hide'); // Hide the edit modal
            findAll(); // Refresh wine list or perform necessary updates
        },
        error: function(xhr, status, error) {
            console.error("Error updating wine:", error);
            alert("Error updating wine: " + error);
        }
    });
}


