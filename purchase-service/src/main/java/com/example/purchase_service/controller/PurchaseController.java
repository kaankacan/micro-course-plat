package com.example.purchase_service.controller;

import com.example.purchase_service.dto.PurchaseRequest;
import com.example.purchase_service.dto.PurchaseResponse;
import com.example.purchase_service.service.PurchaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;

    public PurchaseController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @PostMapping
    public ResponseEntity<Void> createPurchase(@RequestHeader("Authorization") String authorizationHeader,
                                               @RequestBody PurchaseRequest purchaseRequest) {
        purchaseService.savePurchase(purchaseRequest, authorizationHeader);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<List<PurchaseResponse>> getMyPurchases(@RequestHeader("Authorization") String authorizationHeader) {
        List<PurchaseResponse> purchases = purchaseService.getPurchasesByToken(authorizationHeader);
        return ResponseEntity.ok(purchases);
    }
}
