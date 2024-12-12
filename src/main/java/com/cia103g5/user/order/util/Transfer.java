package com.cia103g5.user.order.util;

import org.springframework.stereotype.Component;

@Component
public class Transfer {

	//轉換付款方式的方法(傳入Byte 回傳String)
	public String transferPayment(Byte payment) {
		
		String paymentString="";
		switch(payment) {
		case 0 :
			paymentString="信用卡";
			break;
		case 1 :
			paymentString="ATM轉帳";
			break;
		case 2:
			paymentString="超商繳費";
			break;
		case 3:
			paymentString="貨到付款";
			break;
			default:
			paymentString="未知付款狀態";
		}
		
		return paymentString;
		
	}
	
	//轉換訂單狀態
	public String transferOrderState(Byte state) {
		String orderStateString="";
		switch(state) {
		case 0 :
			orderStateString="已成立";
			break;
		case 1 :
			orderStateString="已取消";
			break;
		case 2:
			orderStateString="退貨";
			break;
		case 3:
			orderStateString="已完成";
			break;
			default:
			orderStateString="未知訂單狀態";
		}
		
		return orderStateString;
	}
	
	//轉換物流狀態
	public String transferShipStatus(Byte state) {
		String shipStateString ="";
		switch(state) {
		case 0 :
			shipStateString="待出貨";
			break;
		case 1 :
			shipStateString="已出貨";
			break;
		case 2:
			shipStateString="已送達";
			break;
		case 3:
			shipStateString="待出貨";
			break;
		case 4:
			shipStateString="已送達";
			break;
		case 5:
			shipStateString="已送達";
			break;
			default :
			shipStateString="未知物流狀態";
		}
		
		return shipStateString;
	}
	
	
	
}
