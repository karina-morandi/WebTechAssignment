package com.tus.jpa;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.anyLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.tus.jpa.controllers.CustomersController;
import com.tus.jpa.repositories.OrdersRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.tus.jpa.controllers.RegistrationController;
import com.tus.jpa.dto.Orders;
import com.tus.jpa.dto.UserInfo;
import com.tus.jpa.dto.Users;
import com.tus.jpa.dto.Wines;
import com.tus.jpa.repositories.UserRepository;
import com.tus.jpa.service.CustomerService;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {
	  private RegistrationController registrationController;
	  private CustomersController customerController;
	  private CustomerService customerService;
	  private UserRepository userRepo;
	  private PasswordEncoder passwordEncoder;
	  private OrdersRepository ordersRepository;
	  
	  private static final Long CUSTOMER1_ID = 1L;
	  private static final Long CUSTOMER2_ID = 2L;

	  private static final String CUSTOMER1_LOGIN = "user1";
	  private static final String CUSTOMER2_LOGIN = "user2";

	  @BeforeEach
	  void setUp() {
	      userRepo = Mockito.mock(UserRepository.class);
	      passwordEncoder = mock(PasswordEncoder.class);
	      customerService = mock(CustomerService.class);
	      ordersRepository = mock(OrdersRepository.class);
	      customerController = new CustomersController();
	      customerController.setUserRepo(userRepo); // Injecting the mock repository
	      customerController.setOrdersRepo(ordersRepository);
	      customerController.setCustomerService(customerService);
	      registrationController = new RegistrationController(userRepo, passwordEncoder);
	  }

	  @Test
	    void testGetAllCustomers() {
	        // Arrange
	        List<Users> allCustomers = Arrays.asList(
	                new Users(CUSTOMER1_LOGIN, "user1@root.ie", "password", "CUSTOMER"),
	                new Users(CUSTOMER2_LOGIN, "user2@root.ie","password", "CUSTOMER")
	        );

	        when(customerService.getAllCustomers()).thenReturn(allCustomers);

	        // Act
	        List<Users> result = customerController.getAllCustomers();

	        // Assert
	        assertEquals(allCustomers, result);
	        verify(customerService, times(1)).getAllCustomers();
	    }
	  
	  @Test
	    void testGetCustomerById() {
	        // Arrange
	        Users customer = new Users(CUSTOMER1_LOGIN, "user1@root.ie", "password", "CUSTOMER");

	        when(customerService.getCustomerById(any(Long.class))).thenReturn(Optional.of(customer));

	        // Act
	        EntityModel<Users> result = customerController.getCustomerById(CUSTOMER1_ID);

	        // Assert
	        assertTrue(result instanceof EntityModel);
	        assertEquals(customer, result.getContent());
	        verify(customerService, times(1)).getCustomerById(CUSTOMER1_ID);
	    }


	  @Test
	  void testGetCustomerByIdNotFound() {
	      // Arrange
	      when(customerService.getCustomerById(CUSTOMER1_ID)).thenReturn(Optional.empty());

	      // Act and Assert
	      assertThrows(RuntimeException.class, () -> {
	          customerController.getCustomerById(CUSTOMER1_ID);
	      });

	      verify(customerService, times(1)).getCustomerById(CUSTOMER1_ID);
	  }

	  @Test
	  void testGetUserByName() {
	      String login = "username";
	      Long userId = 123L; // Sample user ID
	      Users user = new Users(login, "user1@root.ie", "password", "CUSTOMER");
	      user.setId(userId);
	      
	      // Mocking repository behavior to return a valid user
	      when(userRepo.findByLogin(login)).thenReturn(user);

	      // Act
	      Long returnedUserId = customerController.getUserByName(login);

	      // Assert
	      assertNotNull(returnedUserId);
	      assertEquals(userId, returnedUserId);
	  }

	  @Test
	  void testAuthenticate_UserExistsAndPasswordCorrect() {
	      // Arrange
	      String password = "password";
	      String encodedPassword = passwordEncoder.encode(password);
	      Users user = new Users();
	      user.setLogin("username");
	      user.setPassword(encodedPassword);
	      user.setRole("CUSTOMER"); // set a non-null role
	      when(userRepo.findByLogin(anyString())).thenReturn(user);
	      when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

	      // Act
	      ResponseEntity<?> responseEntity = registrationController.authenticate(new UserInfo("username", password));

	      // Assert
	      assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
	      assertEquals("Customer", responseEntity.getBody());
	  }
    
	  @Test
	  void testAuthenticate_UserExistsAndPasswordIncorrect() {
	      String password = "password";
	      String encodedPassword = passwordEncoder.encode(password);
	      Users user = new Users();
	      user.setLogin("username");
	      user.setPassword(encodedPassword);
	      user.setRole("CUSTOMER"); // set a non-null role
	      when(userRepo.findByLogin(anyString())).thenReturn(user);
	      when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

	      ResponseEntity<?> responseEntity = registrationController.authenticate(new UserInfo("username", password));
	      assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
	      assertEquals("Invalid username or password", ((Map<String, String>) responseEntity.getBody()).get("error"));
	  }
    
    @Test
    void testAuthenticate_UserDoesNotExist() {
        when(userRepo.findByLogin(anyString())).thenReturn(null);

        UserInfo userInfo = new UserInfo("username","password");

        ResponseEntity<?> responseEntity = registrationController.authenticate(userInfo);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("This user does not exist!", ((Map<String, String>) responseEntity.getBody()).get("error"));
    }  
    
    @Test
    void testDeleteUser() {
        // Mock
        doNothing().when(customerService).deleteUser(anyLong());

        // Test
        assertDoesNotThrow(() -> customerController.deleteUser(1L));
        verify(customerService, times(1)).deleteUser(1L);
    }

    @Test
    void testGetOrderById_OrderFound() {
        // Mock
        Orders expectedOrder = new Orders(); // create an expected order
        when(ordersRepository.findById(anyLong())).thenReturn(Optional.of(expectedOrder));

        // Test
        Orders actualOrder = customerController.getOrderById(1L);
        assertEquals(expectedOrder, actualOrder);
    }

    @Test
    void testGetOrderById_OrderNotFound() {
        // Mock
        when(ordersRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Test
        assertThrows(RuntimeException.class, () -> customerController.getOrderById(1L));
    }

    @Test
    void testGetAllOrdersByCustomerId() {
        // Mock
        List<Orders> orders = Arrays.asList(new Orders(), new Orders()); // create some sample orders
        when(customerService.getAllOrdersByCustomerId(anyLong())).thenReturn(orders);

        // Test
        CollectionModel<Orders> result = customerController.getAllOrdersByCustomerId(1L);
        assertNotNull(result);
        assertEquals(2, result.getContent().size()); // assert the number of orders

        // Print self link and result
        Link selfLink = linkTo(methodOn(CustomersController.class).getAllOrdersByCustomerId(1L)).withSelfRel();

        // assert the self link
        assertTrue(result.getLinks().contains(selfLink));
    }

    @Test
    void testCreateOrder_CustomerFound() {
        // Mock customer
        Users user = new Users(); // create a mock user
        when(customerService.getCustomerById(anyLong())).thenReturn(Optional.of(user));

        // Mock order
        Orders order = new Orders(); // create a mock order
        order.setWine(new Wines()); // set wine in the order
        when(customerService.saveOrder(any(Orders.class))).thenReturn(order);

        // Test
        Orders createdOrder = customerController.createOrder(1L, order);
        
        // Assertions
        assertEquals(order, createdOrder);
    }

    @Test
    void testCreateOrder_CustomerNotFound() {
        // Mock customer not found
        when(customerService.getCustomerById(anyLong())).thenReturn(Optional.empty());

        // Test and assertion
        assertThrows(RuntimeException.class, () -> customerController.createOrder(1L, new Orders()));
    }
    
	@Test
	void testUpdateOrder_OrderFound() {
	    // Mock existing order
	    Orders existingOrder = new Orders(); // create an existing order
	    when(ordersRepository.findById(anyLong())).thenReturn(Optional.of(existingOrder));
	
	    // Mock updated order
	    Orders updatedOrder = new Orders(); // create an updated order
	    updatedOrder.setWine(new Wines()); // set wine in the updated order
	    updatedOrder.setQuantity(10); // set quantity in the updated order
	
	    // Mock service method
	    when(customerService.saveOrder(any(Orders.class))).thenReturn(updatedOrder);
	
	    // Test
	    Orders result = customerController.updateOrder(1L, updatedOrder);
	
	    // Assertion
	    assertEquals(updatedOrder, result);
	}
	
	@Test
	void testUpdateOrder_OrderNotFound() {
	    // Mock order not found
	    when(ordersRepository.findById(anyLong())).thenReturn(Optional.empty());
	
	    // Test and assertion
	    assertThrows(RuntimeException.class, () -> customerController.updateOrder(1L, new Orders()));
	}
	@Test
	void testDeleteOrder() {
	    // Test and assertion
	    assertDoesNotThrow(() -> customerController.deleteOrder(1L));
	}

    

}