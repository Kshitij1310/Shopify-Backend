package repository;

import model.CheckoutAddressRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckoutAddressRepository extends JpaRepository<CheckoutAddressRecord, String> {
    List<CheckoutAddressRecord> findByUid(String uid);
}
