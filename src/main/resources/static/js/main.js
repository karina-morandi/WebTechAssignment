import UserInfo from './UserInfo.js';

function HomePageDashboard() {
    $('#homePage').show();
    $('#loginFormDiv').hide();
    $('#registrationForm').hide();
    $('#admin').hide();
    $('#wineListDiv').hide();
}

function LoginDashboard() {
    $('#homePage').hide();
    $('#loginFormDiv').show();
    $('#registrationForm').hide();
    $('#admin').hide();
    $('#wineListDiv').hide();
}

function RegistrationDashboard() {
//	console.log("Showing registration form");
    $('#homePage').hide();
    $('#loginFormDiv').hide();
    $('#registrationForm').show();
    $('#admin').hide();
    $('#wineListDiv').hide();
}

function AdminDashboard() {
//	console.log("Showing admin dashboard");
    $('#homePage').hide();
    $('#loginFormDiv').hide();
    $('#registrationForm').hide();
    $('#admin').show();
    $('#detailsTable').hide(); // Hide any existing tables
    $('#wineListDiv').hide();
    $('#header').hide();
}

function WineListCustomer(){
	$('#homePage').hide();
    $('#loginFormDiv').hide();
    $('#registrationForm').hide();
    $('#admin').hide();
    $('#wineListDiv').show();
}



function clearList(){
	console.log("Clear button clicked!");
	$('#winesContent').empty(); // Clear the content of the wines list
    $('#winesTable').hide(); // Hide the table containing the wines list
}

// Function to convert form data to JSON
function formToJSONCart(orderId, wineId, quantity) {
    var formData = {
        id: orderId,
        wine: { id: wineId },
        quantity: quantity, // Include the quantity in the JSON data
        // Add other fields here as needed
    };
    return JSON.stringify(formData);
}


let rootURL = "http://localhost:9090";
var currentViewType = 'list'; // Default view type
var wineData;
let customerId;

function findAll(){
	$('#detailsTable').hide(); // Hide any existing tables
    console.log("Find all wines");
    $.ajax({
        type: "GET",
        url: rootURL + "/wines",
        dataType: "json",
        success: function(data) {
            console.log('Success:', data);
            // Process the wines data here
            wineData = data;
            renderContent();
        },
        error: function(xhr, status, error) {
            console.log('Error:', error);
        },
        complete: function(xhr, status) {
            console.log('Raw response:', xhr.responseText);
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
	
	// Handle click on logout button in the home page
	$(document).on("click", "#logoutButtonHome", function () {
	    console.log("Logout button on home page clicked");
	    $("#paymentPage").hide();
	    // Implement logout functionality here
	    // For example, clear customerId and update button visibility
	    customerId = null;
	    updateLoginLogoutButtons();
	});
	    
	$('#loginForm').submit(function (event) {
    event.preventDefault(); // Prevent default form submission behavior
    $("#paymentPage").hide();
    console.log("Login button clicked");
    var loginUsername = $("#loginUsername").val().trim();
    console.log("login= ", loginUsername);
    var passwordLog = $("#loginPassword").val().trim();
    console.log("password= ", passwordLog);
    login();
});

// Function to fetch customer ID after successful login
function fetchCustomerId(username) {
    $.ajax({
        type: "GET",
        url: "/serviceLayer/customers/username/" + username,
        success: function (data) {
            console.log("Customer ID:", data);
            customerId = data;
            updateLoginLogoutButtons(); // Update button visibility after fetching customer ID
            // Pass the customer ID to other functions as needed
            // For example, you can pass it to the addToCart function
            // addToCart(data.id, wineId);
        },
        error: function (xhr, status, error) {
            console.error("Error fetching customer ID:", error);
            // Handle error appropriately
        }
    });
}

// Function to check if the user is logged in
function checkAuthenticationStatus() {
	console.log("AUTHENTICATION!!!!")
    // Perform your authentication status check here
    // For example, you can check if a customerId exists
    return customerId != null;
}

// Function to update the visibility of login and logout buttons
function updateLoginLogoutButtons() {
	console.log("UPDATE BUTTONS!!!")
    if (checkAuthenticationStatus()) {
        // Hide the login button and show the logout button
        $("#loginButtonHome").hide();
        $("#cartButtonHome").show();
        $("#logoutButtonHome").show();
    } else {
        // Show the login button and hide the logout button
        $("#loginButtonHome").show();
        $("#cartButtonHome").hide();
        $("#logoutButtonHome").hide();
    }
}

// Call the function to update button visibility when the page loads
$(document).ready(function () {
	checkAuthenticationStatus();
    updateLoginLogoutButtons();
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
            fetchCustomerId(username);
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
	        url: rootURL + "/serviceLayer/register",
	        contentType: 'application/json',
	        data: JSON.stringify(admin),
	        success: function(data) {
	            console.log("Registration successful:", login, email, password, role);
	            LoginDashboard(); // Show the login form 
	        },
	        error: function(xhr, status, error) {
	            console.error("Registration failed:", error);
	            alert("Registration failed: " + error);
	        }
	    });
	});
	//HERE!!!!!
	function logout() {
	console.log('Logout');
	customerId = null; // Clear customerId
    updateLoginLogoutButtons(); // Update button visibility
    // Clear login and password input fields
    $("#loginUsername").val('');
    $("#loginPassword").val('');
    makeCartDropdownInvisible();
}

	// Handle click on logout button in the home page
	$(document).on("click", "#logoutButtonHome", function () {
	    console.log("Logout button on home page clicked");
	    logout(); // Call the logout function
	});
	
	// Handle click on logout button in the home page
	$(document).on("click", "#logoutButton", function () {
	    console.log("Logout button clicked");
	    customerId = null;
	    HomePageDashboard();
	    $('#header').show();
	    logout(); // Call the logout function
	});

});

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
	    formWine.append('price', $('#Price').val());
	    formWine.append('description', $('#Description').val());
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
            $('#editPrice').val(data.price);
            $('#editDescription').val(data.description);
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

function updateWineDetails(wineId, imagePath) {
    var updatedWine = {
        name: $('#editName').val(),
        grapes: $('#editGrapes').val(),
        country: $('#editCountry').val(),
        year: $('#editYear').val(),
        color: $('#editColor').val(),
        winery: $('#editWinery').val(),
        region: $('#editRegion').val(),
        price: $('#editPrice').val(),
        description: $('#editDescription').val(),
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











document.getElementById('closeWineListButton').addEventListener('click', function() {
    document.getElementById('wineListDiv').style.display = 'none'; // Hide the wine list section
    document.getElementById('homePage').style.display = 'block'; // Show the home page section
});


let selectedWineId;
// Function to display wine list without star ratings
function displayWineList() {
    fetch('http://localhost:9090/customers/wines') // Update the endpoint to fetch wines for customers
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch wines');
            }
            return response.json();
        })
        .then(data => {
            // Sort wines based on default criteria (e.g., name)
            data.sort((a, b) => a.name.localeCompare(b.name));

            let wineListHTML = '<div class="row">';
            data.forEach(wine => {
                wineListHTML += `
                    <div class="col-md-6">
                        <div class="wine-card">
                            <img src="../images/${wine.picture}" alt="Wine Image" class="wine-image">
                            <div class="wine-details">
                                <h3 class="wine-name">${wine.name}</h3>
                                <p class="wine-description">${wine.description}</p>
                                <p class="wine-price">€${wine.price}</p>
                                <button class="btn btn-primary view-details-button" data-wine-id="${wine.id}" data-bs-toggle="modal" data-bs-target="#wineModal">View Details</button>
                            </div>
                        </div>
                    </div>
                `;
            });
            wineListHTML += '</div>'; // Close the row
            document.getElementById('wineListBody').innerHTML = wineListHTML;
            document.getElementById('wineListDiv').style.display = 'block'; // Display the wine list section
                  
             })
     .catch(error => {
         console.error('Error fetching wine list:', error);
         // Handle errors or display error message to the user
     });
}

function sortWines() {
    try {
        const sortBy = document.getElementById('sortSelect').value;
        fetch(`http://localhost:9090/customers/wines?sortBy=${sortBy}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Failed to fetch sorted wines');
                }
                return response.json();
            })
            .then(sortedData => {
                // Sort the data based on the selected criteria
                if (sortBy === 'price-asc') {
                    sortedData.sort((a, b) => a.price - b.price);
                } else if (sortBy === 'price-desc') {
                    sortedData.sort((a, b) => b.price - a.price);
                } else if (sortBy === 'name') {
                    sortedData.sort((a, b) => a.name.localeCompare(b.name));
                }

                // Update the wine list with sorted data
                let wineListHTML = '<div class="row">';
                sortedData.forEach(wine => {
					wineListHTML += `
	                    <div class="col-md-6">
	                        <div class="wine-card">
	                            <img src="../images/${wine.picture}" alt="Wine Image" class="wine-image">
	                            <div class="wine-details">
	                                <h3 class="wine-name">${wine.name}</h3>
	                                <p class="wine-description">${wine.description}</p>
	                                <p class="wine-price">€${wine.price}</p>
	                                <button class="btn btn-primary view-details-button" data-wine-id="${wine.id}" data-bs-toggle="modal" data-bs-target="#wineModal">View Details</button>
	                            </div>
	                        </div>
	                    </div>
                    `;
                });
                wineListHTML += '</div>'; // Close the row
                document.getElementById('wineListBody').innerHTML = wineListHTML;
                document.getElementById('wineListDiv').style.display = 'block'; // Display the wine list section
            })
            .catch(error => {
                console.error('Error fetching sorted wine list:', error);
                // Handle errors or display error message to the user
            });
    } catch (error) {
        console.error('Error in sortWines:', error);
    }
}

document.getElementById('sortSelect').addEventListener('change', sortWines);

// Event listener for the wineListButton
document.getElementById('wineListButton').addEventListener('click', function() {
    $('#homePage').hide(); // Hide the homepage
    displayWineList(); // Display the list of wines
});

// Function to display wine details with average rating
function displayWineDetails(wineId) {
    fetch(`http://localhost:9090/customers/wines/${wineId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch wine details');
            }
            return response.json();
        })
        .then(wine => {
            // Fetch average rating
            fetch(`http://localhost:9090/customers/wines/${wineId}/averageRating`)
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Failed to fetch average rating');
                    }
                    return response.json();
                })
                .then(averageRating => {
                    const starRatingHTML = displayAverageStarRating(averageRating);
                    // Update wine details in the modal
                    const wineModalBody = document.getElementById('wineModalBody');
                    console.log(wineModalBody);
                    wineModalBody.querySelector('.wine-image').src = `../images/${wine.picture}`;
                    wineModalBody.querySelector('.wine-name').textContent = wine.name;
                    wineModalBody.querySelector('.wine-year').textContent = wine.year;
                    wineModalBody.querySelector('.wine-color').textContent = wine.color;
                    wineModalBody.querySelector('.wine-winery').textContent = wine.winery;
                    wineModalBody.querySelector('.wine-grapes').textContent = wine.grapes;
                    wineModalBody.querySelector('.wine-country').textContent = wine.country;
                    wineModalBody.querySelector('.wine-region').textContent = wine.region;
                    wineModalBody.querySelector('.wine-description').textContent = wine.description;
                    wineModalBody.querySelector('.wine-price').textContent = `€${wine.price}`;
                    wineModalBody.querySelector('.average-star-rating').innerHTML = starRatingHTML;
                    wineModalBody.querySelector('.average-rating-value').textContent = `(${averageRating.toFixed(1)})`;
                    // Set data attribute to the submit rating button for identification
                    const submitRatingButton = wineModalBody.querySelector('.submit-rating-button');
                    submitRatingButton.setAttribute('data-wine-id', wineId);
                    
                    // Add "Add to Cart" button
	                const addToCartButton = document.createElement('button');
	                addToCartButton.textContent = 'Add to Cart';
	                addToCartButton.classList.add('btn', 'btn-primary', 'add-to-cart-button');
	                addToCartButton.setAttribute('data-wine-id', wineId);
	                addToCartButton.addEventListener('click', function() {
	                    const wineId = this.dataset.wineId;
	                    addToCart(wineId);
	                });
	                wineModalBody.querySelector('.add-to-cart-button-container').innerHTML = ''; // Clear previous button if any
	                wineModalBody.querySelector('.add-to-cart-button-container').appendChild(addToCartButton);
                 
                 
                 
                    // Show the modal
                    $('#wineModal').modal('show');
                })
                .catch(error => {
                    console.error('Error fetching average rating:', error);
                    // Handle errors or display error message to the user
                });
        })
        .catch(error => {
            console.error('Error fetching wine details:', error);
            // Handle errors or display error message to the user
        });
}

document.getElementById('wineListBody').addEventListener('click', function(event) {
    if (event.target.classList.contains('view-details-button')) {
        const wineId = event.target.dataset.wineId;
        displayWineDetails(wineId);
    }
});

document.addEventListener("DOMContentLoaded", function() {
    // Attach event listener to the submit rating button
    document.getElementById('wineModalBody').addEventListener('click', function(event) {
        if (event.target.classList.contains('submit-rating-button')) {
            const wineId = event.target.getAttribute('data-wine-id');
            submitRating(wineId);
        }
    });
});

function submitRating(wineId) {
    const rating = parseInt(document.getElementById('rating').value);
    if (isNaN(rating) || rating < 1 || rating > 5) {
        console.error('Invalid rating value. Rating must be between 1 and 5.');
        alert('Please enter a valid value.');
        // Handle error or display error message to the user
        return;
    }

    // Make a POST request to submit the rating
    fetch(`http://localhost:9090/customers/wines/${wineId}/rating`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ rating: rating })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Failed to submit rating');
        }
        // Handle success (e.g., update UI)
        displayWineDetails(wineId); // Update the wine details to reflect the new rating
        // Maybe close the modal or display a success message
        
        
        
        
        
        
        // Clear the quantity and rating fields
		document.getElementById('quantity').value = '';
		document.getElementById('rating').value = '';
    })
    .catch(error => {
        console.error('Error submitting rating:', error);
        // Handle errors or display error message to the user
    });
}

function displayAverageStarRating(averageRating) {
    const roundedRating = Math.round(averageRating * 2) / 2;
    let starHTML = '';
    for (let i = roundedRating; i >= 1; i--) {
        starHTML += '<i class="fa fa-star" style="color: gold"></i>';
    }
    if (roundedRating % 1 !== 0) {
        starHTML += '<i class="fa fa-star-half-o" style="color: gold"></i>';
    }
    const remainingStars = 5 - Math.ceil(roundedRating);
    for (let i = 0; i < remainingStars; i++) {
        starHTML += '<i class="fa fa-star-o" style="color: gold"></i>';
    }
    return starHTML;
}

// Event listener for the Cart button
document.getElementById('cartButtonHome').addEventListener('click', function() {
    // Implement your logic here to handle the Cart button click event
    console.log("Cart button clicked");
});

// Event listener for the "Add to Cart" button within the wine details modal
document.getElementById('wineModalBody').addEventListener('click', function(event) {
    if (event.target.classList.contains('add-to-cart-button')) {
		event.stopPropagation(); // Stop event propagation
		const wineId = event.target.getAttribute('data-wine-id');
 //       addToCart(wineId);
    }
});

// Function to add the selected wine to the cart
function addToCart(wineId) {
	console.log("Adding wine with ID " + wineId + " to the cart...");
	
	if (customerId === null || isNaN(parseFloat(customerId))) {
	    console.error('Invalid or missing customer ID');
	    alert("Please, login to add a wine to cart!");
	    return;
	}
	
    // Get quantity from the input field
    const quantity = parseInt(document.getElementById('quantity').value);
    if (isNaN(quantity) || quantity < 1) {
        alert('Please enter a valid quantity.');
        return;
    }

    // Make a POST request to add the wine to the cart
    fetch(`/serviceLayer/${customerId}/newOrder`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            wineId: wineId, // Pass the wine ID in the request body
            quantity: quantity,
            status: "Placed"
        }),
    })
    .then(response => {
        if (response.ok) {
            // Wine added to cart successfully
            alert('Wine added to cart');
            // Clear the quantity and rating fields
			document.getElementById('quantity').value = '';
			document.getElementById('rating').value = '';
            // Optionally, you can redirect to the cart page or update the UI to reflect the addition
        } else {
            // Handle error response
            alert('Failed to add wine to cart');
        }
    })
    .catch(error => {
        console.error('Error adding wine to cart:', error);
        alert('Failed to add wine to cart');
    });
}









var deleteOrder=function() {
	$('#orders').hide(); // Hide orders table if it's visible
    $('#customers').show(); // Show customers table
	var orderid = $('#orderID').val();
	console.log("Order to be deleted: "+orderid);
	$.ajax({
		type: 'DELETE',
		url: rootURL +'/serviceLayer/orders/' + orderid, 

		success: function(data, textStatus, jqXHR){
			alert('Order deleted successfully');
			$('#details Table').hide();
			$('#' + orderid).closest('tr').remove(); // Remove the row with the order ID
			findAllCustomers();
			},
			error: function(jqXHR, textStatus, errorThrown) {
				alert('delete Order error');
			}
		});
};

var updateOrder = function(orderId, wineId, quantity){
	
	var wineId = $('#prodID').val(); // Fetch wine ID from the input field
	var quantity = $('#prodQuantity').val(); // Fetch quantity from the input field
	console.log('updating order');
	
	if (!orderId || !wineId || !quantity) {
	     console.error("Invalid order, wine ID, or quantity");
	     return;
	 }
	 
	 console.log("Order: ", orderId, "Wine:", wineId, "Quantity:", quantity)
	$.ajax({
		type: 'PUT',
		contentType: 'application/json',
		url: rootURL + '/serviceLayer/orders/' + orderId,
		dataType: 'json',
		data: formToJSONCart(orderId, wineId, quantity),
		success: function(data, textStatus, jqXRH){
			alert('Order Updated Successfully');
			findOrderById(orderId); // Fetch and update the order details
		},
		error: function(jqXHR, textStatus, errorThrown) {
			alert('updateOrder error: '+ textStatus);
		}
	});
};

var findOrderById = function (id) {
	console.log("Fetching order details for ID:", id); // Log the order ID being fetched
	$.ajax({
		type: 'GET', 
		url: rootURL +"/serviceLayer/orders/"+id, 
		dataType: "json", 
		success: function (order) {
            console.log("Order details fetched successfully:", order); // Log the fetched order details
			editOrder(order);
		},
		error: function(xhr, status, error) {
			console.error('Error fetching orders:', error);
			$('#details').html('<h2 class="w3-row w3-red">Error</h2><p>Error fetching orders. Please try again later.</p>');
		}
	});
}

function editOrder(order) {
    $('#orderID').val(order.id);
    if (order.wine) {
        $('#prodID').val(order.wine.id);
        $('#prodName').val(order.wine.name);
        $('#prodPrice').val(order.wine.price);
    } else {
        // Handle case where wine information is missing
        console.error('Wine information missing for order:', order);
    }
    $('#prodQuantity').val(order.quantity);
    console.log("This product is: " + (order.wine.id ? order.wine.name : 'Unknown') + "\tQuantity: " + order.quantity);
    $('#orderID_row').hide();
    $('#detailsTable').append('<button id="closeOrderButton" class="w3-button w3-red">Close</button>');
}



function findAllCustomers(customerId) {
	console.log('findAllCustomers!');
    $('#winesTable').hide(); // Hide any existing tables
    $('#orders').hide(); // Hide orders table if it's visible
    $('#customers').show(); // Show customers table
	$.ajax({
		url: rootURL + '/serviceLayer/customers',
		type: 'GET', 
		success: function(customers) { 
			$('#detailsTable').hide();
			renderCustomers(customers); 
			console.log(customers);
		},
		error: function(xhr, status, error) {
			console.error('Error fetching customers:', error);
			$('#customers').html('<p>Error fetching customers. Please try again later.</p>');
		}
	});
}

function renderCustomers(customers) {
    var customersHtml = "<table class='table'>";
    customersHtml += "<tr><th>ID</th><th>Login</th><th>Email</th><th>Role</th><th>Actions</th></tr>";
    $.each(customers, function (index, customer) {
        customersHtml += '<tr><td>' + customer.id + '</td><td>' + customer.login + '</td><td>' + customer.email + '</td><td>' + '</td><td>' + customer.role + '</td><td><button class="viewOrdersButton w3-button w3-green" data-customer-id="' + customer.id + '">View Orders</button></td></tr>';
    });
    customersHtml += '</table>';
    $('#customers').html(customersHtml);
}

// Function to fetch and render orders for a customer
function viewOrders(customerId) {
	$('#orders').show(); // Hide orders table if it's visible
    $('#customers').hide(); // Show customers table
    console.log('View orders for customer:', customerId);
    $.ajax({
        url: rootURL + '/serviceLayer/' + customerId + '/orders',
        type: 'GET',
        success: function (orders) {
            renderOrders(orders);
        },
        error: function (xhr, status, error) {
            console.error('Error fetching orders:', error);
            $('#orders').html('<p>Error fetching orders. Please try again later.</p>');
        }
    });
}

function renderOrders(response) {
    var orders = response._embedded ? response._embedded.ordersList : []; // Check if orders exist in the response
    console.log(orders); // Log the orders array
    
    var ordersHtml = "<table class='w3-table'>";
    ordersHtml += "<tr><th>Order ID</th><th>Product ID</th><th>Product Name</th><th>Price</th><th>Quantity</th><th>Total</th><th>Status</th><th>Edit</th></tr>";
    if (orders.length > 0) {
        $.each(orders, function (index, order) {
            console.log(order); // Log each order object
            var total = (order.quantity * order.wine.price).toFixed(2);
            ordersHtml += '<tr><td>' + order.id + '</td><td>' + order.wine.id + '</td><td>' + order.wine.name + '</td><td>' + order.wine.price + '</td><td>' + order.quantity + '</td><td>' + total + '</td>';
            // Add a dropdown menu for selecting the status
            ordersHtml += '<td><select class="statusSelect">';
            ordersHtml += '<option value="Placed"' + (order.status === "Placed" ? " selected" : "") + '>Placed</option>';
            ordersHtml += '<option value="Accepted"' + (order.status === "Accepted" ? " selected" : "") + '>Accepted</option>';
            ordersHtml += '<option value="Sent"' + (order.status === "Sent" ? " selected" : "") + '>Sent</option>';
            ordersHtml += '</select></td>';
			ordersHtml += '<td><button class="editOrderButton w3-button w3-green" order-id="' + order.id + '" wine-id="' + order.wine.id + '" quantity="' + order.quantity + '">Edit</button></td>';
        });
    } else {
        // Display a message if there are no orders for the customer
        ordersHtml += '<tr><td colspan="6">No orders found for this customer</td></tr>';
    }
    ordersHtml += '</table>';
    $('#orders').html(ordersHtml); // Display orders in the #orders element
}

$(document).ready(function(){
	// Attach click event listener to the button
	$(document).on("click", "#fetchCustomersBtn", function () {
    	findAllCustomers(); // Call your existing function here
	});

	$(document).on("click", ".viewOrdersButton", function () {
		var customerId = $(this).data('customer-id');
        viewOrders(customerId); // Fetch orders for the current user
    });
    
    $(document).on("click", ".editOrderButton", function () {
		var orderId = $(this).attr('order-id'); // Retrieve the order ID using the custom attribute
		console.log("Edit button clicked for order " + orderId);
		findOrderById(orderId);
		$('#detailsTable').show();
		return false;
	});
	
	$(document).on("click", "#UpdateOrder", function (){
	    console.log("Updating order!!");
	    var orderId = $('#orderID').val(); // Get the order ID from the input field
	    console.log("Order:", orderId);
	    var wineId = $('#prodID').val(); // Get the wine ID from the input field
	    console.log("Wine:", wineId);
	    var quantity = $('#prodQuantity').val(); // Get the quantity from the input field
	    console.log("Quantity:", quantity);
	    if (!orderId || !wineId || !quantity) {
	        console.error("Invalid order, wine ID, or quantity");
	        return;
	    }
	    updateOrder(orderId, wineId, quantity);
	    console.log("Update button clicked for order " + orderId); 
	});
	
	$(document).on("click", "#deleteOrder", function () {
		deleteOrder();
		console.log("Delete button clicked for order "+this.id); 
		return false;
	});
	
	
	$(document).on("click", "#closeOrderButton", function () {
        closeOrderDetails(); // Call the function to close order details
        return false; // Prevent default behavior of the button
    });

    // Function to close order details and show customers table
    function closeOrderDetails() {
        $('#detailsTable').hide(); // Hide the order details table
        $('#orders').show(); // Hide orders table if it's visible
        $('#customers').show(); // Show the customers table
    }
});














$(document).ready(function() {
	// Handle form submission for search
	$('#search-by-name').submit(function(event) {
	    event.preventDefault(); // Prevent default form submission behavior
	
	    let searchQuery = $('#searchTextList').val().trim(); // Get the search query from the input
	
		if(searchQuery === "") {
			displayWineList(); // Display the list of wines
		} else {
		    // Send search query via AJAX
		    $.ajax({
		        type: "GET",
		        url: rootURL + "/wines/name/" + searchQuery,
		        success: function(data) {
		            console.log("Search successful:", data);
		            searchByName(data); // Render search results
		        },
		        error: function(xhr, status, error) {
		            console.error("Search failed:", error);
		            // Display appropriate error message to the user
		            alert("Search failed: " + error);
		        }
		    });
	    }
	});   

	// Function to render search results
	function searchByName(data) {
	    $('#wineListBody').empty(); // Clear previous search results
	
	    if (data.length > 0) {
	        // Create a row to contain the search results
	        var row = $('<div>').addClass('row');
	        
	        // Render each wine in the search results
	        data.forEach(wine => {
                var wineCardHtml = `
                    <div class="col-md-6">
                        <div class="wine-card">
                            <img src="../images/${wine.picture}" alt="Wine Image" class="wine-image">
                            <div class="wine-details">
                                <h3 class="wine-name">${wine.name}</h3>
                                <p class="wine-description">${wine.description}</p>
                                <p class="wine-price">€${wine.price}</p>
                                <button class="btn btn-primary view-details-button" data-wine-id="${wine.id}" data-bs-toggle="modal" data-bs-target="#wineModal">View Details</button>
                            </div>
                        </div>
                    </div>
                `;
                row.append(wineCardHtml);
            });
            
            $('#wineListBody').append(row); // Append the row to the wine list body
        	$('#wineListDiv').show(); // Display the wine list section
            /*document.getElementById('wineListBody').innerHTML = row;
            document.getElementById('wineListDiv').style.display = 'block'; // Display the wine list section*/
	    } else {
	        // If no wines found, display a message to the user
	        $('#wineListBody').html('<p>No wines found</p>');
	    }
	}
});


















// Function to toggle the visibility of the cart dropdown
function toggleCartDropdown() {
    var cartDropdown = document.getElementById("cartDropdown");
    cartDropdown.classList.toggle("visible");
}

function makeCartDropdownInvisible() {
    var cartDropdown = document.getElementById("cartDropdown");
    cartDropdown.classList.remove("visible");
}

// Event listener to toggle cart dropdown when cart button is clicked
document.getElementById("cartButtonHome").addEventListener("click", function() {
	fetchCartData(customerId);
    toggleCartDropdown();
});

// Function to fetch cart data from backend including orders by customer ID
function fetchCartData(customerId) {
	console.log('View orders for customer:', customerId);
    // Make AJAX request to fetch orders for the specified customer ID
    $.ajax({
        type: "GET",
        url: rootURL + "/serviceLayer/" + customerId + "/orders",
        success: function(data) {
            // Process the fetched orders data
            displayCartData(data); // Call a function to display the cart data
        },
        error: function(xhr, status, error) {
            console.error("Error fetching cart data:", error);
            // Handle error appropriately
        }
    });
}

// Function to display cart data in the dropdown
function displayCartData(data) {
    var orders = data._embedded ? data._embedded.ordersList : []; // Check if orders exist in the response
    console.log(orders); // Log the orders array
    // Clear previous cart items from the dropdown
    $("#cartDropdown").empty();
    
    var total = 0; // Initialize total price variable
    
    // Create table structure for cart items
    var cartTable = $("<table class='cart-table'></table>");
    var tableHeader = $("<tr class='cart-header'></tr>");
    tableHeader.append("<th>Wine Name</th>");
    tableHeader.append("<th>Price</th>");
    tableHeader.append("<th>Quantity</th>");
    tableHeader.append("<th>Actions</th>"); // Header for edit/delete buttons
    cartTable.append(tableHeader);
        
    // Iterate over the fetched orders and populate the table
    $.each(orders, function (index, order) {
        console.log(order); // Log each order object
        
        // Create HTML elements to display order details in a table row
        var row = $("<tr class='cart-row'></tr>");
        row.data('order-id', order.id);
        row.append("<td>" + order.wine.name + "</td>"); // Display wine name
        row.append("<td>€" + order.wine.price + "</td>"); // Display price
//        row.append("<td>" + order.quantity + "</td>"); // Display quantity
        
        var quantityContainer = $("<div class='quantity-container'></div>");
        var decreaseButton = $("<button class='quantity-button decrease'>-</button>");
        var quantityDisplay = $("<span type='number' id='quantityCart' class='quantity-display'>" + order.quantity + "</span>");
        var increaseButton = $("<button class='quantity-button increase'>+</button>");
        quantityContainer.append(decreaseButton, quantityDisplay, increaseButton);
        
//        row.append("<td>$" + totalPrice + "</td>"); // Display total price for this order
        var editOrderButtonCart = $("<button class='editOrderButtonCart'>Edit Order</button>");
        editOrderButtonCart.data('order-id', order.id); // Set the data-order-id attribute
        editOrderButtonCart.data('wine-id', order.wine.id); // Set the data-wine-id attribute
        
        
        var deleteOrderButtonCart = $("<button class='deleteOrderButtonCart'>Delete Order</button>");
        deleteOrderButtonCart.data('order-id', order.id); // Set the data-order-id attribute
        
        var actionsCell = $("<td></td>");
        actionsCell.append(editOrderButtonCart, deleteOrderButtonCart);
        row.append(quantityContainer, actionsCell);

        cartTable.append(row);

       // $("#cartTableBody").append(row);
        
        total += order.wine.price * order.quantity;
    });
    
    // Append the table to the dropdown
    $("#cartDropdown").append(cartTable);
    
    // Update total section
    var totalSection = $("<div class='total-section'></div>");
    totalSection.text("Total: $" + total.toFixed(2));
    $("#cartDropdown").append(totalSection);
    
     // Add a checkout button to the dropdown
	var checkoutButton = $("<button class='checkoutButton'>Checkout</button>");
	$("#cartDropdown").append(checkoutButton);
    
    // Add event listeners for quantity buttons
    $(".quantity-button.decrease").on("click", function () {
        var quantityDisplay = $(this).siblings(".quantity-display");
        var quantity = parseInt(quantityDisplay.text());
        if (quantity > 1) {
            quantityDisplay.text(quantity - 1);
            // Update cart data and UI accordingly (optional)
        }
    });

    $(".quantity-button.increase").on("click", function () {
        var quantityDisplay = $(this).siblings(".quantity-display");
        var quantity = parseInt(quantityDisplay.text());
        quantityDisplay.text(quantity + 1);
        // Update cart data and UI accordingly (optional)
    });
}

// Function to clear the dropdown after successful payment
function clearDropdown() {
 //   $("#cartDropdown").empty(); // Clear the dropdown
    $("#paymentPage").hide(); // Hide the payment page
}

// Assume payment is confirmed and redirected back to the main page after payment
$(document).ready(function() {
    clearDropdown();
});

// Event listener for the checkout button
$(document).on("click", ".checkoutButton", function() {
    // Hide the cart dropdown
    $("#cartDropdown").hide();
    $('#homePage').hide();
    $('#wineListDiv').hide();
    // Show the payment page
    $("#paymentPage").show();
});

// Event listener for the Edit Order button
$(document).on("click", ".editOrderButtonCart", function() {
    var orderId = $(this).data('order-id'); // Get the order ID from the data attribute
    var wineId = $(this).data('wine-id'); // Get the wine ID from the data attribute
    var quantityDisplay = $(this).parent().prev().find('.quantity-display'); // Find the quantity display element
    var quantity = parseInt(quantityDisplay.text()); // Extract the quantity from the display
    if (!orderId || !wineId || isNaN(quantity)) {
        console.error("Invalid order, wine ID, or quantity");
        return;
    }
    updateOrderCart(orderId, wineId, quantity); // Call updateOrderCart with the order ID, wine ID, and quantity
});

//Event listener for Delete Order Button
$(document).on("click", ".deleteOrderButtonCart", function() {
    var orderId = $(this).data('order-id'); // Get the order ID from the data attribute
    if (!orderId) {
        console.error("Invalid order ID");
        return;
    }
    deleteOrderCart(orderId); // Call deleteOrderCart with the order ID
});


var updateOrderCart = function(orderId, wineId, quantity){
    console.log('updating order');
    // Validate order, wine, and quantity before making the AJAX request
    if (!orderId || !wineId || !quantity) {
        console.error("Invalid order, wine ID, or quantity");
        return;
    }
    $.ajax({
        type: 'PUT',
        contentType: 'application/json',
        url: rootURL + '/serviceLayer/orders/' + orderId,
        dataType: 'json',
        data: formToJSONCart(orderId, wineId, quantity), // Pass the order ID, wine ID, and quantity to the formToJSON function
        success: function(data, textStatus, jqXRH){
            alert('Order Updated Successfully');
            fetchCartData(customerId); // Fetch and update the order details
        },
        error: function(jqXHR, textStatus, errorThrown) {
            alert('updateOrder error: '+ textStatus);
        }
    });
};

var deleteOrderCart = function(orderId) {
    console.log('deleting order');
    $.ajax({
        type: 'DELETE',
        url: rootURL + '/serviceLayer/orders/' + orderId,
        success: function(data, textStatus, jqXRH) {
            alert('Order Deleted Successfully');
            fetchCartData(customerId); // Fetch and update the order details
        },
        error: function(jqXHR, textStatus, errorThrown) {
            alert('deleteOrder error: '+ textStatus);
        }
    });
};

// Call fetchCartData when page loads to populate the cart dropdown initially
document.addEventListener("DOMContentLoaded", function() {
    fetchCartData();
});

$(document).on("click", "#processPayment", function() {
    // Validate card number
    var cardNumber = $("#cardNumber").val();
    if (!isValidCardNumber(cardNumber)) {
        alert("Invalid card number. Please enter a valid 16-digit card number.");
        return;
    }

    // Validate expiration date
    var expirationDate = $("#expirationDate").val();
    if (!isValidExpirationDate(expirationDate)) {
        alert("Invalid expiration date. Please enter a valid MM/YY format.");
        return;
    }

    // Validate CVV code
    var cvvCode = $("#cvvCode").val();
    if (!isValidCVV(cvvCode)) {
        alert("Invalid CVV code. Please enter a valid 3-digit CVV code.");
        return;
    }

    // Payment processing successful, show success message and redirect
    alert("Payment processed successfully!");
    // Redirect to home page
   	HomePageDashboard();
   	$("#paymentPage").hide();
});

function isValidCardNumber(cardNumber) {
    return /^\d{16}$/.test(cardNumber);
}

function isValidExpirationDate(expirationDate) {
    // Assuming expirationDate is in MM/YY format
    var regex = /^(0[1-9]|1[0-2])\/?([0-9]{2})$/;
    return regex.test(expirationDate);
}

function isValidCVV(cvvCode) {
    return /^\d{3}$/.test(cvvCode);
}

document.addEventListener('DOMContentLoaded', function() {
    const viewDetailButtons = document.querySelectorAll('.view-details-button');
    viewDetailButtons.forEach(button => {
        button.addEventListener('click', function() {
            const wineId = this.getAttribute('data-wine-id');
            displayWineDetails(wineId);
        });
    });
});