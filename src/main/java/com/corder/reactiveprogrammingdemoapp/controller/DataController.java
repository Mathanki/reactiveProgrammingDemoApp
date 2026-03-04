package com.corder.reactiveprogrammingdemoapp.controller;

import com.corder.reactiveprogrammingdemoapp.model.Customer;
import com.corder.reactiveprogrammingdemoapp.model.Order;
import com.corder.reactiveprogrammingdemoapp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class DataController {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @Autowired
    private CustomerService customerService;

    @PostMapping("/customer/create")
    public Mono<Customer> createCustomer(@RequestBody Customer customer) {
        return reactiveMongoTemplate.save(customer);
    }

    @GetMapping("/customer/find-by-id")
    public Mono<Customer> findCustomerById(@RequestParam("customerId") String customerId) {
        return customerService.getCustomerById(customerId);
    }

    @PostMapping("/order/create")
    public Mono<Order> createOrder(@RequestBody Order order) {
        return reactiveMongoTemplate.save(order);
    }

    @GetMapping("/sales/summary")
    public Mono<Map<String, Double>> calculateSummary() {
        return reactiveMongoTemplate.findAll(Customer.class)
                .flatMap(customer -> Mono.zip(Mono.just(customer), customerService.calculateOrderSum(customer.getId())))
                .collectMap(tuple2 -> tuple2.getT1().getName(), tuple2 -> tuple2.getT2());
    }


}
