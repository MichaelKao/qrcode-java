package com.qrcode.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qrcode.model.po.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	/**
	 * findByProductSeq 以商品序號查詢商品資訊
	 * 
	 * @param productSeq 商品序號 關聯store_t.storeSeq
	 * @return 商品資訊
	 */
	List<Product> findByProductSeq(Long productSeq);

}
