package com.qrcode.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.qrcode.model.po.QrCode;
import com.qrcode.repository.QrCodeRepository;

@Component
public class QrcodeUtil {

	@Value("${app.base-url}")
	private String baseUrl;

	@Autowired
	private QrCodeRepository qrCodeRepository;

	/**
	 * generateQRCodeForStore 產生專屬qrcode
	 * 
	 * @return 專屬qrcode
	 */
	public String generateQRCodeForStore(Long storeSeq) {

		Optional<QrCode> qrCodeOptional = qrCodeRepository.findFirstByStoreSeqOrderBySeqDesc(storeSeq);

		int num = 0;

		if (qrCodeOptional.isPresent()) {
			// 取得最新num
			num = qrCodeOptional.get().getNum() + 1;
		}
		// qrcode網址
		String qrCodeUrl = baseUrl + "/" + storeSeq + "/" + num;

		try {
			// 生成 QR Code
			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(qrCodeUrl, BarcodeFormat.QR_CODE, 350, 350);
			// 確保目錄存在
			Path path = Paths.get("./qrcodes");
			if (!Files.exists(path)) {
				Files.createDirectories(path);
			}
			// 生成檔案名稱
			String fileName = "store_" + num + ".png";
			Path filePath = path.resolve(fileName);
			// 儲存 QR Code 圖片
			MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);

			return filePath.toString();

		} catch (Exception e) {
			throw new RuntimeException("生成 QR Code 失敗: " + e.getMessage());
		}
	}
}
