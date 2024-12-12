package com.cia103g5.user.productImage.model;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("product_imageService")
public class ProductImageService {

	
	@Autowired
	ProductImageRepository repository;

	@Autowired
    private SessionFactory sessionFactory;
	
	public void addProductImg(ProductImageVO productImageVO) {
		repository.save(productImageVO);
		
	}
	
	public void updateProductImg(ProductImageVO productImageVO) {
		repository.save(productImageVO);
	}
	
	public void deleteProductImg(Integer imageNo) {
		if(repository.existsById(imageNo)) {
			repository.deleteById(imageNo);
		}
	}
	
	//使用PK:imageNo查詢單一圖片
	public ProductImageVO getOneProductImg(Integer imageNo) {
		Optional<ProductImageVO> optional =repository.findById(imageNo);
//		Product_imageVO product_image = optional.get();
		return optional.orElse(null);
	}
	
	public List<ProductImageVO> getAll(){
		return repository.findAll();
	}
	
	
	//使用FK:prodNO查詢主要圖片(回傳該圖)
	public ProductImageVO findPrimaryImageByProdNo(Integer prodNo) {
		return repository.findPrimaryImageByProdNo(prodNo);
	}
	
	//使用FK:ProdNo查詢該商品其他圖片的筆數
	public int findOthersPicsByProdNo(Integer prodNo) {
		return repository.findOthersPicsByProdNo(prodNo);
	}
	
	//使用FK:prodNo查詢該商品，主圖以外的其他圖片，使用list裝起來
	public List<ProductImageVO> getOtherPicVO(Integer prodNo){
		return repository.findOtherPicVO(prodNo);
	}
	
}
