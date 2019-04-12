package cn.farwalker.waka.core;


/**
 * 所有业务异常的枚举
 *
 * @author Jason Chen
 * @date 2016年11月12日 下午5:04:51
 */
public enum RavvExceptionEnum implements ServiceExceptionEnum {

    /**
     * 数据库操作相关
     */
    INSERT_ERROR(101,"INSERT ERROR"),
    UPDATE_ERROR(102,"UPDATE ERROR"),
    DELETE_ERROR(103,"DELETE ERROR"),
    SELECT_ERROR(104,"SELECT ERROR"),

    /**
     * 请求参数相关
     */
    INVALID_PARAMETER_ERROR(201,"INVALID PARAMETER ERROR"),

    /**
     * token异常
     */
    TOKEN_EXPIRED(301, "TOKEN EXPIRED"),
    TOKEN_VERIFICATION_FAILED(302, "TOKEN VERIFICATION FAILED"),
    SIGN_VERIFICATION_FAILED(303,"SIGN VERIFICATION FAILED"),
    TOKEN_PARSE_MEMBER_ID_FAILED(304,"TOKEN PARSE MEMBER ID FAILED"),

    /**
     * 用户注册,登录,校验相关
     */
    USER_EMAIL_VERIFICATION_FAILED(401,"Incorrect verification code"),
    USER_CHECK_FAILED(402, "User check failed"),
    USER_NO_REGISTER(403, "Invalid user ID or password"),
    USER_ACCOUNT_OR_PASSWORD_INCORRECT(404, "Invalid user ID or password"),
    USER_DUPLICATE_REGISTER_ERROR(405, "This account already exists"),
    USER_CAPTCHA_FAILED(406,"GENERATED CAPTCHA FAILED"),
    USER_CAPTCHA_VALIDATOR_FAILED(407,"CAPTCHA VALIDATOR FAILED"),
    USER_MEMBER_ID_ERROR(408,"USER MEMBER ID ERROR"),
    USER_MEMBER_REFERRALCODE_ERROR(409,"USER_MEMBER_REFERRALCODE_ERROR"),


    /**
     * 商品相关
     */
    GOODS_NOT_FIND(501,"GOODS NOT FIND"),
    GOODS_DATA_ERROR(502,"GOODS DATA ERROR"),
    GOODS_STOCK_SHORTAGE_ERROR(503,"GOODS STOCK SHORTAGE ERROR"),
    GOODS_HAS_BEEN_TAKEN_OFF_THE_SHELVES(504,"GOODS HAS BEEN TAKEN OFF THE SHELVES"),


	
	/**
	 * 余额不足
	 */
	NOT_ENOUGH_ADVANCE(801, "NOT ENOUGH ADVANCE"),

    /**
     * 数据解析错误
     */
    DATA_PARSE_ERROR(901,"DATA PARSE ERROR"),

    /**
     *直播相关数据
     */
    ANCHOR_NOT_REGISTER(1001,"ANCHOR NOT REGISTER"),
    ANCHOR_IS_FROZEN(1002,"ANCHOR IS FROZEN"),

    /**
     *支付相关
     */
    PAY_FAILED(2001,"PAY FAILED"),
    PAY_LOG_UPDATE_FAILED(2002,"PAY LOG UPDATE FAILED"),
    INCORRECT_PASSWORD(2003,"INCORRECT PASSWORD"),


    /**
     * 系统错误相关
     */
    FILE_READING_ERROR(3001, "FILE_READING_ERROR!"),
    FILE_NOT_FOUND(3002, "FILE_NOT_FOUND!"),
	;
    RavvExceptionEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private Integer code;

    private String message;

    @Override
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
