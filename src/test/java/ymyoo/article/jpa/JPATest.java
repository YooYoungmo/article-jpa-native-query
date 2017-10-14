package ymyoo.article.jpa;

import org.junit.Assert;
import org.junit.Test;
import ymyoo.article.jpa.dto.ProductOrderedMemberDTO;
import ymyoo.article.jpa.entitiy.Member;
import ymyoo.article.jpa.entitiy.Order;
import ymyoo.article.jpa.entitiy.Product;

import javax.persistence.*;
import java.util.List;


public class JPATest {
    final String PERSISTENCE_UNIT_NAME = "jpa";
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

    @Test
    public void test_상품별_구매한_회원_목록_조회_by_NativeQuery() {
        // given
        setUpTestFixture();

        // when
        EntityManager em = emf.createEntityManager();

        String sql = "SELECT \"product\".\"product_id\" AS productId, \"product\".\"name\" AS productName, \"product\".\"price\", \n" +
                "    \"product\".\"stock_amount\" AS stockAmount, \"order\".\"order_id\" AS orderId,\n" +
                "    \"member\".\"member_id\" AS memberId, \"member\".\"name\" AS memberName\n" +
                "FROM \"product\" \n" +
                "    LEFT JOIN \"order\" \n" +
                "        ON \"product\".\"product_id\" = \"order\".\"PRODUCT_ID\"\n" +
                "    LEFT JOIN \"member\" \n" +
                "        ON \"order\".\"MEMBER_ID\" = \"member\".\"member_id\"";

        Query nativeQuery = em.createNativeQuery(sql, "ProductOrderedMemberMapping");
        List<ProductOrderedMemberDTO> products = nativeQuery.getResultList();

        // then
        Assert.assertEquals(5, products.size());
        em.close();

    }

    private void setUpTestFixture() {
        EntityManager em = this.emf.createEntityManager();
        EntityTransaction tx = null;
        try {
            tx = em.getTransaction();
            tx.begin();

            Member member1 = new Member("철수", 10);
            Member member2 = new Member("영희", 20);

            em.persist(member1);
            em.persist(member2);

            Product product1 = new Product("Headphones", 100, 12);
            Product product2 = new Product("Earphones", 200, 42);
            Product product3 = new Product("Watch", 300, 32);
            Product product4 = new Product("Pad", 400, 10);
            Product product5 = new Product("Pad2", 400, 10);

            em.persist(product1);
            em.persist(product2);
            em.persist(product3);
            em.persist(product4);
            em.persist(product5);

            // 철수 주문
            Order order1 = new Order(3, member1, product1);
            Order order2 = new Order(2, member1, product2);

            // 영희 주문
            Order order3 = new Order(1, member2, product3);
            Order order4 = new Order(1, member2, product4);

            em.persist(order1);
            em.persist(order2);
            em.persist(order3);
            em.persist(order4);

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
            throw new RuntimeException(e);
        } finally {
            em.close();
        }
    }
}
