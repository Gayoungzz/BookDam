package kr.or.ddit.dam.pay.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import jakarta.servlet.http.HttpSession;
import kr.or.ddit.dam.book.service.BookServiceImpl;
import kr.or.ddit.dam.book.service.IBookService;
import kr.or.ddit.dam.cart.dao.CartDaoImpl;
import kr.or.ddit.dam.cart.dao.ICartDao;
import kr.or.ddit.dam.nomem.service.INomemService;
import kr.or.ddit.dam.nomem.service.NomemServiceImple;
import kr.or.ddit.dam.pay.dao.IPayDao;
import kr.or.ddit.dam.pay.dao.PayDaoImpl;
import kr.or.ddit.dam.vo.BookVO;
import kr.or.ddit.dam.vo.CustVO;
import kr.or.ddit.dam.vo.NoMemVO;
import kr.or.ddit.dam.vo.OrdersDetailVO;
import kr.or.ddit.dam.vo.OrdersVO;
import kr.or.ddit.dam.vo.PaymentVO;
import kr.or.ddit.dam.vo.RefundVO;
import kr.or.ddit.mybatis.config.MybatisUtil;

public class PayServiceImpl implements IPayService {
	// 싱글톤
	private static IPayService payService = new PayServiceImpl();

	private static IPayDao payDao;

	// 싱글톤
	private PayServiceImpl() {};
	// 싱글톤
	public static IPayService getInstance() {
		payDao = PayDaoImpl.getInstance();

		return payService;
	}

	/**
	 * 결재 수행
	 */
	@Override
	public void processPayment(Long partnerOrderId, String partnerUserId, String useMileage, JsonObject formData,
			JsonArray sendArr, JsonArray giftArr, HttpSession session) {

		int earnRate = 0;
        String nmemPass = formData.get("nmem_pass") != null && !formData.get("nmem_pass").isJsonNull()
                ? formData.get("nmem_pass").getAsString() : null;

        if(nmemPass == null) {
            String memGrade = getMemGrade(partnerUserId);
            earnRate = switch (memGrade) {
                case "실버" -> 7;
                case "골드" -> 8;
                case "다이아몬드" -> 10;
                default -> 5;
            };
        }

        SqlSession sqlSession = MybatisUtil.getInstance();
        try {
            IBookService bookService = BookServiceImpl.getInstance();
            int totalEarnedMileage = 0;

            // 1. 주문 도서 상세 처리
            for(JsonElement el : sendArr) {
                Long bookNo = el.getAsJsonObject().get("book_no").getAsLong();
                int qty = el.getAsJsonObject().get("cart_qty").getAsInt();
                BookVO book = bookService.getBookDetail(bookNo);
                int earnedMileage = (int)(book.getBook_price() * (earnRate / 100.0));
                totalEarnedMileage += earnedMileage;

                OrdersDetailVO orderDetail = new OrdersDetailVO();
                orderDetail.setOrder_no(partnerOrderId);
                orderDetail.setBook_no(bookNo);
                orderDetail.setOrder_qty(qty);
                orderDetail.setOrder_price(book.getBook_price());
                orderDetail.setEarned_mileage(earnedMileage);

                insertOrderDetail(orderDetail);

                // 재고 차감
                Map<String,Object> paramMap = new HashMap<>();
                paramMap.put("book_no", bookNo);
                paramMap.put("order_qty", qty);
                paramMap.put("type", 0);
                bookService.updateStock(sqlSession, paramMap);
            }

            // 2. 사은품 처리
            if(giftArr != null) {
                for(JsonElement el : giftArr) {
                    int giftNo = el.getAsJsonObject().get("gift_no").getAsInt();
                    Map<String,Object> giftMap = new HashMap<>();
                    giftMap.put("order_no", partnerOrderId);
                    giftMap.put("gift_no", giftNo);
                    insertGiftCheck(sqlSession, giftMap);
                }
            }

            // 3. 배송지 처리
            CustVO deliveryInfo = new CustVO(
                    partnerUserId,
                    formData.get("cust_name").getAsString(),
                    formData.get("cust_zip").getAsString(),
                    formData.get("cust_addr1").getAsString(),
                    formData.get("cust_addr2") != null && !formData.get("cust_addr2").isJsonNull()
                            ? formData.get("cust_addr2").getAsString() : null,
                    formData.get("cust_tel").getAsString(),
                    partnerOrderId
            );
            insertOrderAddr(sqlSession, deliveryInfo);

            // 4. 회원/비회원 처리
            if(nmemPass == null) {
                // 회원 마일리지 적립 및 사용
                Map<String,Object> mileageMap = new HashMap<>();
                mileageMap.put("mem_mail", partnerUserId);
                mileageMap.put("total_mileage", totalEarnedMileage);
                mileageMap.put("mileage_type", "도서구매");
                insertMileage(sqlSession, mileageMap);

                if(Integer.parseInt(useMileage) != 0) {
                    mileageMap = new HashMap<>();
                    mileageMap.put("mem_mail", partnerUserId);
                    mileageMap.put("total_mileage", -Integer.parseInt(useMileage));
                    mileageMap.put("mileage_type", "결제사용");
                    insertMileage(sqlSession, mileageMap);
                }

                // 카트 삭제
                ICartDao cartDao = CartDaoImpl.getInstance();
                for(JsonElement el : sendArr) {
                    Long bookNo = el.getAsJsonObject().get("book_no").getAsLong();
                    Map<String,Object> cartMap = new HashMap<>();
                    cartMap.put("cust_id", partnerUserId);
                    cartMap.put("book_no", bookNo);
                    int cartCnt = cartDao.getCartBook(cartMap);
                    if(cartCnt >= 1) {
                        cartDao.deleteCartDetail(sqlSession, cartMap);
                    }
                }

            } else {
                // 비회원 처리
                NoMemVO vo = new NoMemVO(partnerUserId, nmemPass);
                INomemService nomemService = NomemServiceImple.getNoService();
                nomemService.insertNoMember(sqlSession, vo);
                session.setAttribute("nmloginOk", vo);
            }

            sqlSession.commit();
        } catch(Exception e) {
            sqlSession.rollback();
            throw e;
        } finally {
            sqlSession.close();
        }
    }

	@Override
	public String getMemGrade(String mem_mail) {
		return payDao.getMemGrade(mem_mail);
	}

	@Override
	public String getTid(Long order_no) {
		return payDao.getTid(order_no);
	}

	@Override
	public PaymentVO getPaymentInfo(Long order_no) {
		return payDao.getPaymentInfo(order_no);
	}

	@Override
	public int insertOrderDetail(OrdersDetailVO orderItem) {
		return payDao.insertOrderDetail(orderItem);
	}

	@Override
	public int insertGiftCheck(SqlSession sqlSession, Map<String, Object> giftMap) {
		return payDao.insertGiftCheck(sqlSession, giftMap);
	}

	@Override
	public int insertOrderAddr(SqlSession sqlSession, CustVO delivery_info) {
		return payDao.insertOrderAddr(sqlSession, delivery_info);
	}

	@Override
	public int insertMileage(SqlSession sqlSession, Map<String, Object> mileageMap) {
		return payDao.insertMileage(sqlSession, mileageMap);
	}
	@Override
	public List<Integer> getMileList(Long order_no) {
		return payDao.getMileList(order_no);
	}
	@Override
	public int insertRefund(SqlSession sqlSession, RefundVO rvo) {
		return payDao.insertRefund(sqlSession, rvo);
	}
	// Refund 테이블에 주문번호가 있는지 확인하는 메소드
	@Override
	public int getCheckRefund(OrdersVO odv) {
		return payDao.getCheckRefund(odv);
	}
}
