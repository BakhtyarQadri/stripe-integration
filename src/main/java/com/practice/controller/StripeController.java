package com.practice.controller;

import com.practice.dto.PaymentRequest;
import com.practice.dto.StripeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

@RestController
@CrossOrigin("http://localhost:63342") // to allow frontend call
public class StripeController {

    @PostMapping("/v1/stripe")
    public ResponseEntity<StripeResponse> getStripeSession(
            @RequestBody PaymentRequest reqBody
    ) {
        var params = SessionCreateParams.builder()
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.ALIPAY)
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:8080/checkout/payment/success")
                        .setCancelUrl("http://localhost:8080/checkout/payment/error")
                        .addLineItem(
                            SessionCreateParams.LineItem.builder()
                            .setQuantity(reqBody.quantity())
                            .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(reqBody.currency())
                                .setUnitAmount(reqBody.amount()) // $1 = 100 cents
                                .setProductData(
                                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                    .setName(reqBody.name())
                                    .build()
                                )
                                .build()
                            )
                            .build()
                        )
                        .build();

        try {
            Session stripeSession = Session.create(params);
            return ResponseEntity.ok(
                    new StripeResponse("SUCCESS", "stripe payment session created", stripeSession.getId(), stripeSession.getUrl())
            );
        } catch (StripeException e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(
                    new StripeResponse("ERROR", e.getMessage(), null, null)
            );
        }
    }

    @GetMapping("/success")
    public String success(){
        return "success";
    }

    @GetMapping("/error")
    public String error(){
        return "error occurred";
    }

//    @PostMapping("/v2/stripe")
//    public Map<String, Object> stripePayment2(@RequestParam Long amount, @RequestParam String currency) throws StripeException {
//        Stripe.apiKey = secretKey;
//        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
//                .setAmount(amount * 100)  // Convert to the smallest currency unit
//                .setCurrency(currency)
//                .build();
//
//        PaymentIntent paymentIntent = PaymentIntent.create(params);
//
//        // Extract only relevant fields
//        Map<String, Object> response = new HashMap<>();
//        response.put("id", paymentIntent.getId());
//        response.put("status", paymentIntent.getStatus());
//        response.put("clientSecret", paymentIntent.getClientSecret());
//        response.put("amount", paymentIntent.getAmount());
//        response.put("currency", paymentIntent.getCurrency());
//
//        return response;
//    }

}
