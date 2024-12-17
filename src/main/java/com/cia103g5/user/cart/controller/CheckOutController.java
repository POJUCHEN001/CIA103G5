package com.cia103g5.user.cart.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cia103g5.user.cart.model.CartServiceImpl;
import com.cia103g5.user.cart.model.CartVO;
import com.cia103g5.user.ft.model.FtVO;
import com.cia103g5.user.member.model.MemberService;
import com.cia103g5.user.member.model.MemberVO;
import com.cia103g5.user.order.model.OrdersRepository;
import com.cia103g5.user.order.model.OrdersService;
import com.cia103g5.user.order.model.OrdersVO;
import com.cia103g5.user.orderDetails.model.OrderDetailService;
import com.cia103g5.user.orderDetails.model.OrderDetailVO;
import com.cia103g5.user.product.model.ProductService;
import com.cia103g5.user.product.model.ProductVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/checkout")
public class CheckOutController {

    private final CartServiceImpl cartService;
    private final OrdersService ordersService;
    private final OrderDetailService orderDetailService;
    private final MemberService memberService;
    private final ProductService productService;
    
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    public CheckOutController(CartServiceImpl cartService, OrdersService ordersService,
                              OrderDetailService orderDetailService, MemberService memberService,ProductService productService) {
        this.cartService = cartService;
        this.ordersService = ordersService;
        this.orderDetailService = orderDetailService;
        this.memberService = memberService;
		this.productService =  productService;
    }

    // 結帳頁面
    @GetMapping("/{userId}")
    public String checkoutPage(@PathVariable Integer userId, Model model, HttpSession session) {
        session.setAttribute("redirectUrl", "/checkout/" + userId);

        Map<Integer, List<CartVO>> cartItemsByFtId = cartService.getCartItemsGroupedByFtId(userId);
        double totalAmount = cartItemsByFtId.values().stream()
                .flatMap(List::stream)
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        MemberVO member = memberService.findMemberById(userId);
        model.addAttribute("cartItemsByFtId", cartItemsByFtId);
        model.addAttribute("totalAmount", totalAmount);
        model.addAttribute("member", member);

        return "checkout";
    }

    // 提交訂單
    @PostMapping("/confirm/{userId}")
    public String confirmOrder(@PathVariable Integer userId, @RequestParam("pointsUsed") int pointsUsed,
                               @RequestParam("paymentMethod") Byte paymentMethod, Model model) {
        try {
            // 獲取購物車內容
            Map<Integer, List<CartVO>> cartItemsByFtId = cartService.getCartItemsGroupedByFtId(userId);
            MemberVO member = memberService.findMemberById(userId);
            int userPoints = member.getPoints();

            if (pointsUsed > userPoints) {
                model.addAttribute("error", "折抵點數不能超過您的累積點數！");
                return "checkout";
            }

            double totalAmount = cartService.calculateTotalAmount(userId);
            double shippingFee = 60.0;
            double finalAmount = totalAmount - pointsUsed +shippingFee;

            // 獲取新增點數
            int newPointsEarned = (int) ((finalAmount-shippingFee) / 100); // 每100元加1點
            int remainingPoints = userPoints - pointsUsed + newPointsEarned;

            List<OrdersVO> createdOrders = new ArrayList<>();

            // 按商家拆單
            for (Map.Entry<Integer, List<CartVO>> entry : cartItemsByFtId.entrySet()) {
                Integer ftId = entry.getKey();
                List<CartVO> cartItems = entry.getValue();

                int orderAmount = cartItems.stream().mapToInt(item -> (int) (item.getPrice() * item.getQuantity())).sum();
                int realAmount = orderAmount - pointsUsed;

                // 創建訂單

                OrdersVO order = new OrdersVO();
                order.setMemId(member);
                
                FtVO ftVO =order.getFtId();
                ftVO.setFtId(ftId);
                order.setFtId(ftVO); 
                order.setOrderAmount(orderAmount);
                order.setRealAmount(realAmount);
                order.setPointUse(pointsUsed);
                order.setCreatedTime(new Timestamp(System.currentTimeMillis()));
                order.setOrderState((byte) 0);
                order.setPayment(paymentMethod);
                order.setShipStatus((byte) 0);

                OrdersVO savedOrder = ordersRepository.save(order); 
                createdOrders.add(savedOrder);
                
                
                

                // 創建訂單詳情並扣除庫存量
                for (CartVO item : cartItems) {
                    // 更新商品庫存量
                    ProductVO product = productService.getOneProduct(item.getProdNo());
                    if (product != null) {
                        int newQuantity = product.getAvailableQuantity() - item.getQuantity();
                        product.setAvailableQuantity(newQuantity > 0 ? newQuantity : 0);
                        productService.updateProduct(product);
                    }

                    // 創建訂單詳情
                    OrderDetailVO.CompositeDetail compositeKey = new OrderDetailVO.CompositeDetail(savedOrder, null);
                    OrderDetailVO detail = new OrderDetailVO(compositeKey, item.getPrice().intValue(),
                            item.getQuantity(), null, null, (byte) 0, null, null);
                    orderDetailService.addOrder_detail(detail);
                }
            }

            // 更新會員點數
            memberService.updateMemberPoints(userId, remainingPoints);

            // 清空購物車
            cartService.clearCart(userId);

            model.addAttribute("orders", createdOrders);
            model.addAttribute("finalAmount", finalAmount);
            model.addAttribute("newPoints", newPointsEarned);

            return "order-success";

        } catch (Exception e) {
            model.addAttribute("error", "結帳失敗：" + e.getMessage());
            return "checkout";
        }
    }
}
