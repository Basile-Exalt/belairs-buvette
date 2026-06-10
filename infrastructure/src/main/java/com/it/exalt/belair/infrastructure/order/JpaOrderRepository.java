package com.it.exalt.belair.infrastructure.order;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JpaOrderRepository extends JpaRepository<OrderEntity, String> {
    List<OrderEntity> findByFestivalierIdAndStatus(String festivalierId, String status);
}
