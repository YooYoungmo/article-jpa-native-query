package ymyoo.article.jpa.dto;

public class ProductOrderedMemberDTO {
    private Long productId;
    private String productName;
    private int price;
    private int stockAmount;
    private Long orderId;
    private Long memberId;
    private String memberName;

    public ProductOrderedMemberDTO(Long productId, String productName, int price, int stockAmount, Long orderId, Long memberId, String memberName) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.stockAmount = stockAmount;
        this.orderId = orderId;
        this.memberId = memberId;
        this.memberName = memberName;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public int getStockAmount() {
        return stockAmount;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }
}
