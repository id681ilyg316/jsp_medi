package dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import model.Goods;
import model.OrderItem;
import utils.DBUtil;

public class OrderItemDao {
	public OrderItem getOrderItemById(int id) throws SQLException {
		QueryRunner r = new QueryRunner(DBUtil.getDataSource());
		String sql = "select * from orderitem o,goods g where o.id = ? and g.id=o.goods_id";
		return r.query(sql, new BeanHandler<OrderItem>(OrderItem.class),id);
	}
	public static void main(String[] args) throws SQLException {
		OrderItemDao orderItemDao = new OrderItemDao();
		OrderItem orderItem = orderItemDao.getOrderItemById(55);
		System.out.println(orderItem.getGoods_id());
	}
}
