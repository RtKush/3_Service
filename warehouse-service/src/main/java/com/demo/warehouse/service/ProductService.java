package com.demo.warehouse.service;
import com.demo.warehouse.entity.Product;
import com.demo.warehouse.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.Instant; import java.util.List;
import java.util.Map;

@Service
public class ProductService {
    private final ProductRepository repository;
    private final NotificationClient notifications;
    private final RabbitTemplate rabbitTemplate;
    public ProductService(ProductRepository repository, NotificationClient notifications,
                           @Autowired(required=false) RabbitTemplate rabbitTemplate) {
        this.repository=repository; this.notifications=notifications; this.rabbitTemplate=rabbitTemplate;
    }
    public List<Product> findAll(){return repository.findAll();}
    public Product findById(Long id){return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found: "+id));}
    public Product findBySku(String sku){return repository.findBySku(sku).orElseThrow(() -> new IllegalArgumentException("Product not found: "+sku));}
    public Product save(Product p){p.setUpdatedAt(Instant.now()); return repository.save(p);}
    @Transactional
    public Product updateStock(String sku, int quantity){
        Product p=findBySku(sku); p.setQuantity(quantity); p.setUpdatedAt(Instant.now()); p=repository.save(p);
        if(quantity<=p.getReorderLevel()) {
            notifications.sendStockNotification(sku,"Low stock: "+quantity);
            if (rabbitTemplate != null) {
                try {
                    rabbitTemplate.convertAndSend("sc360.events","stock.event",
                        Map.of("sku",sku,"message","Low stock: "+quantity,"type","STOCK"));
                } catch (RuntimeException ex) {
                    throw new IllegalStateException("Unable to publish stock.below-reorder event", ex);
                }
            }
        }
        return p;
    }
    public void delete(Long id){repository.deleteById(id);}
    public List<Product> lowStock(){return repository.findAll().stream().filter(p -> p.getQuantity()<=p.getReorderLevel()).toList();}
}
