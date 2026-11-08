package com.satyam._5springbatchprocessing.config;

import com.satyam._5springbatchprocessing.dao.ICustomerRepo;
import com.satyam._5springbatchprocessing.model.Customer;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;

import org.springframework.batch.infrastructure.item.data.RepositoryItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.LineMapper;
import org.springframework.batch.infrastructure.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.infrastructure.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    @Autowired
    private ICustomerRepo repo;

    @Autowired
    private JobRepository jobRepo;

    @Autowired
    private PlatformTransactionManager transactionManager;


    // Item Reader
  //  @Bean
//    public FlatFileItemReader<Customer> itemReader() {
//
//        FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();
//
//        reader.setResource(new FileSystemResource("src/main/resources/customer_data.csv"));
//        reader.setName("csv-reader");
//        reader.setLinesToSkip(1);
//        reader.setLineMapper(lineMapper());
//
//        return reader;
//    }

    @Bean
    public FlatFileItemReader<Customer> itemReader() {

        FlatFileItemReader<Customer> reader =
                new FlatFileItemReader<Customer>(
                        new FileSystemResource("src/main/resources/customer_data1.csv"),
                        lineMapper()
                );

        reader.setName("csv-reader");
        reader.setLinesToSkip(1);

        return reader;
    }


    // Line Mapper
    private LineMapper<Customer> lineMapper() {

        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();

        tokenizer.setDelimiter(",");
        tokenizer.setStrict(false);

        tokenizer.setNames(
                "customerId",
                "firstName",
                "lastName",
                "email",
                "phoneNo",
                "address",
                "city",
                "state",
                "zipcode",
                "country"
        );

        BeanWrapperFieldSetMapper<Customer> beanWrapper =
                new BeanWrapperFieldSetMapper<>();

        beanWrapper.setTargetType(Customer.class);

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(beanWrapper);

        return lineMapper;
    }


    // Item Processor
    @Bean
    public CustomerProcessor processCxData() {
        return new CustomerProcessor();
    }


    // Item Writer
//    @Bean
//    public RepositoryItemWriter<Customer> itemWriter() {
//
//        RepositoryItemWriter<Customer> writer = new RepositoryItemWriter<>();
//
//        writer.setRepository(repo);
//        writer.setMethodName("save");
//        return writer;
//    }
//

    @Bean
    public RepositoryItemWriter<Customer> itemWriter() {

        RepositoryItemWriter<Customer> writer =
                new RepositoryItemWriter<Customer>(repo);

        writer.setMethodName("save");

        return writer;
    }

    // Step

    @Bean
    public Step step() {

        return new StepBuilder("step-1", jobRepo)
                .<Customer, Customer>chunk(100)
                .transactionManager(transactionManager)
                .reader(itemReader())
                .processor(processCxData())
                .writer(itemWriter())
                .build();
    }

    // Job
    @Bean
    public Job job() {

        return new JobBuilder("customer-import", jobRepo)
                .start(step())
                .build();
    }

}
