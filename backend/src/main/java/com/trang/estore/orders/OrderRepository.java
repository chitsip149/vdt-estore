package com.trang.estore.orders;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
    @Query("select o from Order o where o.customer.id = :userId")
    List<Object> findAllByCustomer(@Param("userId") Long userId);
}
