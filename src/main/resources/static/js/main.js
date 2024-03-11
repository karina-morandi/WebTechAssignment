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
    $('#wineListDiv').hide();
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

let rootURL = "http://localhost:9090";
var currentViewType = 'list'; // Default view type
var wineData;

function findAll(){
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
                    // Generate star rating HTML
                    const starRatingHTML = generateStarRating(wine.rating);
                    wineListHTML += `
                        <div class="col-md-6">
                            <div class="wine-card">
                                <img src="../images/${wine.picture}" alt="Wine Image" class="wine-image">
                                <div class="wine-details">
                                    <h3 class="wine-name">${wine.name}</h3>
                                    <p class="wine-description">${wine.description}</p>
                                    <p class="wine-price">€${wine.price}</p>
                                    <div class="star-rating">${starRatingHTML}</div>
                                    <div class="review-form">
                                        <label for="review${wine.id}">Add Review (0-5 stars):</label>
                                        <input type="number" id="review${wine.id}" min="0" max="5">
                                        <button class="btn btn-primary" onclick="submitReview(${wine.id})">Submit Review</button>
                                    </div>
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







function getStars(rating) {
    rating = Math.round(rating * 2)/2;
    let output = '<div><h3>';
    for (var i = rating; i >= 1; i--)
        output += '<i class="fa fa-star" style="color:gold"></i>';
    if(i==.5)
        output += '<i class="fa fa-star-half-o" style="color:gold"></i>';
    for(let i = (5-rating); i >= 1; i--)
        output += '<i class="fa fa-star-o" style="color:gold"></i>'

    return output;
}

$(window).on('load', function() {
    // console.log("Hello World");

    $(document).on("click", "#createStars", function() {
        // console.log("Generate Clicked");
        $('#theStars').html('');
        let numericalStarValue = $('#starValue').val();
        // console.log('Number of Stars = ' + numericalStarValue + 'stars');

        if(isNaN(numericalStarValue) || numericalStarValue < 0 || numericalStarValue > 5) {
            console.log("Something went wrong, all values must be numerical and between 1 and 5");
            alert("Something went wrong, all values must be numerical and between 1 and 5");
        }
        else {
            let stars = getStars(parseFloat(numericalStarValue));
            console.log("Stars ----> " + stars)
            $('#theStars').append(stars);
        }
    });

    $(document).on("click", "#clearSelection", function() {
        console.log("Reset button has been clicked");
        $('#theStars').html('<br/>');
        $('#starValue').val('');
    });
});
