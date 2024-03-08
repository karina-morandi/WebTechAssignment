import UserInfo from './UserInfo.js';

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

function clearList(){
	console.log("Clear button clicked!");
	$('#winesContent').empty(); // Clear the content of the wines list
    $('#winesTable').hide(); // Hide the table containing the wines list
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
			try {
                wineData = JSON.parse(data); // Parse JSON response
                console.log("AJAX request successful - Raw response:", data);
                wineData = data; // If parsing is successful, assign the parsed data to wineData
                renderContent();
            } catch (error) {
                console.error("Error parsing JSON response:", error);
                alert("An error occurred while loading wines. Please try reloading the page.");
            }
        },
        error: function(xhr, status, error) {
            console.error("AJAX request error:", error);
        }
    });
}
            /*console.log("AJAX request successful - Raw response:", data);
            wineData = data;
//            $('#dashboard_title').text("All Products");
            renderContent();
        },
        error: function(xhr, status, error) {
                console.error("AJAX request error:", error);
        }
    });
}*/

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
    $('#winesContent').empty(); // Clear previous content
    var table = $('<table>').addClass('table');
    var header = $('<thead>').html('<tr><th>Winery</th><th>Name</th><th>Country</th><th>Edit</th></tr>');
    var tbody = $('<tbody>');
    $('#winesContent').append(table.append(header).append(tbody));

    var list = wineData == null ? [] : (wineData instanceof Array ? wineData : [wineData]);
    $.each(list, function(index, wine){
        var row = $('<tr>');
        row.append('<td>' + wine.winery + '</td>');
        row.append('<td>' + wine.name + '</td>');
        row.append('<td>' + wine.country + '</td>');

        var editCell = $('<td>'); // Create a new cell for the edit button
        var editBtn = $('<button>').attr('type', 'button').addClass('editButton btn btn-secondary').attr('data-id', wine.id).text('Edit');
        editCell.append(editBtn); // Append the edit button to the cell
        row.append(editCell); // Append the cell to the row

        tbody.append(row); // Append the row to the tbody
    });
}

var renderGrid = function() {
    $('#winesContent').empty(); // Clear previous content
    var list = wineData == null ? [] : (wineData instanceof Array ? wineData : [wineData]);

    // Iterate over the wine list
    for (var i = 0; i < list.length; i++) {
        var wine = list[i];
        
        // Create a new row for every third wine or for the last wine
        if (i % 3 === 0 || i === list.length - 1) {
            var row = $('<div>').addClass('row');
            $('#winesContent').append(row);
        }
        
        // Create a column for the wine
        var col = $('<div>').addClass('col-sm-4 text-center p-3 wine-card');
        
        // Wine Name
        var wineName = $('<h3>').addClass('wine-name').text(wine.name);
        col.append(wineName);
        
        // Image Container
        var imageContainer = $('<div>').addClass('image-container');
		var image = "../images/" + wine.picture;
        var img = $('<img>').attr('src', image).addClass('wine-image').attr('alt', wine.name);
        imageContainer.append(img);
        col.append(imageContainer);
        
        // Winery
        var winery = $('<p>').addClass('winery').text(wine.winery);
        col.append(winery);
        
        // Country
        var country = $('<p>').addClass('country').text(wine.country);
        col.append(country);
        
        // Edit Button
        var editBtn = $('<button>').attr('type', 'button').addClass('editButton btn btn-secondary').attr('data-id', wine.id).text('Edit');
        col.append(editBtn);
        
        // Append the column to the current row
        row.append(col);
    }
}

//When DOM is ready
$(document).ready(function () {
	$(document).on("click", "#homeButton", function () { 
        console.log("Home button clicked - list all");
        HomePageDashboard();
     });
     
     $(document).on("click", "#findallWine", function () { 
	    console.log("findallWine button clicked - list all");
	    $('#winesContent').show(); // Ensure the wine list container is visible
	    $('#winesTable').show(); // Hide the table containing the wines list
	    findAll(); // Fetch and render all wines
	});

    $('#listView, #gridView').on('change', function() {
        currentViewType = $(this).val();
        AdminDashboard();
        renderContent(); // Render content based on the current view type
    });

	// Handle click on login button in the home page
	$(document).on("click", "#loginButtonHome", function () {
	    console.log("Login button on home page clicked");
	    LoginDashboard(); // Show the login form
	});
	    
	$('#loginForm').submit(function (event) {
    event.preventDefault(); // Prevent default form submission behavior

    console.log("Login button clicked");
    var loginUsername = $("#loginUsername").val().trim();
    console.log("login= ", loginUsername);
    var passwordLog = $("#loginPassword").val().trim();
    console.log("password= ", passwordLog);
    login();
});

function login() {
    var username = document.getElementById("loginUsername").value;
    console.log("login= ", username);
    var password = document.getElementById("loginPassword").value;
    console.log("password= ", password);

    $.ajax({
        type: "POST",
        url: "/user/login",
        data: {
            username: username,
            password: password
        },
        success: function (data) {
            console.log("Login successful:", data);
            if (data === "Admin") {
            console.log("Admin");
            AdminDashboard(); // Redirect to the admin dashboard
            findAll();
        } else if (data === "Customer") {
            console.log("Customer");
            HomePageDashboard();
        } else {
            console.error("Invalid response from server: " + data);
        }

        },
        error: function (xhr, status, error) {
            console.log("Login failed:", error);
            // Display appropriate error message to the user
            alert("Login failed: " + error);
        }
    });
}
/*    if (!loginUsername || !passwordLog) {
        console.error("Username or password cannot be empty");
        return;
    }

    var loginInfo = {
        username: loginUsername,
        password: passwordLog
    };
    
    var loginInfo = new UserInfo();
    loginInfo.username = loginUsername;
    loginInfo.password = passwordLog;

    console.log(loginInfo);

    $.ajax({
    url: rootURL + "/user/login",
    type: "POST",
    contentType: "application/json",
    data: JSON.stringify(loginInfo),
    dataType: "json",
    success: function(data) {
        console.log("Login successful:", data);
        if (data === "Admin") {
            console.log("Admin");
            AdminDashboard(); // Redirect to the admin dashboard
            findAll();
        } else if (data === "Customer") {
            console.log("Customer");
            HomePageDashboard();
        } else {
            console.error("Invalid response from server: " + data);
        }
    },
    error: function(xhr, status, error) {
        console.error("Login failed:", error);
        // Display appropriate error message to the user
        alert("Login failed: " + error);
    }
});*/

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
	    let role = $("#role").val();
	    console.log("Role:", role);
	    
	    if (!login || !email || !password || !confirmpassword || !role) {
	        console.error("All fields are required");
	        return;
	    }
	    
	    if (password !== confirmpassword) {
	        console.error("Passwords do not match");
	        alert("Passwords don't match!")
	        return;
	    }
	    
	    let admin = {
	        login: login,
	        email: email,
	        password: password,
	        role: role,
	    };
		    
	    // Send registration data via AJAX
	    $.ajax({
	        type: "POST",
	        url: rootURL + "/user/register",
	        contentType: 'application/json',
	        data: JSON.stringify(admin),
	        success: function(data) {
	            console.log("Registration successful:", login, email, password, role);
	            LoginDashboard(); // Show the login form
	           	/*if (data === "Admin registered successfully") {
			        AdminDashboard(); // Redirect to the admin dashboard
			        findAll();
			    } else if (data === "Customer registered successfully") {
			        HomePageDashboard(); // Redirect to the home page
			    } else {
			        console.error("Registration failed:", data);
			        alert("Registration failed: " + data);
			    }         */  
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
	            $('#createWineModal').modal('hide');
	            AdminDashboard();
	            findAll();      
	        },
	        error: function(xhr, status, error) {
	            console.error("Error adding wine:", error);
	            alert("Error creating wine!");
	        }
	    });
    });

   // findAll();
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
	            $('#deleteWineModal').modal('hide');
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
	        // Create a row to contain the search results
	        var row = $('<div>').addClass('row');
	        
	        // Render each wine in the search results
	        $.each(data, function(index, wine) {
	            // Create a column for the wine
	            var col = $('<div>').addClass('col-sm-4 text-center p-3 wine-card');
	            
	            // Wine Name
	            var wineName = $('<h3>').addClass('wine-name').text(wine.name);
	            col.append(wineName);
	            
	            // Image Container
	            var imageContainer = $('<div>').addClass('image-container');
				var image = rootURL + "../images/" + wine.picture;
	            var img = $('<img>').attr('src', image).addClass('wine-image').attr('alt', wine.name);
	            imageContainer.append(img);
	            col.append(imageContainer);
	            
	            // Winery
	            var winery = $('<p>').addClass('winery').text('Winery: ' + wine.winery);
	            col.append(winery);
	            
	            // Country
	            var country = $('<p>').addClass('country').text('Country: ' + wine.country);
	            col.append(country);
	            
	            // Edit Button
	            var editBtn = $('<button>').attr('type', 'button').addClass('editButton btn btn-secondary').attr('data-id', wine.id).text('Edit');
	            col.append(editBtn);
	            
	            // Append the column to the row
	            row.append(col);
	            
	            // If 3 wines are added or it's the last wine, append the row to the content
	            if ((index + 1) % 3 === 0 || index === data.length - 1) {
	                $('#winesContent').append(row);
	                row = $('<div>').addClass('row'); // Create a new row
	            }
	        });
	    } else {
	        // If no wines found, display a message to the user
	        $('#winesContent').html('<p>No wines found</p>');
	    }
	}
});

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
            $('#editPicturePreview').attr('src', rootURL + '../images/' + data.picture);

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


//Function to handle submit button click inside the edit wine modal
$(document).on("click", "#submitEditWine", function(event) {
 event.preventDefault(); // Prevent default form submission

 var pictureFile = $('#editPicture')[0].files[0];
 var wineId = $('#editWineModal').data('wineId');
 
 // If no new image selected, update wine details without changing the image
 if (!pictureFile) {
     var imagePath = $('#editPicturePreview').attr('src');
     updateWineDetails(wineId, imagePath); // Update wine details with the image path
     return;
 }
 
 // If a new image is selected, upload it first
 uploadImage(pictureFile).then(function(response) {
     var imagePath = response; // Assuming the response contains the image path
     updateWineDetails(wineId, imagePath); // Update wine details with the image path
 }).catch(function(error) {
     console.error("Error uploading image:", error);
     alert("Error uploading image: " + error);
 });
});

// Function to update wine details
/*function updateWineDetails(wineId, imagePath) {
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
}*/

function updateWineDetails(wineId, imagePath) {
    var updatedWine = {
        name: $('#editName').val(),
        grapes: $('#editGrapes').val(),
        country: $('#editCountry').val(),
        year: $('#editYear').val(),
        color: $('#editColor').val(),
        winery: $('#editWinery').val(),
        region: $('#editRegion').val(),
        picture: imagePath // Assuming imagePath contains the new image path
    };

    $.ajax({
        type: "PUT",
        url: rootURL + "/wines/" + wineId,
        contentType: "application/json",
        data: JSON.stringify(updatedWine),
        success: function(data) {
            console.log("Wine updated successfully:", data);
            $('#editWineModal').modal('hide');
            findAll(); // Refresh wine list or perform necessary updates
        },
        error: function(xhr, status, error) {
            console.error("Error updating wine:", error);
            alert("Error updating wine: " + error);
        }
    });
}

