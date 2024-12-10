package com.cia103g5.common.verificationcode;

/**#########################
#                          #
#         驗證碼產生器        # 
#                          #
##########################*/
import java.security.SecureRandom;

public class VerificationCodeGenerator {

	// 驗證碼僅由數字組成，默認長度為6位。
	// 可自行定義字串池內容
	private static final String CHAR_POOL = "0123456789";
	private static final SecureRandom RANDOM = new SecureRandom();
	private static final int CODE_LENGTH = 6; // 固定6位數

	// 生成 6 位數驗證碼
	public static String generateCode() {
		//
		StringBuilder code = new StringBuilder(CODE_LENGTH);
		for (int i = 0; i < CODE_LENGTH; i++) {
			code.append(CHAR_POOL.charAt(RANDOM.nextInt(CHAR_POOL.length())));
		}
		// return 驗證碼
		return code.toString();
	}

	// 允許生成自定義長度的數字驗證碼
	// @param length 驗證碼長度
	// @return 驗證碼
	public static String generateCode(int length) {
		if (length <= 0) {
			throw new IllegalArgumentException("Code length must be greater than 0.");
		}

		StringBuilder code = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			code.append(CHAR_POOL.charAt(RANDOM.nextInt(CHAR_POOL.length())));
		}
		return code.toString();
	}

}
