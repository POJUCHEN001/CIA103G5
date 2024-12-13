package com.cia103g5.user.productCollection.model;
import com.cia103g5.user.productCollection.model.ProductCollectionVO;

import java.util.List;

public interface ProductCollectionService {

    
    List<ProductCollectionVO> findByMemId(Integer memId);

 
    List<ProductCollectionVO> findByProdNo(Integer prodNo);

  
    ProductCollectionVO addCollection(Integer memId, Integer prodNo);

   
    void removeCollection(Integer memId, Integer prodNo);

    
    boolean existsByProdNoAndMemId(Integer prodNo, Integer memId);
}
