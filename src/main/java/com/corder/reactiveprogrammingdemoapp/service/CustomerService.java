package com.corder.reactiveprogrammingdemoapp.service;

import com.corder.reactiveprogrammingdemoapp.model.Customer;
import com.corder.reactiveprogrammingdemoapp.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    public Mono<Customer> getCustomerById(String customerId) {
        Criteria criteria = Criteria.where("id").is(customerId);
        Query query = Query.query(criteria);
        return reactiveMongoTemplate.findOne(query, Customer.class)
                .log();
    }

    public Mono<Double> calculateOrderSum(String customerId) {
        Criteria criteria = Criteria.where("customerId").is(customerId);
        return reactiveMongoTemplate.find(Query.query(criteria), Order.class)
                .map(order -> order.getTotal())
                .reduce(0d, Double::sum);
    }

}
