package org.course.kafka.service;

import org.course.kafka.models.entity.Product;
import org.course.kafka.models.event.ProductEvent;
import org.course.kafka.repo.ProductRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class ProductService {

    private final ProductRepo productRepo;
    private KafkaTemplate<String, ProductEvent> template;
    private Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    public ProductService(ProductRepo productRepo, KafkaTemplate<String, ProductEvent> template) {
        this.productRepo = productRepo;
        this.template = template;
    }

    public Product createProduct(Product product) {
        productRepo.save(product);

        ProductEvent productEvent = new ProductEvent(product.getId(), product.getPrice(), product.getDescription());

        CompletableFuture<SendResult<String, ProductEvent>> future = template
                .send("product-cancelled-events-topic", productEvent.getId(), productEvent);

        future.whenComplete((result, ex) -> {
          if (ex != null) {
              logger.error(ex.getMessage());
          } else  logger.info("sent " + result.getRecordMetadata());
        });

        logger.info("res " + product.getId());

        return product;
    }

    public Product getProduct(String id) {
        Product product = productRepo.findById(id).orElseThrow(()-> new RuntimeException("id not found"));

        ProductEvent productEvent = new ProductEvent(product.getId(), product.getPrice(), product.getDescription());

        CompletableFuture<SendResult<String, ProductEvent>> future = template
                .send("product-cancelled-events-topic", productEvent.getId(), productEvent);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                logger.error(ex.getMessage());
            } else  logger.info("sent " + result.getRecordMetadata());
        });

        logger.info("res " + product.getId());
        return product;
    }
}
