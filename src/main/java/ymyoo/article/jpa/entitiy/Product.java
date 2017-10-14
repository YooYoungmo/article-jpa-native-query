package ymyoo.article.jpa.entitiy;

import ymyoo.article.jpa.dto.ProductOrderedMemberDTO;

import javax.persistence.*;

@SqlResultSetMapping(
        name="ProductOrderedMemberMapping",
        classes = @ConstructorResult(
                targetClass = ProductOrderedMemberDTO.class,
                columns = {
                        @ColumnResult(name="productId", type = Long.class),
                        @ColumnResult(name="productName", type = String.class),
                        @ColumnResult(name="price", type = Integer.class),
                        @ColumnResult(name="stockAmount", type = Integer.class),
                        @ColumnResult(name="orderId", type = Long.class),
                        @ColumnResult(name="memberId", type = Long.class),
                        @ColumnResult(name="memberName", type = String.class),
                })
)
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private Long id;

    private String name;

    private int price;

    @Column(name = "stock_amount")
    private int stockAmount;

    public Product() {
    }

    public Product(String name, int price, int stockAmount) {
        this.name = name;
        this.price = price;
        this.stockAmount = stockAmount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getStockAmount() {
        return stockAmount;
    }

    public void setStockAmount(int stockAmount) {
        this.stockAmount = stockAmount;
    }
}
