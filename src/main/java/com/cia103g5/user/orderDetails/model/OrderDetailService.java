package com.cia103g5.user.orderDetails.model;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cia103g5.user.order.model.OrdersRepository;
import com.cia103g5.user.order.model.ReturnInfo;
import com.cia103g5.user.order.model.dto.CommentModalDTO;
import com.cia103g5.user.order.model.dto.ProductCommetDTO;
import com.cia103g5.user.order.model.dto.ScoringDTO;
import com.cia103g5.user.order.model.dto.ShowReturnOrderDTO;
import com.cia103g5.user.orderDetails.model.OrderDetailVO.CompositeDetail;
import com.cia103g5.user.productImage.model.ProductImageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;



@Service("order_detailService")
public class OrderDetailService {
	
	@Autowired
	OrdersRepository orderRepository;

	@Autowired
	OrderDetailRepository repository;

//	@Autowired
//    private SessionFactory sessionFactory;
	
	 @PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
    private ProductImageService productImageService;
	
	public void addOrder_detail(OrderDetailVO orderDetailVO) {
		repository.save(orderDetailVO);
		
	}
	
	public void updateOrder_detail(OrderDetailVO orderDetailVO) {
			repository.save(orderDetailVO);
		
	}
	
	public void deleteOrder_detail(CompositeDetail compositeDetail) {
		if(repository.existsById(compositeDetail)) {
			repository.deleteById(compositeDetail);
		}
	}
	
	public OrderDetailVO getOneOrder_detail(CompositeDetail compositeDetail) {
		Optional<OrderDetailVO> optional =repository.findById(compositeDetail);
//		Order_detailVO order_detail = optional.get();
		return optional.orElse(null);
	}
	
	
	public List<OrderDetailVO> getAll(){
		return repository.findAll();
	}
	
	//查詢不需要貼@transactional，如果是新增、變更、刪除就要貼
	//使用orderNo查詢同筆訂單下所有的訂單明細(返回List存放多個訂單明細物件)
	public List<OrderDetailVO>getAllDetailsByOrderNo(Integer orderNo){
		return repository.findAllDetailsByOrderNo(orderNo);
	}

	
	
	//包裝會員的評價燈箱資訊
	public List<CommentModalDTO> getAllCommentModalInfo(Integer orderNo){
		List<CommentModalDTO> commentList = new ArrayList<CommentModalDTO>();
		List<OrderDetailVO> detailList = repository.findAllDetailsByOrderNo(orderNo);
		
		for(OrderDetailVO VO : detailList) {
			CommentModalDTO DTO = new CommentModalDTO();
			DTO.setProdNo(VO.getCompositekey().getProductVO().getProdNo());
			DTO.setProdName(VO.getCompositekey().getProductVO().getProdName());
			commentList.add(DTO);
		}
		
		return commentList;		
	}


	
	//回傳單一一個訂單的多個評價(一個商品一個評價)->DTO list
	@Transactional
	public List<ScoringDTO> getProductCommentByOrderNo(Integer orderNo){
		
		entityManager.flush(); 
		List<ScoringDTO> list =repository.findScoringInfoByOrderNo(orderNo);
		
//		for(ScoringDTO comment : list) {
//			comment.setPhoto(productImageService.findPrimaryImageByProdNo(comment.getProdNo()).getProdPic());		
//
//		}
		
		return list;
		
	}
	
	//更新單一一個訂單的所有商品評價
	@Transactional
	public Boolean addComment(List<ProductCommetDTO> comments) {
	    for (ProductCommetDTO comment : comments) {
	        int rowsUpdated = repository.addProductComment(
	            comment.getOrderNo(),
	            comment.getProdNo(),
	            comment.getRateContent(),
	            comment.getRateScore()
	        );
	        System.out.println("Rows updated for OrderNo: " + comment.getOrderNo() + ", ProdNo: " + comment.getProdNo() + " -> " + rowsUpdated);
	        
	        if (rowsUpdated == 0) {
	            return false; // 如果更新失敗，立即返回 false
	        }
	    }
	    return true;
	}


	
	//update->回傳型別只可為void或是int(代表成功變動了幾筆資料)
	//指針對訂單明細裡面的退貨狀態、退貨資訊、退貨圖片做更新的方法
	@Transactional
	public Boolean updateReturnInfo(Byte isReturn,
									String returnInfo,
									byte[] returnPhoto,
									Integer orderNo,
									Integer prodNo) {
		if(repository.updateReturnInfo(isReturn, returnInfo, returnPhoto, orderNo, prodNo)!=0) {
			return true;
		}else {
			return false;
		}
		
	}
	
	//查回一筆訂單的所有訂單明細，判斷該明細的is_return是否為1，若為1則取出裡面的return_info
	//使用一個Map<Integer,ReturnInfo>，將該prodno作為key、return_info轉為物件型態，作為value存進去
	public Map<Integer,ReturnInfo> getOneOrderAllReturnInfo(Integer orderNo) {
		
		Map<Integer,ReturnInfo> map = new HashMap<Integer,ReturnInfo>();
		
		List<OrderDetailVO> list = repository.findAllDetailsByOrderNo(orderNo);
		for(OrderDetailVO detail:list) {
			if(detail.getIsReturn()==1) {//找出有退貨的明細
				String info =detail.getReturnInfo();
				
				ObjectMapper objectmapper = new ObjectMapper();
				objectmapper.registerModule(new JavaTimeModule());
				ReturnInfo returnInfo=new ReturnInfo();
				try {
					returnInfo = objectmapper.readValue(info, ReturnInfo.class);
									
				} catch (JsonMappingException e) {
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				//手動查出該商品價格，設置進去
				Integer price =detail.getPrice();
				returnInfo.setPrice(price);
				
				//手動查出商品名稱，設置進去
				String name =detail.getCompositekey().getProductVO().getProdName();
				returnInfo.setName(name);
				
				
				//查出商品編號作為key
				Integer prodno =detail.getCompositekey().getProductVO().getProdNo();
				
				map.put(prodno, returnInfo);//主鍵存入有退的產品編號、值存入物件
				
			
			}
		}
//		System.out.println("service層印出infoMap:"+map);
		return map;
		
	}
	
	
	
	//查回一筆訂單的所有訂單明細，判斷該明細的is_return是否為1，若為1則取出裡面的return_photo
	//使用一個Map<Integer,String>，將return_photo存進去(byte[]轉成base64)
	public Map<Integer,String> getOneOrderAllReturnPic (Integer orderNo){
		Map<Integer,String> map = new HashMap<Integer,String>();
		
		List<OrderDetailVO> list = repository.findAllDetailsByOrderNo(orderNo);
		
		for(OrderDetailVO detail : list ) {
			if(detail.getIsReturn()==1) {
				byte [] pic = detail.getReturnPhoto();
				String picString =Base64.getEncoder().encodeToString(pic);
				
				Integer prodno =detail.getCompositekey().getProductVO().getProdNo();
				map.put(prodno, picString);
			}
		}
//		System.out.println("service層印出photoMap:"+map);
		return map;
	}
	
	//回傳一個訂單中有退的所有商品編號
	public List<Integer> getOneOrderAllReturnProdNo (Integer orderNo){
		List<Integer> list =new ArrayList<Integer>();
		
		List<OrderDetailVO> voList= repository.findAllDetailsByOrderNo(orderNo);
		for(OrderDetailVO VO : voList) {
			if(VO.getIsReturn()==1) {
				list.add(VO.getCompositekey().getProductVO().getProdNo());
			}
		}
		
//		for(Integer i :list) {
//			System.out.println("service層印出一個訂單編號內所有有退的產品編號:"+list);	
//		}
	
		return list;
		
	}
	
	
	//查出所有有退貨的訂單編號、迭代每個訂單編號取得DTO、手動查出該訂單退貨的applytime(來自於returnInfo)、每個物件存入一個List、回傳控制器
	
	public List<ShowReturnOrderDTO> getAllReturnOrder(){
		
		List<ShowReturnOrderDTO> dtoList =new ArrayList<ShowReturnOrderDTO>();
		
		List<Integer> allOrderNo =repository.getAllReturnOrderNo();
		
		//查出所有有退的訂單編號，迭代編號，取得各自的資料(一個DTO)
		for(Integer orderno : allOrderNo) {
			ShowReturnOrderDTO DTO = orderRepository.getReturnOrderByorderNo(orderno);
			
			//手動設置時間到DTO裡面
			String infoJson =repository.getOneReturnInfoStringByOrderNoAndIsReturnLimit1(orderno);
			ObjectMapper objectmapper = new ObjectMapper();
			objectmapper.registerModule(new JavaTimeModule());
			ReturnInfo returnInfo=new ReturnInfo();
			try {
				//將JSON轉換回物件
				returnInfo = objectmapper.readValue(infoJson, ReturnInfo.class);				
								
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			
			DTO.setApplyTime(returnInfo.getApplyTime()); //從returnInfo取得時間設置進去(LocalDate型別)
			dtoList.add(DTO);//每次都存到list裡面		
			
		}
		
		return dtoList;
		
	}
	
	
	
}//->class end

