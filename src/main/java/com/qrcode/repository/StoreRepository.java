package com.qrcode.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qrcode.model.po.Store;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

	Optional<Store> findFirstByStoreSeqOrderBySeqDesc(Long storeSeq);

	List<Store> findByStoreSeq(Long storeSeq);

}
