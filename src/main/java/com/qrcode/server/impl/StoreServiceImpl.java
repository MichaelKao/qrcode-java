package com.qrcode.server.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qrcode.model.QrCodeResponse;
import com.qrcode.model.po.BusinessHour;
import com.qrcode.model.po.Product;
import com.qrcode.model.po.QrCode;
import com.qrcode.model.po.Store;
import com.qrcode.model.vo.BusinessHourVo;
import com.qrcode.model.vo.ProductVo;
import com.qrcode.model.vo.StoreAndProductVo;
import com.qrcode.model.vo.StoreVo;
import com.qrcode.model.vo.UserDetailVo;
import com.qrcode.repository.BusinessHourRepository;
import com.qrcode.repository.ProductRepository;
import com.qrcode.repository.QrCodeRepository;
import com.qrcode.repository.StoreRepository;
import com.qrcode.repository.UserRepository;
import com.qrcode.server.LoginService;
import com.qrcode.server.StoreService;
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
	public UserDetailVo createStore(StoreVo storeVo) throws Exception {

		Long storeSeq = storeVo.getStoreSeq();

		if (!checkUserExists(storeSeq)) {
			throw new Exception();
		}
		// 儲存商店logo圖片
		String logoPath = getLogoPath(storeVo);

		int seats = storeVo.getSeats();
		// 前端json字串轉換成物件
		List<BusinessHourVo> businessHourVoList = convertToBusinessHours(storeVo.getBusinessHours());
		// 寫入營業時間資料表
		businessHourRepository.saveAllAndFlush(businessHourVoList.stream().map(businessHourVo -> {

			BusinessHour businessHour = new BusinessHour();

			BeanUtils.copyProperties(businessHourVo, businessHour);

			return BusinessHour.builder().storeSeq(storeSeq).isOpen(businessHour.isOpen()).week(businessHour.getWeek())
					.openTime(businessHour.getOpenTime()).closeTime(businessHour.getCloseTime()).build();

		}).toList());
		// 資料存入商店
		storeRepository.saveAndFlush(Store.builder().storeSeq(storeSeq).storeName(storeVo.getStoreName())
				.description(storeVo.getDescription()).phone(storeVo.getPhone()).email(storeVo.getEmail())
				.logo(logoPath).postCode(storeVo.getPostCode()).city(storeVo.getCity()).district(storeVo.getDistrict())
				.streetAddress(storeVo.getStreetAddress()).createTime(LocalDateTime.now())
				.updateTime(LocalDateTime.now()).build());

		// 依照座位號產生qrcode 第0個是店家所用
		for (int i = 0; i < seats + 1; i++) {
			String qrcodePath = qrcodeUtil.generateQRCodeForStore(storeSeq);

			qrCodeRepository.saveAndFlush(QrCode.builder().storeSeq(storeSeq).qrcode(qrcodePath).num(i)
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
	public Store viewStore(Long seq) {

		return storeRepository.findById(seq).orElse(null);
	}

	/**
	 * updateStore 更新商店
	 * 
	 * @param storeVo 商店資訊
	 * @throws IOException
	 */
	@Override
	public void updateStore(StoreVo storeVo) throws IOException {
		// 使用者的序號
		Long seq = storeVo.getSeq();
		// 儲存商店logo圖片
		String logoPath = getLogoPath(storeVo);

		Optional<Store> storeOptional = storeRepository.findById(seq);

		if (storeOptional.isPresent()) {

			Store store = storeOptional.get();
			// 商店名稱
			store.setStoreName(storeVo.getStoreName());
			// 商店描述
			store.setDescription(storeVo.getDescription());
			// 商店號碼
			store.setPhone(storeVo.getPhone());
			// 商店信箱
			store.setEmail(storeVo.getEmail());
			// 商店地址
//			store.setAddress(storeVo.getAddress());
//			// 商店營業時間
//			store.setBusinessHours(storeVo.getBusinessHours());
			// 商店商標
			store.setLogo(logoPath);
			// 修改時間
			store.setUpdateTime(LocalDateTime.now());

			storeRepository.save(store);
		}

	}

	/**
	 * getStoreQRCode 獲取qrcode
	 * 
	 * @param seq 商店資訊序號
	 * @return qrcode
	 * @throws MalformedURLException
	 */
	@Override
	public ResponseEntity<List<QrCodeResponse>> getStoreQRCode(Long seq) throws MalformedURLException {
		// 以商店序號查詢QrCode
		List<QrCode> qrCodeList = qrCodeRepository.findByStoreSeq(seq);

		if (qrCodeList.isEmpty()) {

			return ResponseEntity.notFound().build();

		} 
		
		// 將每個 QR code 轉換成回應物件
	    List<QrCodeResponse> responses = qrCodeList.stream()
	        .map(qrCode -> {
	            try {
	                Path path = Paths.get(qrCode.getQrcode());
	                // 將圖片轉成 Base64 字串
	                byte[] imageBytes = Files.readAllBytes(path);
	                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
	                
	                return new QrCodeResponse(
	                    qrCode.getNum(),
	                    base64Image
	                );
	            } catch (IOException e) {
	                throw new RuntimeException("Error reading QR code image", e);
	            }
	        })
	        .collect(Collectors.toList());

	    return ResponseEntity.ok(responses);
	}

	/**
	 * getStoreLogo 獲取logo
	 * 
	 * @param seq 商店資訊序號
	 * @return logo
	 */
	@Override
	public ResponseEntity<Resource> getStoreLogo(Long seq) throws MalformedURLException {
		return null;
//		// 以商店資訊序號取得商店資訊
//		Optional<Store> storeOptional = storeRepository.findById(seq);
//
//		if (storeOptional.isEmpty() || storeOptional.get().getQrcode() == null) {
//
//			return ResponseEntity.notFound().build();
//
//		} else {
//
//			Store store = storeOptional.get();
//
//			// 讀取文件
//			Path path = Paths.get(store.getLogo());
//			Resource resource = new UrlResource(path.toUri());
//
//			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(resource);
//		}
	}

	/**
	 * getProductLogo 獲取商品圖片
	 * 
	 * @param seq 商品資訊序號
	 * @return 商品圖片
	 * @throws MalformedURLException
	 */
	@Override
	public ResponseEntity<Resource> getProductLogo(Long seq) throws MalformedURLException {
		// 以商店資訊序號取得商店資訊
		Optional<Product> productOptional = productRepository.findById(seq);

		if (productOptional.isEmpty() || productOptional.get().getPicture() == null) {

			return ResponseEntity.notFound().build();

		} else {

			Product product = productOptional.get();

			// 讀取文件
			Path path = Paths.get(product.getPicture());
			Resource resource = new UrlResource(path.toUri());

			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(resource);
		}
	}

	/**
	 * createProduct 創建商品
	 * 
	 * @param products 商品資訊
	 * @param files    商品圖片
	 * @throws IOException
	 */
	@Override
	public void createProduct(List<ProductVo> products, List<MultipartFile> files) throws IOException {

		Map<String, MultipartFile> fileMap = new HashMap<>();
		if (files != null) {
			for (MultipartFile file : files) {
				fileMap.put(file.getOriginalFilename(), file);
			}
		}

		for (ProductVo product : products) {
			// 處理圖片上傳
			String picturePath = null;

			MultipartFile file = fileMap.get(product.getPictureFileName());

			if (file != null) {

				// 生成檔案名稱
				String originalFilename = file.getOriginalFilename();
				String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
				String newFileName = UUID.randomUUID().toString() + fileExtension;
				// 儲存路徑
				Path uploadPath = Paths.get("./uploads/pictures");
				if (!Files.exists(uploadPath)) {
					Files.createDirectories(uploadPath);
				}
				// 儲存檔案
				Path filePath = uploadPath.resolve(newFileName);
				Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

				picturePath = filePath.toString();

			}

			productRepository.save(Product.builder().productSeq(product.getStoreSeq()).productName(picturePath)
					.productPrice(product.getProductPrice()).description(product.getDescription())
					.sortOrder(product.getSortOrder()).spicy(product.isSpicy()).coriander(product.isCoriander())
					.vinegar(product.isVinegar()).createTime(LocalDateTime.now()).updateTime(LocalDateTime.now())
					.build());
		}

	}

	/**
	 * viewProduct 檢視商品資訊
	 * 
	 * @param seq 商店序號
	 * @return 商品資訊
	 */
	@Override
	public StoreAndProductVo viewProduct(Long seq) {
		// 以商店資訊序號取得商店資訊
		Optional<Store> storeOptional = storeRepository.findById(seq);

		if (storeOptional.isPresent()) {
			// 商店資訊
			Store store = storeOptional.get();
			// 商店序號
			Long storeSeq = store.getSeq();
			// 以商品序號查詢商品資訊
			List<Product> productList = productRepository.findByProductSeq(storeSeq);

			StoreVo storeVo = new StoreVo();
			BeanUtils.copyProperties(store, storeVo);

			return StoreAndProductVo.builder().storeVo(storeVo).productList(productList).build();
		}

		return null;
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
