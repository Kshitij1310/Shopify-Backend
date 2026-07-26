package repository;

import model.OrderRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderRecord, UUID> {
    List<OrderRecord> findByUid(String uid);
}
