package com.qrcode.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qrcode.model.po.QrCode;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, Long> {

	/**
	 * findByStoreSeq 以商店序號查詢QrCode
	 * 
	 * @param storeSeq 商店序號
	 * @return QrCode
	 */
	List<QrCode> findByStoreSeq(Long storeSeq);
	
	Optional<QrCode> findFirstByStoreSeqOrderBySeqDesc(Long storeSeq);

}
