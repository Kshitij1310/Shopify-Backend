package repository;

import model.CartRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<CartRecord, String> {
    List<CartRecord> findByUid(String uid);
}
