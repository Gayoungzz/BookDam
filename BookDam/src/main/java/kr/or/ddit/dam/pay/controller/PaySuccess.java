package kr.or.ddit.dam.pay.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.ddit.dam.book.service.BookServiceImpl;
import kr.or.ddit.dam.book.service.IBookService;
import kr.or.ddit.dam.cart.dao.CartDaoImpl;
import kr.or.ddit.dam.cart.dao.ICartDao;
import kr.or.ddit.dam.nomem.service.INomemService;
import kr.or.ddit.dam.nomem.service.NomemServiceImple;
import kr.or.ddit.dam.order.dao.IOrderDao;
import kr.or.ddit.dam.order.dao.OrderDaoImpl;
import kr.or.ddit.dam.pay.service.IPayService;
import kr.or.ddit.dam.pay.service.PayServiceImpl;
import kr.or.ddit.dam.vo.BookVO;
import kr.or.ddit.dam.vo.CustVO;
import kr.or.ddit.dam.vo.NoMemVO;
import kr.or.ddit.dam.vo.OrdersDetailVO;
import kr.or.ddit.mybatis.config.MybatisUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class PaySuccess
 */
@WebServlet("/PaySuccess.do")
public class PaySuccess extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public PaySuccess() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	doPost(req, resp);
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Long partnerOrderId = Long.parseLong((String) session.getAttribute("partner_order_id"));
        String partnerUserId = (String) session.getAttribute("partner_user_id");
        String useMileage = (String) session.getAttribute("use_mileage");
        String formData = (String) session.getAttribute("form_data");
        String sendArr = (String) session.getAttribute("sendArr");
        String giftArr = (String) session.getAttribute("giftArr");

        Gson gson = new Gson();
        JsonObject jForm = gson.fromJson(formData, JsonObject.class);
        JsonArray jSendArr = gson.fromJson(sendArr, JsonArray.class);
        JsonArray jGiftArr = giftArr == null || giftArr.equals("null") ? null : gson.fromJson(giftArr, JsonArray.class);

        IPayService payService = PayServiceImpl.getInstance();
        try {
            payService.processPayment(partnerOrderId, partnerUserId, useMileage, jForm, jSendArr, jGiftArr, session);
            response.sendRedirect("order/pay_result_page.jsp");
        } catch(Exception e) {
            e.printStackTrace();
            response.sendRedirect("order/pay_fail_page.jsp");
        }
    }


}
