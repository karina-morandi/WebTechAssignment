//package com.tus.jpa.repositories;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import com.tus.jpa.dto.Admin;
//
//@Repository
//public interface AdminRepository extends JpaRepository<Admin,Long>{
//	
//	Admin findByLogin(String login);
//}


//
//	
//			function editOrder(order) {
//    $('#orderID').val(order.id);
//    if (order.wine) {
//        $('#prodID').val(order.wine.id);
//        $('#prodName').val(order.wine.name);
//        $('#prodPrice').val(order.wine.price);
//    } else {
//        console.error('Wine information missing for order:', order);
//    }
//    $('#prodQuantity').val(order.quantity);
//    console.log("This product is: " + (order.wine.id ? order.wine.name : 'Unknown') + "\tQuantity: " + order.quantity);
//    $('#orderID_row').hide();
//
//    // Append close button to the details table
//    $('#detailsTable').append('<button id="closeOrderButton" class="w3-button w3-red">Close</button>');
//}
