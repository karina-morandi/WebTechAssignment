package com.tus.jpa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tus.jpa.dto.Orders;

@Repository
public interface OrdersRepository extends JpaRepository<Orders,Long>{
	
//	List<Orders> findByCustomerEmail(String customerEmail);
	Optional<Orders> findById(Long id);
}






//
//
//row.append("<td>$" + totalPrice + "</td>"); // Display total price for this order
//var editOrderButtonCart = $("<button class='editOrderButtonCart'>Edit Order</button>");
//editOrderButtonCart.data('order-id', order.id); // Set the data-order-id attribute
//editOrderButtonCart.data('wine-id', order.wine.id); // Set the data-wine-id attribute
//
//
//var deleteOrderButtonCart = $("<button class='deleteOrderButtonCart'>Delete Order</button>");
//deleteOrderButtonCart.data('order-id', order.id); // Set the data-order-id attribute
//
//var actionsCell = $("<td></td>");
//actionsCell.append(editOrderButtonCart, deleteOrderButtonCart);
//row.append(quantityContainer, actionsCell);
//
//cartTable.append(row);
//
//
//
////Event listener for the Edit Order button
//$(document).on("click", ".editOrderButtonCart", function() {
// var orderId = $(this).data('order-id'); // Get the order ID from the data attribute
// var wineId = $(this).data('wine-id'); // Get the wine ID from the data attribute
// var quantityDisplay = $(this).parent().prev().find('.quantity-display'); // Find the quantity display element
// var quantity = parseInt(quantityDisplay.text()); // Extract the quantity from the display
// if (!orderId || !wineId || isNaN(quantity)) {
//     console.error("Invalid order, wine ID, or quantity");
//     return;
// }
// updateOrderCart(orderId, wineId, quantity); // Call updateOrderCart with the order ID, wine ID, and quantity
//});
//
//
//
//var updateOrderCart = function(orderId, wineId, quantity){
//    console.log('updating order');
//    // Validate order, wine, and quantity before making the AJAX request
//    if (!orderId || !wineId || !quantity) {
//        console.error("Invalid order, wine ID, or quantity");
//        return;
//    }
//    $.ajax({
//        type: 'PUT',
//        contentType: 'application/json',
//        url: rootURL + '/serviceLayer/orders/' + orderId,
//        dataType: 'json',
//        data: formToJSONCart(orderId, wineId, quantity), // Pass the order ID, wine ID, and quantity to the formToJSON function
//        success: function(data, textStatus, jqXRH){
//            alert('Order Updated Successfully');
//            fetchCartData(customerId); // Fetch and update the order details
//        },
//        error: function(jqXHR, textStatus, errorThrown) {
//            alert('updateOrder error: '+ textStatus);
//        }
//    });
//};