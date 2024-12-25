package com.qrcode.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qrcode.model.po.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * findByAccount 以帳號查詢使用者資訊
	 * 
	 * @param account 帳號
	 * @return 使用者資訊
	 */
	Optional<User> findByAccount(String account);

	/**
	 * findByEmail 以電子信箱查詢使用者資訊
	 * 
	 * @param email 電子信箱
	 * @return 使用者資訊
	 */
	Optional<User> findByEmail(String email);

	/**
	 * findByAccountAndPassword 以帳號帳號查詢使用者資訊
	 * 
	 * @param account  帳號
	 * @param password 密碼
	 * @return 使用者資訊
	 */
	Optional<User> findByAccountAndPassword(String account, String password);

}
