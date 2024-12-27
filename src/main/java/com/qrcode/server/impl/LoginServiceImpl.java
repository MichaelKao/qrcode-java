package com.qrcode.server.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.qrcode.model.po.Store;
import com.qrcode.model.po.User;
import com.qrcode.model.vo.BusinessHourVo;
import com.qrcode.model.vo.QrCodeVo;
import com.qrcode.model.vo.UserDetailVo;
import com.qrcode.model.vo.UserProductVo;
import com.qrcode.model.vo.UserStoreVo;
import com.qrcode.model.vo.UserVo;
import com.qrcode.model.vo.VerificationVo;
import com.qrcode.repository.BusinessHourRepository;
import com.qrcode.repository.ProductRepository;
import com.qrcode.repository.QrCodeRepository;
import com.qrcode.repository.StoreRepository;
import com.qrcode.repository.UserRepository;
import com.qrcode.server.LoginService;
import com.qrcode.util.VerificationCodeUtil;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private VerificationCodeUtil verificationCodeUtil;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private BusinessHourRepository businessHourRepository;

	@Autowired
	private QrCodeRepository qrCodeRepository;

	/**
	 * signIn 使用者註冊
	 * 
	 * @param userVo 前端傳入使用者資訊
	 * @return
	 */
	@Override
	public UserDetailVo signIn(UserVo userVo) {
		// 帳號
		String account = userVo.getAccount();
		// 電子信箱
		String email = userVo.getEmail();
		// 驗證碼
		String code = userVo.getCode();
		// 以帳號查詢使用者資訊
		Optional<User> userOptional = userRepository.findByAccount(account);
		// 檢查使用者是否存在
		if (userOptional.isPresent()
				|| verificationCodeUtil.verifyCode(VerificationVo.builder().email(email).code(code).build())) {

			return null;
		} else {

			User user = new User();
			// 前端傳入vo轉成資料庫所需po
			BeanUtils.copyProperties(userVo, user);
			// 建立時間
			user.setCreateTime(LocalDateTime.now());
			// 修改時間
			user.setUpdateTime(LocalDateTime.now());
			// 資料不存在則新增
			user = userRepository.saveAndFlush(user);

			return UserDetailVo.builder().seq(user.getSeq()).account(account).password(code).email(email)
					.userStoreVo(null).build();
		}
	}

	/**
	 * login 使用者登入
	 * 
	 * @param userVo 前端傳入使用者資訊
	 * @return
	 * @throws IOException
	 */
	@Override
	public UserDetailVo login(UserVo userVo) throws IOException {
		// 帳號
		String account = userVo.getAccount();
		// 密碼
		String password = userVo.getPassword();
		// 電子信箱
		String email = userVo.getEmail();
		// 以帳號查詢使用者資訊
		Optional<User> userOptional = userRepository.findByAccountAndPassword(account, password);
		// 檢查使用者是否存在
		if (userOptional.isPresent()) {

			UserDetailVo result = new UserDetailVo();
			// 使用者資訊
			User user = userOptional.get();
			// 使用者序號
			result.setSeq(user.getSeq());
			// 帳號
			result.setAccount(account);
			// 密碼
			result.setPassword(password);
			// 電子信箱
			result.setEmail(email);

			Optional<Store> storeOptional = storeRepository.findByStoreSeq(user.getSeq()).stream().findFirst();

			if (storeOptional.isPresent()) {

				Store store = storeOptional.get();

				UserStoreVo userStoreVo = new UserStoreVo();

				BeanUtils.copyProperties(store, userStoreVo);
				// 把檔案轉換成base64
				String base64Logo = convertBase64(store.getLogo());

				userStoreVo.setLogo(base64Logo);
				// 取得營業時間
				List<BusinessHourVo> businessHourVoList = businessHourRepository.findByStoreSeq(store.getSeq()).stream()
						.map(businessHour -> {
							BusinessHourVo businessHourVo = new BusinessHourVo();
							BeanUtils.copyProperties(businessHour, businessHourVo);
							return businessHourVo;
						}).toList();
				// 取得商品資訊
				List<UserProductVo> userProductVoList = productRepository.findByProductSeq(store.getSeq()).stream()
						.map(product -> {
							UserProductVo productVo = new UserProductVo();
							BeanUtils.copyProperties(product, productVo);
							return productVo;
						}).toList();
				// 取得店家qrcode清單
				List<QrCodeVo> qrCodeVoList = qrCodeRepository.findByStoreSeq(store.getSeq()).stream().map(qrCode -> {
					QrCodeVo qrCodeVo = new QrCodeVo();

					String base64Qrcode = "";

					try {
						// 把檔案轉換成base64
						base64Qrcode = convertBase64(qrCode.getQrcode());
					} catch (IOException e) {
						e.printStackTrace();
					}

					BeanUtils.copyProperties(qrCode, qrCodeVo);

					qrCodeVo.setQrcode(base64Qrcode);

					return qrCodeVo;
				}).toList();

				userStoreVo.setBusinessHoursList(businessHourVoList);
				userStoreVo.setUserProductVoList(userProductVoList);
				userStoreVo.setQrCodeVoList(qrCodeVoList);

				result.setUserStoreVo(userStoreVo);

				return result;

			} else {
				return result;
			}
		}

		return null;

	}

	/**
	 * updateUserDetail 使用者資訊變更
	 * 
	 * @param userVo 前端傳入使用者資訊
	 */
	@Override
	public boolean updateUserDetail(UserVo userVo) {
		// 帳號
		String account = userVo.getAccount();
		// 以帳號查詢使用者資訊
		Optional<User> userOptional = userRepository.findByAccount(account);
		// 檢查使用者是否存在
		if (userOptional.isPresent()) {

			User user = userOptional.get();
			// 密碼
			String password = userVo.getPassword();
			// 電子信箱
			String email = userVo.getEmail();
			// 更新密碼
			user.setPassword(password);
			// 更新電子信箱
			user.setEmail(email);
			// 更新
			userRepository.saveAndFlush(user);

			return true;
		} else {
			return false;
		}

	}

	@Override
	public UserDetailVo getUserDetailBySeq(Long seq) throws Exception {

		User user = userRepository.findById(seq).orElseThrow(() -> new Exception());

		UserDetailVo result = new UserDetailVo();
		// 使用者序號
		result.setSeq(user.getSeq());
		// 帳號
		result.setAccount(user.getAccount());
		// 密碼
		result.setPassword(user.getPassword());
		// 電子信箱
		result.setEmail(user.getEmail());

		Optional<Store> storeOptional = storeRepository.findByStoreSeq(user.getSeq()).stream().findFirst();

		if (storeOptional.isPresent()) {

			Store store = storeOptional.get();

			UserStoreVo userStoreVo = new UserStoreVo();

			BeanUtils.copyProperties(store, userStoreVo);
			// 把檔案轉換成base64
			String base64Logo = convertBase64(store.getLogo());

			userStoreVo.setLogo(base64Logo);
			// 取得營業時間
			List<BusinessHourVo> businessHourVoList = businessHourRepository.findByStoreSeq(store.getSeq()).stream()
					.map(businessHour -> {
						BusinessHourVo businessHourVo = new BusinessHourVo();
						BeanUtils.copyProperties(businessHour, businessHourVo);
						return businessHourVo;
					}).toList();
			// 取得商品資訊
			List<UserProductVo> userProductVoList = productRepository.findByProductSeq(store.getSeq()).stream()
					.map(product -> {
						UserProductVo productVo = new UserProductVo();
						BeanUtils.copyProperties(product, productVo);
						return productVo;
					}).toList();
			// 取得店家qrcode清單
			List<QrCodeVo> qrCodeVoList = qrCodeRepository.findByStoreSeq(store.getSeq()).stream().map(qrCode -> {
				QrCodeVo qrCodeVo = new QrCodeVo();

				String base64Qrcode = "";

				try {
					// 把檔案轉換成base64
					base64Qrcode = convertBase64(qrCode.getQrcode());
				} catch (IOException e) {
					e.printStackTrace();
				}

				BeanUtils.copyProperties(qrCode, qrCodeVo);

				qrCodeVo.setQrcode(base64Qrcode);

				return qrCodeVo;
			}).toList();

			userStoreVo.setBusinessHoursList(businessHourVoList);
			userStoreVo.setUserProductVoList(userProductVoList);
			userStoreVo.setQrCodeVoList(qrCodeVoList);

			result.setUserStoreVo(userStoreVo);

			return result;

		}

		return null;
	}

	/**
	 * convertBase64 把檔案轉換成base64
	 * 
	 * @param filePath 檔案路徑
	 * @return base64
	 * @throws IOException
	 */
	private String convertBase64(String filePath) throws IOException {

		if (StringUtils.isBlank(filePath)) {
			return "";
		}

		Path path = Paths.get(filePath);

		byte[] imageBytes;

		imageBytes = Files.readAllBytes(path);

		return Base64.getEncoder().encodeToString(imageBytes);

	}

}
