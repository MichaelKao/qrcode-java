package com.qrcode.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qrcode.model.po.BusinessHour;
import com.qrcode.model.po.Product;

@Repository
public interface BusinessHourRepository extends JpaRepository<BusinessHour, Long> {

	/**
	 * findByStoreSeq 以商店序號查詢營業時間
	 * 
	 * @param storeSeq 商店序號
	 * @return 商店營業時間
	 */
	List<Product> findByStoreSeq(Long storeSeq);

}
