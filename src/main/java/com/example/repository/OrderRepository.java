package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.example.domain.Item;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.Topping;

/**
 * 注文情報を扱うリポジトリ.
 * 
 * @author yuuki
 *
 */
@Repository
public class OrderRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;
	
	// 自動採番した主キーを含むオブジェクトを返すための記述.
	private SimpleJdbcInsert insert;

	@PostConstruct
	public void init() {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert((JdbcTemplate) template.getJdbcOperations());
		SimpleJdbcInsert withTableName = simpleJdbcInsert.withTableName("orders");
		insert = withTableName.usingGeneratedKeyColumns("id");
	}
	
	
	/** Orderオブジェクトを生成するResultSetExtractor */
	private ResultSetExtractor<List<Order>> ORDER_RESULT_SET_EXTRACTOR = (rs) -> {
		// Orderオブジェクトを入れておく箱を用意
		List<Order> orderList = new ArrayList<>();
		List<OrderItem> orderItemList = null;
		List<OrderTopping> orderToppingList = null;
		
		Integer beforeOrderId = 0;
		Integer beforeOrderItemId = 0;
		
		while (rs.next()) {
			Integer nowOrderId = rs.getInt("order_id");
			if (beforeOrderId != nowOrderId) {
				Order order = new Order();
				order.setId(nowOrderId);
				order.setUserId(rs.getInt("user_id"));
				order.setStatus(rs.getInt("status"));
				order.setTotalPrice(rs.getInt("total_price"));
				order.setOrderDate(rs.getDate("order_date"));
				order.setDestinationName(rs.getString("destination_name"));
				order.setDestinationEmail(rs.getString("destination_email"));
				order.setDestinationZipcode(rs.getString("destination_zipcode"));
				order.setDestinationAddress(rs.getString("destination_address"));
				order.setDestinationTel(rs.getString("destination_tel"));
				order.setDeliveryTime(rs.getTimestamp("delivery_time"));
				order.setPaymentMethod(rs.getInt("payment_method"));
				// orderItemListを更新
				orderItemList = new ArrayList<>();
				order.setOrderItemList(orderItemList); // この段階では空の箱
				// orderをリストに格納
				orderList.add(order);
				// beforeOrderIdをnowOrderIdに更新
				beforeOrderId = nowOrderId;
			}
			
			Integer nowOrderItem = rs.getInt("order_item_id");
			if (beforeOrderItemId != nowOrderItem) {
				// orderItemドメインを作成
				OrderItem orderItem = new OrderItem();
				orderItem.setId(nowOrderItem);
				orderItem.setItemId(rs.getInt("item_id"));
				orderItem.setOrderId(rs.getInt("order_id"));
				orderItem.setQuantity(rs.getInt("quantity"));
				String size = rs.getString("size");
				orderItem.setSize(size.toCharArray()[0]);
				// orderItemに詰めるItemオブジェクトを生成し、OrderItemドメインに詰める
				Item item = new Item();
				item.setId(rs.getInt("item_id"));
				item.setName(rs.getString("item_name"));
				item.setPriceM(rs.getInt("item_price_m"));
				item.setPriceL(rs.getInt("item_price_l"));
				item.setImagePath(rs.getString("image_path"));
				orderItem.setItem(item);
				// orderToppingリストを更新し、セットする
				orderToppingList = new ArrayList<>();
				orderItem.setOrderToppingList(orderToppingList);
				// 作成したorderItemオブジェクトをorderドメインのorderItemListにセット
				orderItemList.add(orderItem);
				// beforeOrderItemIdをnowOrderItemに更新
				beforeOrderItemId = nowOrderItem;
			}
			
			if (rs.getInt("order_toppings_id") != 0) {
				// OrderToppingオブジェクトを作成
				OrderTopping orderTopping = new OrderTopping();
				orderTopping.setId(rs.getInt("order_toppings_id"));
				orderTopping.setToppingId(rs.getInt("topping_id"));
				orderTopping.setOrderItemId(rs.getInt("order_item_id"));
				// OrderToppingに詰めるToppingオブジェクトを生成し、OrderToppingドメインに詰める
				Topping topping = new Topping();
				topping.setId(rs.getInt("topping_id"));
				topping.setName(rs.getString("toppings_name"));
				topping.setPriceM(rs.getInt("topping_price_m"));
				topping.setPriceL(rs.getInt("topping_price_l"));
				orderTopping.setTopping(topping);
				// 作成したorderToppingオブジェクトをorderItemドメインのorderToppingListにセット
				orderToppingList.add(orderTopping);
			}
		}
		return orderList;
	};
	
	
	
	/**
	 * 引数のユーザーIDとステータスで、注文情報を注文ID降順で取得する.
	 * 
	 * @param userId ユーザid
	 * @param status 注文状況
	 * @return 注文情報一覧(該当データなしの場合null)
	 */
	public List<Order> findByUserIdAndStatus (Integer userId, Integer status){
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT o.id AS order_id, o.user_id, o.status, o.total_price, ");
		sql.append("o.order_date, o.destination_name, o.destination_email,");
		sql.append("o.destination_zipcode, o.destination_address, o.destination_tel, o.delivery_time, o.payment_method, ");
		sql.append("oi.id AS order_item_id, oi.item_id,oi.quantity, oi.size,");
		sql.append("i.id AS item_id ,i.name AS item_name, i.price_m AS item_price_m, i.price_l AS item_price_l, i.image_path,");
		sql.append("ot.id AS order_toppings_id, ot.topping_id, ot.order_item_id,");
		sql.append("t.name AS toppings_name, t.price_m AS topping_price_m, t.price_l AS topping_price_l ");
		sql.append("FROM orders o LEFT OUTER JOIN order_items oi ON o.id = oi.order_id ");
		sql.append("LEFT OUTER JOIN order_toppings ot ON oi.id = ot.order_item_id ");
		sql.append("LEFT OUTER JOIN items i ON oi.item_id = i.id ");
		sql.append("LEFT OUTER JOIN toppings t ON ot.topping_id = t.id ");
		sql.append("WHERE o.user_id = :userId AND o.status = :status ORDER BY oi.id ");
		
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId).addValue("status", status);
		
		List<Order> orderList = template.query(sql.toString(), param, ORDER_RESULT_SET_EXTRACTOR);

		if (orderList.size() == 0) {
			return null;
		}

		return orderList;
	}
	
	/**
	 * 主キーを元にDBから削除.
	 * 
	 * @param id 主キー
	 */
	public void deleteById(Integer id) {
		String sql = "DELETE FROM orders WHERE id = :id ";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		template.update(sql, param);
	}
	
	
	/**
	 * DBにorderオブジェクトを追加.
	 * 
	 * @param order orderオブジェクト
	 * @return
	 */
	public Order insertOrder(Order order) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(order);

		if (order.getId() == null) {
			Number key = insert.executeAndReturnKey(param);
			order.setId(key.intValue());
		} else {
			String sql = "INSERT INTO orders "
					+ "(user_id, status, total_price, order_date, destination_name, destination_email, "
					+ "destination_zipcode, destination_address, destination_tel, delivery_time, payment_method) "
					+ "VALUES (:userId, :status, :totalPrice, :orderDate, :destinationName, :destinationEmail, "
					+ ":destinationZipcode, :destinationAddress, :destinationTel, :deliveryTime, :paymentMethod) ";
			template.update(sql, param);
		}
		return order;
	}
	
	
	/**
	 * Orderオブジェクトを更新.
	 * 
	 * @param order orderオブジェクト
	 */
	public void updateOrder(Order order) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(order);
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE orders SET status=:status, total_price=:totalPrice, order_date=:orderDate,");
		sql.append("order_number = :orderNumber || to_char(nextval('order_number_seq'), 'FM0000'),");
		sql.append("destination_name=:destinationName, destination_email=:destinationEmail, destination_zipcode=:destinationZipcode,");
		sql.append("destination_address=:destinationAddress, destination_tel=:destinationTel,  delivery_time=:deliveryTime, payment_method=:paymentMethod ");
		sql.append("WHERE status = 0 AND user_id = :userId;"); 
		template.update(sql.toString(), param);
	}
	
	
	
	/**
	 *  注文番号用のシーケンスをリセットするためのメソッド
	 *   setval('order_number_seq',1,false)が実行したい処理
	 */
//	private void resetSequence() {
//		SqlParameterSource param = new MapSqlParameterSource();
//		StringBuilder sql = new StringBuilder();
//		sql.append("SELECT count(*), setval('order_number_seq',1,false) FROM orders");
//		template.queryForObject(sql.toString(), param, Integer.class);
//	}
	
	
	/**
	 * Orderテーブルのuser_idをログインユーザのものに更新する
	 * 
	 * @param order ログイン前のショッピングカートの中身
	 */
	public void updateUserId(Integer userId) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE orders SET user_id = :userId WHERE status = 0 ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
		template.update(sql.toString(), param);
	}
	
	/**
	 * Orderを削除し、OrderItemのorder_idを更新する. ログイン前のカートの中身をログイン後カートに反映させるためのメソッド
	 * 
	 * @param id 主キー
	 */
	public void deleteOrderAndUpdateOrderItem(Integer id, Integer userId) {
		String sql = "WITH deleted AS (DELETE FROM orders WHERE id = :id RETURNING id) "
				+ "UPDATE order_items SET order_id = (SELECT id FROM orders WHERE user_id = :userId AND status = 0) "
				+ "WHERE order_id IN (SELECT id FROM deleted) ;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id).addValue("userId", userId);
		template.update(sql, param);
	}
	
	
}
