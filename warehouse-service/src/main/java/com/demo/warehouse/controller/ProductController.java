package com.demo.warehouse.controller;
import com.demo.warehouse.dto.StockUpdateRequest;
import com.demo.warehouse.entity.Product;
import com.demo.warehouse.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.nio.charset.StandardCharsets;
import org.springframework.http.MediaType;

@RestController @RequestMapping("/api")
public class ProductController {
    private final ProductService service;
    public ProductController(ProductService service){this.service=service;}
    @GetMapping({"/products","/inventory"})
    public List<Product> all(){return service.findAll();}
    @GetMapping("/products/{id}")
    public Product byId(@PathVariable Long id){return service.findById(id);}
    @GetMapping("/products/sku/{sku}")
    public Product bySku(@PathVariable String sku){return service.findBySku(sku);}
    @PostMapping("/products")
    public ResponseEntity<Product> create(@RequestBody Product p){return ResponseEntity.status(HttpStatus.CREATED).body(service.save(p));}
    @PutMapping({"/products/{id}","/inventory/{id}"})
    public Product update(@PathVariable Long id,@RequestBody Product p){p.setId(id);return service.save(p);}
    @PatchMapping("/products/{id}/stock")
    public Product stock(@PathVariable Long id,@Valid @RequestBody StockUpdateRequest r){
        Product p=service.findById(id); p.setQuantity(r.quantity()); return service.save(p);
    }
    @PutMapping("/inventory/{sku}/quantity")
    public Product stockBySku(@PathVariable String sku,@Valid @RequestBody StockUpdateRequest r){return service.updateStock(sku,r.quantity());}
    @GetMapping({"/inventory/low-stock","/products/low-stock"})
    public List<Product> lowStock(){return service.lowStock();}
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){service.delete(id);return ResponseEntity.noContent().build();}

    @GetMapping(value={"/v1/warehouse/export/csv","/warehouse/export/csv","/products/export/csv"},
                produces="text/csv")
    public ResponseEntity<byte[]> exportCsv(){
        StringBuilder csv=new StringBuilder("id,sku,name,quantity,reorderLevel,price\n");
        for(Product p:service.findAll()){
            csv.append(p.getId()).append(',')
               .append(p.getSku()).append(',')
               .append(p.getName().replace(",", " ").replace("\n"," "))
               .append(',').append(p.getQuantity()).append(',')
               .append(p.getReorderLevel()).append(',').append(p.getPrice()).append('\n');
        }
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=warehouse-products.csv")
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(csv.toString().getBytes(StandardCharsets.UTF_8));
    }

    @GetMapping("/v1/warehouse/report")
    public java.util.Map<String,Object> report(){
        java.util.Map<String,Object> report=new java.util.LinkedHashMap<>();
        report.put("asOf",java.time.Instant.now());
        report.put("totalProducts",service.findAll().size());
        report.put("lowStock",service.lowStock());
        report.put("products",service.findAll());
        return report;
    }
}
