package com.cia103g5.user.order.model;

import java.sql.Timestamp;

import com.cia103g5.user.ft.model.FtVO;
import com.cia103g5.user.member.model.MemberVO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name="orders")
public class OrdersVO {
	
	@Id
	@Column(name="order_no")
	@GeneratedValue(strategy =GenerationType.IDENTITY)//自增
	private Integer orderNo;
	
	
	@ManyToOne
	@JoinColumn(name="mem_id",referencedColumnName = "mem_id",nullable = false)
	private MemberVO memId;
	
	@ManyToOne
	@JoinColumn(name="ft_Id",referencedColumnName = "ft_id",nullable = false)
	private FtVO ftId;
	
		
	@Column(name="order_amount")
	private Integer orderAmount;
	
	@Column(name="real_amount")
	private Integer realAmount;
	
	@Column(name="point_use")
	private Integer pointUse;
		
	@Column(name="note")
	private String note;
	
	@Column(name="created_time",updatable = false)
	private Timestamp createdTime;
	
	//0:已成立 1:已取消 2:申請退貨 3:已結案
	@Column(name="order_state",nullable=false)
	private Byte orderState;
	
	
	//0:信用卡 1:ATM轉帳 2:超商代碼 3:貨到付款 
	@Column(name="payment",nullable=false)
	private Byte payment;
	
	//0:待出貨 1:運送中 2:已送達 (3:待出貨->被取消) (4:已送達->申請退貨) (5:已送達->送出完成)
	@Column(name="ship_status",nullable=false)
	private Byte shipStatus;
	
	@Column(name="ended_time",updatable = false)
	private Timestamp endedTime;

	public OrdersVO() {
		super();
	}

	
	
	public OrdersVO(Integer orderNo, MemberVO memId, FtVO ftId, Integer orderAmount, Integer realAmount,
			Integer pointUse, String note, Timestamp createdTime, Byte orderState, Byte payment, Byte shipStatus,
			Timestamp endedTime) {
		super();
		this.orderNo = orderNo;
		this.memId = memId;
		this.ftId = ftId;
		this.orderAmount = orderAmount;
		this.realAmount = realAmount;
		this.pointUse = pointUse;
		this.note = note;
		this.createdTime = createdTime;
		this.orderState = orderState;
		this.payment = payment;
		this.shipStatus = shipStatus;
		this.endedTime = endedTime;
	}



	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public MemberVO getMemId() {
		return memId;
	}

	public void setMemId(MemberVO memId) {
		this.memId = memId;
	}

	public FtVO getFtId() {
		return ftId;
	}

	public void setFtId(FtVO ftId) {
		this.ftId = ftId;
	}

	public Integer getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(Integer orderAmount) {
		this.orderAmount = orderAmount;
	}

	public Integer getRealAmount() {
		return realAmount;
	}

	public void setRealAmount(Integer realAmount) {
		this.realAmount = realAmount;
	}

	public Integer getPointUse() {
		return pointUse;
	}

	public void setPointUse(Integer pointUse) {
		this.pointUse = pointUse;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public Byte getOrderState() {
		return orderState;
	}

	public void setOrderState(Byte orderState) {
		this.orderState = orderState;
	}

	public Byte getPayment() {
		return payment;
	}

	public void setPayment(Byte payment) {
		this.payment = payment;
	}

	public Byte getShipStatus() {
		return shipStatus;
	}

	public void setShipStatus(Byte shipStatus) {
		this.shipStatus = shipStatus;
	}

	public Timestamp getEndedTime() {
		return endedTime;
	}

	public void setEndedTime(Timestamp endedTime) {
		this.endedTime = endedTime;
	}

	
	
	
	
	
}
