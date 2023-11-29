package servlet;

import model.*;
import org.apache.commons.beanutils.BeanUtils;

import dao.OrderItemDao;
import service.GoodsService;
import service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.Date;

@WebServlet(name = "order_confirm",urlPatterns = "/order_confirm")
public class OrderConfirmServlet extends HttpServlet {
	private OrderService oService = new OrderService();
	private GoodsService goodsService = new GoodsService();
	private OrderItemDao orderItemDao = new OrderItemDao();
     protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Order o = (Order) request.getSession().getAttribute("order");
        try {
            BeanUtils.copyProperties(o, request.getParameterMap());
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        o.setDatetime(new Date());
        o.setStatus(2);
        o.setUser((User) request.getSession().getAttribute("user"));
        oService.addOrder(o);
        request.getSession().removeAttribute("order");
        Page p = oService.getOrderPage(0, 1);
        Order order = (Order) p.getList().get(0);
         for(OrderItem orderItem:order.getItemList()) {
        	Goods goods = null;
			try {
				goods = goodsService.getGoodsById(orderItemDao.getOrderItemById(orderItem.getId()).getGoods_id());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	int shengyu = goods.getStock() - orderItem.getAmount();
        	if(shengyu<0) {
        		  request.setAttribute("msg", goods.getName()+"库存不足！支付失败！");
        	        request.getRequestDispatcher("/order_success.jsp").forward(request, response);
        	        return;
        	}
        	
        }
        for(OrderItem orderItem:order.getItemList()) {
        	Goods goods = null;
			try {
				goods = goodsService.getGoodsById(orderItemDao.getOrderItemById(orderItem.getId()).getGoods_id());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	int shengyu = goods.getStock() - orderItem.getAmount();
        	goods.setStock(shengyu);
        	goodsService.update(goods);
        }
        request.setAttribute("msg", "订单支付成功！");
        request.getRequestDispatcher("/order_success.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
