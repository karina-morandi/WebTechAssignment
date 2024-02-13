var rootURL = "http://localhost:9090/wines";
var currentViewType = 'list'; // Default view type
var wineData;

var findAll = function(){
    console.log("Find all wines");
    $.ajax({
        type: 'GET',
        url: rootURL,
        dataType: "json",
        success: function(data){
            console.log("AJAX request successful:", data);
            wineData = data;
            $('#dashboard_title').text("All Products");
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
    $('#listView, #gridView').on('change', function() {
        console.log("View type changed");
        currentViewType = $(this).val();
        console.log("Current view type:", currentViewType);
        renderContent(); // Render content based on the current view type
    });

    findAll();
});