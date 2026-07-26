package controller;

import dto.SellerDashboardSnapshot;
import service.SellerDashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seller")
public class SellerController {

    private final SellerDashboardService sellerDashboardService;

    public SellerController(SellerDashboardService sellerDashboardService) {
        this.sellerDashboardService = sellerDashboardService;
    }

    @GetMapping("/{sellerId}/dashboard")
    public ResponseEntity<SellerDashboardSnapshot> getDashboard(@PathVariable String sellerId) {
        return ResponseEntity.ok(sellerDashboardService.generateDashboardSnapshot(sellerId));
    }
}
