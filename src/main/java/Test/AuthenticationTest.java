//package Test;
//
//import java.util.Base64;
//
//import com.sun.jersey.api.client.Client;
//import com.sun.jersey.api.client.ClientResponse;
//import com.sun.jersey.api.client.WebResource;
// 
//public class AuthenticationTest {
// 
//    public static void main(String a[]){
//         
//        String url = "http://localhost:8080/";
//        String name = "java2novice";
//        String password = "Simple4u!";
//        String authString = name + ":" + password;
//        
//        String authStringEnc = Base64.getEncoder().encodeToString(authString.getBytes());
//        System.out.println("Base64 encoded auth string: " + authStringEnc);
//        Client restClient = Client.create();
//        WebResource webResource = restClient.resource(url);
//        ClientResponse resp = webResource.accept("application/json")
//                                         .header("Authorization", "Basic " + authStringEnc)
//                                         .get(ClientResponse.class);
//        if(resp.getStatus() != 200){
//            System.err.println("Unable to connect to the server");
//        }
//        String output = resp.getEntity(String.class);
//        System.out.println("response: "+output);
//    }
//}