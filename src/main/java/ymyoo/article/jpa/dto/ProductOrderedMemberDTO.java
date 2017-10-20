package ymyoo.article.jpa.dto;

import java.math.BigInteger;

public class ProductOrderedMemberDTO {
    private Long productId;
    private String productName;
    private int price;
    private int stockAmount;
    private Long orderId;
    private Long memberId;
    private String memberName;

    public ProductOrderedMemberDTO(BigInteger productId, String productName, Integer price, Integer stockAmount,
                                   BigInteger orderId, BigInteger memberId, String memberName) {
        this.productId = productId == null ? null : productId.longValue();
        this.productName = productName;
        this.price = price;
        this.stockAmount = stockAmount;
        this.orderId = orderId == null ? null : orderId.longValue();
        this.memberId = memberId == null ? null : memberId.longValue();
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
