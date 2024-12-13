package com.cia103g5.user.productCollection.model;

import com.cia103g5.user.productCollection.model.ProductCollectionVO;
import com.cia103g5.user.productCollection.model.ProductCollectionVO.CompositeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCollectionRepository extends JpaRepository<ProductCollectionVO, CompositeDetail> {

 
    List<ProductCollectionVO> findByMemId(Integer memId);

    
    List<ProductCollectionVO> findByProdNo(Integer prodNo);

    
    boolean existsByProdNoAndMemId(Integer prodNo, Integer memId);
}