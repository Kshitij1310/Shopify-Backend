package controller;

import dto.PaymentRequest;
import dto.PaymentResponse;
import service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentsController {

    private final PaymentService paymentService;

    public PaymentsController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/process")
    public ResponseEntity<PaymentResponse> process(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.process(request));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> verify(@PathVariable String paymentId) {
        return ResponseEntity.ok(paymentService.verify(paymentId));
    }
}
