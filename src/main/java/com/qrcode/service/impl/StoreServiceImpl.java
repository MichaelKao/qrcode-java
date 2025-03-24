package com.qrcode.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qrcode.model.ResponseResult;
import com.qrcode.model.po.BusinessHour;
import com.qrcode.model.po.Product;
import com.qrcode.model.po.QrCode;
import com.qrcode.model.po.Store;
import com.qrcode.model.vo.BusinessHourVo;
import com.qrcode.model.vo.ProductVo;
import com.qrcode.model.vo.StoreVo;
import com.qrcode.model.vo.UserDetailVo;
import com.qrcode.repository.BusinessHourRepository;
import com.qrcode.repository.ProductRepository;
import com.qrcode.repository.QrCodeRepository;
import com.qrcode.repository.StoreRepository;
import com.qrcode.repository.UserRepository;
import com.qrcode.service.LoginService;
import com.qrcode.service.StoreService;
import com.qrcode.util.QrcodeUtil;

@Service
@Transactional
public class StoreServiceImpl implements StoreService {

	@Autowired
	private QrcodeUtil qrcodeUtil;

	@Autowired
	private StoreRepository storeRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BusinessHourRepository businessHourRepository;

	@Autowired
	private QrCodeRepository qrCodeRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private LoginService loginService;

	/**
	 * createStore 創建商店
	 * 
	 * @param storeVo 商店資訊
	 * @throws Exception
	 */
	@Override
	public ResponseResult<UserDetailVo> createStore(StoreVo storeVo) throws Exception {

		Long storeSeq = storeVo.getStoreSeq();

		if (!checkUserExists(storeSeq)) {
			return ResponseResult.<UserDetailVo>builder().code(401).message("查無商店資訊").build();
		}
		// 儲存商店logo圖片
		String logoPath = getLogoPath(storeVo);

		int seats = storeVo.getSeats();
		// 資料存入商店
		Store savedStore = storeRepository.saveAndFlush(Store.builder().storeSeq(storeSeq)
				.storeName(storeVo.getStoreName()).description(storeVo.getDescription()).phone(storeVo.getPhone())
				.email(storeVo.getEmail()).logo(logoPath).postCode(storeVo.getPostCode()).city(storeVo.getCity())
				.district(storeVo.getDistrict()).streetAddress(storeVo.getStreetAddress())
				.createTime(LocalDateTime.now()).updateTime(LocalDateTime.now()).seats(seats).build());
		// 前端json字串轉換成物件
		List<BusinessHourVo> businessHourVoList = convertToBusinessHours(storeVo.getBusinessHours());
		// 寫入營業時間資料表
		businessHourRepository.saveAllAndFlush(businessHourVoList.stream().map(businessHourVo -> {

			return BusinessHour.builder().storeSeq(savedStore.getSeq()).isOpen(businessHourVo.isOpen())
					.week(businessHourVo.getWeek()).openTime(businessHourVo.getOpenTime())
					.closeTime(businessHourVo.getCloseTime()).build();

		}).toList());
		// 依照座位號產生qrcode 第0個是店家所用
		for (int i = 0; i < seats + 1; i++) {
			String qrcodePath = qrcodeUtil.generateQRCodeForStore(savedStore.getSeq());

			qrCodeRepository.saveAndFlush(QrCode.builder().storeSeq(savedStore.getSeq()).qrcode(qrcodePath).num(i)
					.createTime(LocalDateTime.now()).updateTime(LocalDateTime.now()).build());

		}

		return loginService.getUserDetailBySeq(storeSeq);

	}

	/**
	 * viewStore 檢視商店
	 * 
	 * @param storeVo 商店資訊
	 * @throws IOException
	 */
	@Override
	public ResponseResult<Store> viewStore(Long seq) {

		Store result = storeRepository.findById(seq).orElse(null);

		if (Objects.isNull(result)) {
			ResponseResult.<Store>builder().code(401).message("查無商店資訊").build();
		}

		return ResponseResult.<Store>builder().code(200).message("檢視商店成功").data(result).build();
	}

	/**
	 * updateStore 更新商店
	 * 
	 * @param storeVo
	 * @throws Exception
	 */
	@Override
	public ResponseResult<UserDetailVo> updateStore(StoreVo storeVo) throws Exception {
		// 商店序號
		Long seq = storeVo.getSeq();
		// 使用者的序號
		Long userSeq = storeVo.getStoreSeq();

		Optional<Store> storeOptional = storeRepository.findById(seq);

		if (storeOptional.isPresent()) {
			Store store = storeOptional.get();
			// 原座位數
			int seats = store.getSeats();
			// 商店名稱
			store.setStoreName(storeVo.getStoreName());
			// 商店描述
			store.setDescription(storeVo.getDescription());
			// 商店號碼
			store.setPhone(storeVo.getPhone());
			// 商店信箱
			store.setEmail(storeVo.getEmail());
			// 郵遞區號
			store.setPostCode(storeVo.getPostCode());
			// 縣市
			store.setCity(storeVo.getCity());
			// 鄉鎮區域
			store.setDistrict(storeVo.getDistrict());
			// 詳細地址
			store.setStreetAddress(storeVo.getStreetAddress());
			// 商店座位
			store.setSeats(storeVo.getSeats());
			// 儲存商店logo圖片
			String logoPath = getLogoPath(storeVo);
			// 商店商標
			store.setLogo(logoPath);

			storeRepository.save(store);

			List<BusinessHour> businessHourList = businessHourRepository.findByStoreSeq(seq);

			if (!businessHourList.isEmpty()) {

				try {
					// 使用 deleteAllInBatch 替代 deleteAll，避免逐個檢查實體狀態
					businessHourRepository.deleteAllInBatch(businessHourList);
				} catch (Exception e) {
					// 如果刪除失敗，重新獲取最新的營業時間記錄
					businessHourList = businessHourRepository.findByStoreSeq(seq);
					businessHourRepository.deleteAllInBatch(businessHourList);
				}

				// 前端json字串轉換成物件
				List<BusinessHourVo> businessHourVoList = convertToBusinessHours(storeVo.getBusinessHours());
				// 寫入營業時間資料表
				businessHourRepository.saveAllAndFlush(businessHourVoList.stream().map(businessHourVo -> {

					return BusinessHour.builder().storeSeq(seq).isOpen(businessHourVo.isOpen())
							.week(businessHourVo.getWeek()).openTime(businessHourVo.getOpenTime())
							.closeTime(businessHourVo.getCloseTime()).build();

				}).toList());
			}
			// 依照座位號產生qrcode 第0個是店家所用
			if (seats != storeVo.getSeats()) {

				List<QrCode> qrCodeList = qrCodeRepository.findByStoreSeq(seq);

				if (!qrCodeList.isEmpty()) {
					qrCodeRepository.deleteAll(qrCodeList);
				}
				// 依照座位號產生qrcode 第0個是店家所用
				for (int i = 0; i < storeVo.getSeats() + 1; i++) {
					String qrcodePath = qrcodeUtil.generateQRCodeForStore(seq);

					qrCodeRepository.saveAndFlush(QrCode.builder().storeSeq(seq).qrcode(qrcodePath).num(i)
							.createTime(LocalDateTime.now()).updateTime(LocalDateTime.now()).build());
				}
			}
		}

		return loginService.getUserDetailBySeq(userSeq);
	}

	/**
	 * createProduct 創建商品
	 * 
	 * @param products 商品資訊
	 * @param files    商品圖片
	 * @throws IOException
	 */
	@Override
	public ResponseResult<UserDetailVo> createProduct(ProductVo productVo) throws IOException {
		// 商店序號
		Long storeSeq = productVo.getProductSeq();

		Optional<Store> storeOptional = storeRepository.findByStoreSeq(storeSeq);

		if (storeOptional.isPresent()) {

			Store store = storeOptional.get();
			// 儲存商品圖片
			String picturePath = getPicturePath(productVo);

			productRepository.save(Product.builder().productSeq(store.getSeq()).productName(productVo.getProductName())
					.productPrice(productVo.getProductPrice()).description(productVo.getDescription())
					.spicy(productVo.isSpicy()).coriander(productVo.isCoriander()).vinegar(productVo.isVinegar())
					.picture(picturePath).createTime(LocalDateTime.now()).updateTime(LocalDateTime.now()).build());

			return loginService.getUserDetailBySeq(storeSeq);
		}

		return ResponseResult.<UserDetailVo>builder().code(401).message("創建商品失敗").build();

	}

	/**
	 * updateProduct 更新商品資訊
	 * 
	 * @param product 商品
	 * @return
	 * @throws IOException
	 */
	@Override
	public ResponseResult<UserDetailVo> updateProduct(ProductVo productVo) throws IOException {
		// 商店序號
		Long storeSeq = productVo.getProductSeq();
		// 商品序號
		Long productSeq = productVo.getSeq();

		Optional<Product> productOptional = productRepository.findById(productSeq);

		if (productOptional.isPresent()) {

			Product product = productOptional.get();
			// 儲存商品圖片
			String picturePath = getPicturePath(productVo);

			product.setProductName(productVo.getProductName());

			product.setProductPrice(productVo.getProductPrice());

			product.setDescription(productVo.getDescription());

			product.setSpicy(productVo.isSpicy());

			product.setCoriander(productVo.isCoriander());

			product.setVinegar(productVo.isVinegar());

			product.setPicture(picturePath);

			product.setUpdateTime(LocalDateTime.now());

			productRepository.save(product);

			Store store = storeRepository.findById(storeSeq).orElse(null);

			return loginService.getUserDetailBySeq(store.getStoreSeq());
		}

		return ResponseResult.<UserDetailVo>builder().code(401).message("更新商品失敗").build();
	}

	/**
	 * deleteProduct 刪除商品資訊
	 * 
	 * @param seq 商品序號
	 * @return
	 */
	@Override
	public ResponseResult<Void> deleteProduct(Long seq) {
		// 商品序號
		productRepository.deleteById(seq);

		return ResponseResult.success(null);
	}

	/**
	 * checkUserExists 檢查使用者是否存在
	 * 
	 * @param seq 使用者序號
	 * @return 檢查結果
	 */
	private boolean checkUserExists(Long seq) {

		return userRepository.existsById(seq);

	}

	/**
	 * getLogoPath 儲存商店logo圖片
	 * 
	 * @param storeVo 商店資訊
	 * @return logo圖片位置
	 * @throws IOException
	 */
	private String getLogoPath(StoreVo storeVo) throws IOException {
		// 處理圖片上傳
		String logoPath = null;

		if (Objects.nonNull(storeVo.getLogo())) {
			// 生成檔案名稱
			String originalFilename = storeVo.getLogo().getOriginalFilename();
			String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
			String newFileName = UUID.randomUUID().toString() + fileExtension;
			// 儲存路徑
			Path uploadPath = Paths.get("./uploads/logos");
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			// 儲存檔案
			Path filePath = uploadPath.resolve(newFileName);
			Files.copy(storeVo.getLogo().getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			logoPath = filePath.toString();
		}

		return logoPath;
	}

	/**
	 * getPicturePath 儲存商品圖片
	 * 
	 * @param productVo
	 * @return
	 * @throws IOException
	 */
	private String getPicturePath(ProductVo productVo) throws IOException {
		// 處理圖片上傳
		String resultPath = null;

		if (Objects.nonNull(productVo.getPictureFile())) {
			// 生成檔案名稱
			String originalFilename = productVo.getPictureFile().getOriginalFilename();
			String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
			String newFileName = UUID.randomUUID().toString() + fileExtension;
			// 儲存路徑
			Path picturesPath = Paths.get("./uploads/pictures");
			if (!Files.exists(picturesPath)) {
				Files.createDirectories(picturesPath);
			}
			// 儲存檔案
			Path filePath = picturesPath.resolve(newFileName);
			Files.copy(productVo.getPictureFile().getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

			resultPath = filePath.toString();
		}

		return resultPath;
	}

	private List<BusinessHourVo> convertToBusinessHours(String businessHoursJson) {
		try {

			return objectMapper.readValue(businessHoursJson, new TypeReference<List<BusinessHourVo>>() {
			});

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
