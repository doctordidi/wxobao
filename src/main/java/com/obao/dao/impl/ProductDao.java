package com.obao.dao.impl;


import com.obao.dao.IProductDao;
import com.obao.entity.*;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/8.
 */
public class ProductDao implements IProductDao {
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Product> findAll(Integer state) {
        return sessionFactory.getCurrentSession().createQuery("from Product where state= ? ")
                .setParameter(0,state).list();
    }

    @Override
    public Product findById(Integer productId) {
        return (Product) sessionFactory.getCurrentSession().get(Product.class,productId);
    }

    @Override
    public void save(Product product) {
        sessionFactory.getCurrentSession().save(product);

    }

    @Override
    public List<ProductSort> findAllProductSort() {
        return sessionFactory.getCurrentSession().createQuery("from ProductSort").list();
    }

    @Override
    public List<Business> findAllBusinesss() {
        return sessionFactory.getCurrentSession().createQuery("from Business").list();
    }

    @Override
    public List<Object> searchProductList(String content) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select b.name,p.product_id,p.product_img,p.product_name,p.sales,p.new_price from t_product p ");
        sql.append(" left join(select t.name,t.business_id from t_business t ) b on p.business_id = b.business_id ");
        sql.append(" where p.product_name like '%").append(content).append("%' ");
        if("".equals(content.trim())){
            sql.append(" limit 1,10");
        }
        return sessionFactory.getCurrentSession().createSQLQuery(sql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public List<Object> findProtions() {

        String sql;
        sql = "SELECT product_id,product_name,product_img,new_price,sales FROM t_product WHERE promotion = 1";
        return sessionFactory.getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public List<Product> findHotProducts(Integer num) {
        String sql = "SELECT product_id,product_name,product_img,new_price,sales  FROM t_product ORDER BY sales DESC LIMIT 0,"+num;
        return sessionFactory.getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public List<Product> findFootprints(String userId) {
        return sessionFactory.getCurrentSession().createSQLQuery("SELECT p.* FROM t_footprint f\n" +
                "LEFT JOIN(SELECT * FROM t_product) p ON  f.product_id = p.product_id\n" +
                "WHERE f.user_id = ?  ORDER BY  f.add_time DESC  limit 0,10").addEntity(Product.class)
                .setParameter(0,userId)
                .list();
    }

    @Override
    public List<Banner> findBanners() {

        return sessionFactory.getCurrentSession().createQuery("from Banner where show = 1").list();
    }

    @Override
    public Object findDetailById(Integer id) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT p.product_id,p.product_name,p.product_img,p.new_price,p.sales,p.remark,b.* FROM t_product p ");
        sql.append(" left join(select business_id,name from t_business) b on b.business_id = p.business_id ");
        sql.append(" where p.product_id = ").append(id);
        return sessionFactory.getCurrentSession().createSQLQuery(sql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
    }

    @Override
    public List<Object> findFlavor(Integer id) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select f.flavor_id,f.name from t_flavor_and_product p ");
        sql.append(" left join(select * from t_product_flavor ) f on f.flavor_id = p.flavor_id ");
        sql.append(" where p.product_id =").append(id);
        return sessionFactory.getCurrentSession().createSQLQuery(sql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public List<Object> findSize(Integer id) {
        StringBuffer sql = new StringBuffer();
        sql.append(" select p.size_id,s.size,p.price from t_size_and_product p");
        sql.append(" left join (select * from t_product_size) s on s.size_id = p.size_id");
        sql.append(" where p.product_id =").append(id);
        return sessionFactory.getCurrentSession().createSQLQuery(sql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public List<BusinessDomain> findDomains() {
        return sessionFactory.getCurrentSession().createQuery("from BusinessDomain").list();
    }

    @Override
    public ProductSize findBySizeId(Integer id) {
        return (ProductSize) sessionFactory.getCurrentSession().get(ProductSize.class,id);
    }

    @Override
    public Business findAllBusinesss(Integer id) {
        return (Business) sessionFactory.getCurrentSession().createSQLQuery("SELECT b.* FROM t_product p\n" +
                "LEFT JOIN(SELECT * FROM t_business) b ON b.business_id = p.business_id\n" +
                "WHERE p.product_id = ?").addEntity(Business.class).setParameter(0,id).list().get(0);
    }

    @Override
    public List<Collect> isCollect(Integer id, String userId) {
        return sessionFactory.getCurrentSession().createQuery("from Collect c where userId=? and productId=?")
                .setParameter(0,userId)
                .setParameter(1,id).list();
    }

    @Override
    public List<String> findImgsByProductId(Integer id) {
        String sql = "select img from t_product_imgs where product_id ="+id;
        return sessionFactory.getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public List<Map<String, Object>> findProductsByBusinessId(Integer businessId) {
        String sql = "SELECT product_id, product_name,product_img,new_price,sales FROM t_product where business_id ="+businessId+" order BY add_time DESC ";
        return sessionFactory.getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public List<Map<String, Object>> findBusinessProductsSortBySales(Integer businessId) {
        String sql = "SELECT product_id, product_name,product_img,new_price,sales FROM t_product where business_id ="+businessId+" order BY sales DESC ";
        return sessionFactory.getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public Map<String, Object> findProductByProductId(Integer productId) {
        String sql = "SELECT product_id, product_name FROM t_product where product_id = "+productId;
        return (Map<String, Object>) sessionFactory.getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list().get(0);
    }

    @Override
    public <T> void deleteEntity(T obj) {
        sessionFactory.getCurrentSession().delete(obj);
    }

    @Override
    public void deleteProductById(Integer id) {
        String sql = "delete from t_product where product_id =" +id;
        sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
    }

    @Override
    public List<ProductFlavor> findFlavors() {
        String sql = "SELECT flavor_id,name FROM t_product_flavor";
        return sessionFactory.getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public List<ProductSize> findSizes() {
        String sql = "SELECT size_id,size FROM t_product_size";
        return sessionFactory.getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public List<Map<String, Object>> findProductsByState(Integer businessId, Integer state) {
        String sql = "SELECT product_id,product_img,product_name FROM t_product WHERE state="+state+" AND business_id="+businessId;
        return sessionFactory.getCurrentSession().createSQLQuery(sql).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }

    @Override
    public void changeProductState(Integer state, Integer productId) {
        String sql = "UPDATE  t_product SET state ="+state+" WHERE product_id="+productId;
        sessionFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
    }

    @Override
    public List<Map<String, Object>> findEvaluationsByProductId(Integer id) {
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT user.nickname,user.headimgurl,evaluation.content,evaluation.star,evaluation.time,o.classifys FROM t_evaluation evaluation ");
        sql.append(" LEFT JOIN (SELECT order_id,classifys FROM t_order) o  on o.order_id= evaluation.order_id ");
        sql.append(" LEFT JOIN (SELECT headimgurl,user_id,nickname FROM t_user) user on user.user_id = evaluation.user_id ");
        sql.append(" WHERE evaluation.product_id = ").append(id).append(" ORDER BY time DESC ");

        return sessionFactory.getCurrentSession().createSQLQuery(sql.toString()).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
    }
}
