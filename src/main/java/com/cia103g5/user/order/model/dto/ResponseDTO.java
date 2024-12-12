package com.cia103g5.user.order.model.dto;

import java.util.List;


//包裝兩個物件傳到前端
public class ResponseDTO<T,U> {

//	private OrdersVO order;
//	private List<OrderDetailVO> details;
	
	private T Object; //可存入任一種一型態的物件
	private List<U> list; //可以存入任一種型態的集合

	
	
	public ResponseDTO() {
		super();
	}



	public ResponseDTO(T object, List<U> list) {
		super();
		Object = object;
		this.list = list;
	}



	public T getObject() {
		return Object;
	}



	public void setObject(T object) {
		Object = object;
	}



	public List<U> getList() {
		return list;
	}



	public void setList(List<U> list) {
		this.list = list;
	}

	

//	public ResponseDTO(OrdersVO order, List<OrderDetailVO> details) {
//		super();
//		this.order = order;
//		this.details = details;
//	}



//	public OrdersVO getOrder() {
//		return order;
//	}
//
//
//
//	public void setOrder(OrdersVO order) {
//		this.order = order;
//	}
//
//
//
//	public List<OrderDetailVO> getDetails() {
//		return details;
//	}
//
//
//
//	public void setDetails(List<OrderDetailVO> details) {
//		this.details = details;
//	} 
	
	
	
	
	
	
}
