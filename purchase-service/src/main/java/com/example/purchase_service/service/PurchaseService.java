package com.example.purchase_service.service;

import com.example.purchase_service.client.CourseClient;
import com.example.purchase_service.dto.CourseResponse;
import com.example.purchase_service.dto.PurchaseRequest;
import com.example.purchase_service.dto.PurchaseResponse;
import com.example.purchase_service.entity.Purchase;
import com.example.purchase_service.kafka.PurchaseProducer;
import com.example.purchase_service.kafka.event.PurchaseCreatedEvent;
import com.example.purchase_service.repository.PurchaseRepository;
import com.example.purchase_service.util.JwtUtil;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseService {

    private static final Logger logger = LoggerFactory.getLogger(PurchaseService.class);

    private final PurchaseRepository purchaseRepository;
    private final CourseClient courseClient;
    private final PurchaseProducer purchaseProducer;
    private final JwtUtil jwtUtil;

    public PurchaseService(PurchaseRepository purchaseRepository, CourseClient courseClient, PurchaseProducer purchaseProducer, JwtUtil jwtUtil) {
        this.purchaseRepository = purchaseRepository;
        this.courseClient = courseClient;
        this.purchaseProducer = purchaseProducer;
        this.jwtUtil = jwtUtil;
    }

    public void savePurchase(PurchaseRequest purchaseRequest, String authorizationHeader) {
        logger.info("New purchase started: courseId={}", purchaseRequest.getCourseId());

        String token = jwtUtil.extractToken(authorizationHeader);
        Long userId = jwtUtil.extractUserId(token);

        Purchase purchase = new Purchase();
        purchase.setUserId(userId);
        purchase.setCourseId(purchaseRequest.getCourseId());
        purchase.setPurchaseDate(LocalDateTime.now());

        purchaseRepository.save(purchase);

        PurchaseCreatedEvent event = new PurchaseCreatedEvent(
                purchase.getUserId(),
                purchase.getCourseId(),
                purchase.getPurchaseDate().toString()
        );

        purchaseProducer.sendPurchaseCreatedEvent(event);

        logger.info("Purchase successfully saved: userId={}, courseId={}", userId, purchaseRequest.getCourseId());
    }

    public List<PurchaseResponse> getPurchasesByToken(String authorizationHeader) {
        String token = jwtUtil.extractToken(authorizationHeader);
        Long userId = jwtUtil.extractUserId(token);

        logger.info("Listing purchases for userId={}", userId);

        List<Purchase> purchases = purchaseRepository.findByUserId(userId);

        return purchases.stream()
                .map(purchase -> {
                    try {
                        CourseResponse courseResponse = courseClient.getCourseById(purchase.getCourseId());

                        PurchaseResponse response = new PurchaseResponse();
                        response.setPurchaseId(purchase.getId());
                        response.setUserId(purchase.getUserId());
                        response.setCourse(courseResponse);
                        response.setPurchaseDate(purchase.getPurchaseDate());

                        logger.info("Course information added: courseId={}", purchase.getCourseId());

                        return response;

                    } catch (FeignException e) {
                        logger.error("Failed to fetch course information: courseId={}, error={}",
                                purchase.getCourseId(), e.getMessage());
                        throw e;
                    }
                })
                .collect(Collectors.toList());
    }
}
