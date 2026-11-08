package com.satyam._5springbatchprocessing.config;

import com.satyam._5springbatchprocessing.model.Customer;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.infrastructure.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<Customer, Customer> {
    @Override
    public @Nullable Customer process(Customer item) throws Exception {

        // Logic To Process Means And NO Filter Here .
        return item;
    }
}
