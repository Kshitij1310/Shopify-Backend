package repository;

import model.WishlistRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WishlistRepository extends JpaRepository<WishlistRecord, String> {
    List<WishlistRecord> findByUid(String uid);
}
