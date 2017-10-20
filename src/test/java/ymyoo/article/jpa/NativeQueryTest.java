package ymyoo.article.jpa;

import org.junit.Assert;
import org.junit.Test;
import org.qlrm.mapper.JpaResultMapper;
import ymyoo.article.jpa.dto.ProductOrderedMemberDTO;
import ymyoo.article.jpa.entitiy.Member;
import ymyoo.article.jpa.entitiy.Order;
import ymyoo.article.jpa.entitiy.Product;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;


public class NativeQueryTest {
    final String PERSISTENCE_UNIT_NAME = "jpa";
    EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);

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

    @Test
    public void test_상품별_구매한_회원_목록_조회_by_ResultSetMappingg() {
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

        int index = 0;

        Assert.assertEquals(1l, products.get(index).getProductId().longValue());
        Assert.assertEquals("Headphones", products.get(index).getProductName());
        Assert.assertEquals(1l, products.get(index).getMemberId().longValue());

        index++;
        Assert.assertEquals(2l, products.get(index).getProductId().longValue());
        Assert.assertEquals("Earphones", products.get(index).getProductName());
        Assert.assertEquals(1l, products.get(index).getMemberId().longValue());

        index++;
        Assert.assertEquals(3l, products.get(index).getProductId().longValue());
        Assert.assertEquals("Watch", products.get(index).getProductName());
        Assert.assertEquals(2l, products.get(index).getMemberId().longValue());

        index++;
        Assert.assertEquals(4l, products.get(index).getProductId().longValue());
        Assert.assertEquals("Pad", products.get(index).getProductName());
        Assert.assertEquals(2l, products.get(index).getMemberId().longValue());

        index++;
        Assert.assertEquals(5l, products.get(index).getProductId().longValue());
        Assert.assertEquals("Pad2", products.get(index).getProductName());
        Assert.assertNull(products.get(index).getMemberId());

        em.close();
    }

    @Test
    public void test_상품별_구매한_회원_목록_조회_by_ObjectArray() {
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

        Query nativeQuery = em.createNativeQuery(sql);

        List<Object[]> resultList = nativeQuery.getResultList();

        List<ProductOrderedMemberDTO> products = resultList.stream().map(product -> new ProductOrderedMemberDTO(
                ((BigInteger)product[0]),
                (String) product[1],
                (Integer) product[2],
                (Integer) product[3],
                ((BigInteger)product[4]),
                ((BigInteger)product[5]),
                (String) product[6])).collect(Collectors.toList());

        // then
        Assert.assertEquals(5, products.size());

        int index = 0;

        Assert.assertEquals(1l, products.get(index).getProductId().longValue());
        Assert.assertEquals("Headphones", products.get(index).getProductName());
        Assert.assertEquals(1l, products.get(index).getMemberId().longValue());

        index++;
        Assert.assertEquals(2l, products.get(index).getProductId().longValue());
        Assert.assertEquals("Earphones", products.get(index).getProductName());
        Assert.assertEquals(1l, products.get(index).getMemberId().longValue());

        index++;
        Assert.assertEquals(3l, products.get(index).getProductId().longValue());
        Assert.assertEquals("Watch", products.get(index).getProductName());
        Assert.assertEquals(2l, products.get(index).getMemberId().longValue());

        index++;
        Assert.assertEquals(4l, products.get(index).getProductId().longValue());
        Assert.assertEquals("Pad", products.get(index).getProductName());
        Assert.assertEquals(2l, products.get(index).getMemberId().longValue());

        index++;
        Assert.assertEquals(5l, products.get(index).getProductId().longValue());
        Assert.assertEquals("Pad2", products.get(index).getProductName());
        Assert.assertNull(products.get(index).getMemberId());

        em.close();
    }

    @Test
    public void test_상품별_구매한_회원_목록_조회_by_QLRM() {
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

        Query nativeQuery = em.createNativeQuery(sql);

        JpaResultMapper jpaResultMapper = new JpaResultMapper();
        List<ProductOrderedMemberDTO> products = jpaResultMapper.list(nativeQuery, ProductOrderedMemberDTO.class);

        // then
        Assert.assertEquals(5, products.size());

        int index = 0;

        Assert.assertEquals(1l, products.get(index).getProductId().longValue());
        Assert.assertEquals("Headphones", products.get(index).getProductName());
        Assert.assertEquals(1l, products.get(index).getMemberId().longValue());

        index++;
        Assert.assertEquals(2l, products.get(index).getProductId().longValue());
        Assert.assertEquals("Earphones", products.get(index).getProductName());
        Assert.assertEquals(1l, products.get(index).getMemberId().longValue());

        index++;
        Assert.assertEquals(3l, products.get(index).getProductId().longValue());
        Assert.assertEquals("Watch", products.get(index).getProductName());
        Assert.assertEquals(2l, products.get(index).getMemberId().longValue());

        index++;
        Assert.assertEquals(4l, products.get(index).getProductId().longValue());
        Assert.assertEquals("Pad", products.get(index).getProductName());
        Assert.assertEquals(2l, products.get(index).getMemberId().longValue());

        index++;
        Assert.assertEquals(5l, products.get(index).getProductId().longValue());
        Assert.assertEquals("Pad2", products.get(index).getProductName());
        Assert.assertNull(products.get(index).getMemberId());

        em.close();
    }
}
