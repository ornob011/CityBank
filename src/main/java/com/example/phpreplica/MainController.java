package com.example.phpreplica;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private RequestService requestService;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/process")
    public String process(Model model) {
        ObjectMapper objectMapper = new ObjectMapper();

        // First service call
        String payloadToken = "{\"password\": \"123456Aa\",\"userName\": \"test\"}";
        String serviceUrlToken = "https://sandbox.thecitybank.com:7788/transaction/token";
        String responseToken = requestService.sendPostRequest(serviceUrlToken, payloadToken);
        Map<String, Object> parsedResponseToken;

        try {
            parsedResponseToken = objectMapper.readValue(responseToken, Map.class);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            System.out.println("Error2: " + e.getMessage());
            return "error";
        }

        String transactionId = (String) parsedResponseToken.get("transactionId");

        // Second service call
        String payloadEcomm = "{\"merchantId\": \"11122333\", \"amount\": \"100\", \"currency\": \"050\", \"description\": \"Reference_Number\", \"approveUrl\": \"http://192.168.220.43:8080/CityBankPHP_1.0.1/Approved.php\", \"cancelUrl\": \"http://192.168.220.43:8080/CityBankPHP_1.0.1/Cancelled.php\", \"declineUrl\": \"http://192.168.220.43:8080/CityBankPHP_1.0.1/Declined.php\", \"userName\": \"test\", \"passWord\": \"123456Aa\", \"secureToken\": \"" + transactionId + "\"}";

        String serviceUrlEcomm = "https://sandbox.thecitybank.com:7788/transaction/createorder";
        String responseEcomm = requestService.sendPostRequest(serviceUrlEcomm, payloadEcomm);
        Map<String, Object> parsedResponseEcomm;

        try {
            parsedResponseEcomm = objectMapper.readValue(responseEcomm, Map.class);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            System.out.println("Error3: " + e.getMessage());
            return "error";
        }

        Map<String, Object> items = (Map<String, Object>) parsedResponseEcomm.get("items");
        String URL = (String) items.get("url");
        String orderId = (String) items.get("orderId");
        String sessionId = (String) items.get("sessionId");

        String redirectUrl = URL + "?ORDERID=" + orderId + "&SESSIONID=" + sessionId;

        model.addAttribute("redirectUrl", redirectUrl);

        return "redirect";
    }
}