package com.cia103g5.user.product.model;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("productService")
public class ProductService {

	@Autowired
	ProductRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;

	
	public Integer addProduct(ProductVO productVO) {
		repository.save(productVO);
		return productVO.getProdNo();
	}
	
	public void updateProduct(ProductVO productVO) {
		repository.save(productVO);
	}
	
	public void deleteProduct(Integer prodNo) {
		if(repository.existsById(prodNo)) {
			repository.deleteById(prodNo);
		}
	}
	
	
	//單一查詢
	public ProductVO getOneProduct(Integer prod_no) {
		Optional<ProductVO> optional =repository.findById(prod_no);
//		ProductVO product2 = optional.get();
		return optional.orElse(null);
	}
	
	
	//查詢所有的商品(商表格內所有存的商品，不分占卜師帳號)
	public List<ProductVO> getAll(){
		return repository.findAll();
	}

	
	//透過ft_id查詢該占卜師所有的商品，排除刪除過的商品(ft_id作為參數傳入)
	public List<ProductVO> getFtAllProductExcludeDelete(Integer ftId){
		return repository.findAllByFtIdExcludeDelete(ftId);
	}
	
	
	
	//查詢所有的商品，排除刪除的商品(不分占卜師帳號)
	public List<ProductVO> getAllExcludeDelete(){
		return repository.findAllExcludeDelete();
	}
	
	//指定商品只有更新status狀態為2
	@Transactional
	public void  updateStatus(Integer prodno) {
		repository.updateOneStatus(prodno);
	}
	
	//選擇狀態查詢
	public List<ProductVO> getAllProductByStatus(Byte status){
		return repository.getAllProductByStatus(status);
	}
	
	
	//占卜師選擇狀態查詢
	public List<ProductVO> getAllProductByStatusAndFtId(Byte status,Integer ftId){		
			return repository.getAllProductByStatusAndFtId(status,ftId);	
		}
	
	
	//選擇複合狀態查詢
	public List<ProductVO> findProductsByMutiCondition(Byte status,String prodName,Integer prodNo){		
		return repository.findProductsByMutiCondition(status, prodName, prodNo);

	}
	
	
	//占卜師選擇複合狀態查詢
	public List<ProductVO> findProductsByMutiConditionByFtId(Byte status,String prodName,Integer prodNo,Integer ftId){		
		return repository.findProductsByMutiConditionByFtId(status, prodName, prodNo,ftId);

	}
	
	
}
