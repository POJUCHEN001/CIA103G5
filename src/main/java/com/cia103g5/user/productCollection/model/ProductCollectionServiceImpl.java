package com.cia103g5.user.productCollection.model;

import com.cia103g5.user.productCollection.model.ProductCollectionVO;
import com.cia103g5.user.productCollection.model.ProductCollectionVO.CompositeDetail;
import com.cia103g5.user.productCollection.model.ProductCollectionRepository;
import com.cia103g5.user.productCollection.model.ProductCollectionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class ProductCollectionServiceImpl implements ProductCollectionService {

    private final ProductCollectionRepository productCollectionRepository;

    @Autowired
    public ProductCollectionServiceImpl(ProductCollectionRepository productCollectionRepository) {
        this.productCollectionRepository = productCollectionRepository;
    }

    @Override
    public List<ProductCollectionVO> findByMemId(Integer memId) {
        return productCollectionRepository.findByMemId(memId);
    }

    @Override
    public List<ProductCollectionVO> findByProdNo(Integer prodNo) {
        return productCollectionRepository.findByProdNo(prodNo);
    }

    @Override
    public ProductCollectionVO addCollection(Integer memId, Integer prodNo) {
        if (productCollectionRepository.existsByProdNoAndMemId(prodNo, memId)) {
            throw new RuntimeException("商品已收藏");
        }

        ProductCollectionVO collection = new ProductCollectionVO();
        collection.setMemId(memId);
        collection.setProdNo(prodNo);
        collection.setCollectedAt(Date.valueOf(LocalDate.now())); 
        return productCollectionRepository.save(collection);
    }

    @Override
    public void removeCollection(Integer memId, Integer prodNo) {
        CompositeDetail key = new CompositeDetail(prodNo, memId);
        if (!productCollectionRepository.existsById(key)) {
            throw new RuntimeException("收藏記錄不存在");
        }
        productCollectionRepository.deleteById(key);
    }

    @Override
    public boolean existsByProdNoAndMemId(Integer prodNo, Integer memId) {
        return productCollectionRepository.existsByProdNoAndMemId(prodNo, memId);
    }
}
