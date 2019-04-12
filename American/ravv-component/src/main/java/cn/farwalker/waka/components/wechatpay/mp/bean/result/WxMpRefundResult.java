package cn.farwalker.waka.components.wechatpay.mp.bean.result;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.io.Serializable;

public class WxMpRefundResult implements Serializable {

    @XStreamAlias("return_code")
    String return_code;
    @XStreamAlias("return_msg")
    String return_msg;
    @XStreamAlias("sign")
    String sign;
    @XStreamAlias("result_code")
    String result_code;
    @XStreamAlias("err_code")
    String err_code;
    @XStreamAlias("err_code_des")
    String err_code_des;
    @XStreamAlias("appid")
    String appid;
    @XStreamAlias("mch_id")
    String mch_id;
    @XStreamAlias("nonce_str")
    String nonce_str;
    @XStreamAlias("transaction_id")
    String transaction_id;
    @XStreamAlias("out_trade_no")
    String out_trade_no;
    @XStreamAlias("out_refund_no")
    String out_refund_no;
    @XStreamAlias("refund_id")
    String refund_id;
    @XStreamAlias("refund_fee")
    int refund_fee;
    @XStreamAlias("settlement_refund_fee")
    int settlement_refund_fee;
    @XStreamAlias("total_fee")
    int total_fee;
    @XStreamAlias("settlement_total_fee")
    int settlement_total_fee;
    @XStreamAlias("fee_type")
    String fee_type;
    @XStreamAlias("cash_fee")
    int cash_fee;
    @XStreamAlias("cash_fee_type")
    String cash_fee_type;
    @XStreamAlias("cash_refund_fee")
    int cash_refund_fee;
    @XStreamAlias("coupon_refund_fee")
    int coupon_refund_fee;
    @XStreamAlias("coupon_refund_count")
    int coupon_refund_count;

    public String getReturnCode() {
        return return_code;
    }

    public void setReturnCode(String return_code) {
        this.return_code = return_code;
    }

    public String getReturnMsg() {
        return return_msg;
    }

    public void setReturnMsg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResultCode() {
        return result_code;
    }

    public void setResultCode(String result_code) {
        this.result_code = result_code;
    }

    public String getErrCode() {
        return err_code;
    }

    public void setErrCode(String err_code) {
        this.err_code = err_code;
    }

    public String getErrCodeDes() {
        return err_code_des;
    }

    public void setErrCodeDes(String err_code_des) {
        this.err_code_des = err_code_des;
    }

    public String getAppId() {
        return appid;
    }

    public void setAppId(String appid) {
        this.appid = appid;
    }

    public String getMchId() {
        return mch_id;
    }

    public void setMchId(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getNonceStr() {
        return nonce_str;
    }

    public void setNonceStr(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getTransactionId() {
        return transaction_id;
    }

    public void setTransactionId(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOutTradeNo() {
        return out_trade_no;
    }

    public void setOutTradeNo(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getOutRefundNo() {
        return out_refund_no;
    }

    public void setOutRefundNo(String out_refund_no) {
        this.out_refund_no = out_refund_no;
    }

    public String getRefundId() {
        return refund_id;
    }

    public void setRefundId(String refund_id) {
        this.refund_id = refund_id;
    }

    public int getRefundFee() {
        return refund_fee;
    }

    public void setRefundFee(int refund_fee) {
        this.refund_fee = refund_fee;
    }

    public int getSettlementRefundFee() {
        return settlement_refund_fee;
    }

    public void setSettlementRefundFee(int settlement_refund_fee) {
        this.settlement_refund_fee = settlement_refund_fee;
    }

    public int getTotalFee() {
        return total_fee;
    }

    public void setTotalFee(int total_fee) {
        this.total_fee = total_fee;
    }

    public int getSettlementTotalFee() {
        return settlement_total_fee;
    }

    public void setSettlementTotalFee(int settlement_total_fee) {
        this.settlement_total_fee = settlement_total_fee;
    }

    public String getFeeType() {
        return fee_type;
    }

    public void setFeeType(String fee_type) {
        this.fee_type = fee_type;
    }

    public int getCashFee() {
        return cash_fee;
    }

    public void setCashFee(int cash_fee) {
        this.cash_fee = cash_fee;
    }

    public String getCashFeeType() {
        return cash_fee_type;
    }

    public void setCashFeeType(String cash_fee_type) {
        this.cash_fee_type = cash_fee_type;
    }

    public int getCashRefundFee() {
        return cash_refund_fee;
    }

    public void setCashRefundFee(int cash_refund_fee) {
        this.cash_refund_fee = cash_refund_fee;
    }

    public int getCouponRefundFee() {
        return coupon_refund_fee;
    }

    public void setCouponRefundFee(int coupon_refund_fee) {
        this.coupon_refund_fee = coupon_refund_fee;
    }

    public int getCouponRefundCount() {
        return coupon_refund_count;
    }

    public void setCouponRefundCount(int coupon_refund_count) {
        this.coupon_refund_count = coupon_refund_count;
    }

    @Override
    public String toString() {
        return "WxMpRefundResult{" +
                "return_code='" + return_code + '\'' +
                ", return_msg='" + return_msg + '\'' +
                ", sign='" + sign + '\'' +
                ", result_code='" + result_code + '\'' +
                ", err_code='" + err_code + '\'' +
                ", err_code_des='" + err_code_des + '\'' +
                ", appid='" + appid + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", nonce_str='" + nonce_str + '\'' +
                ", transaction_id='" + transaction_id + '\'' +
                ", out_trade_no='" + out_trade_no + '\'' +
                ", out_refund_no='" + out_refund_no + '\'' +
                ", refund_id='" + refund_id + '\'' +
                ", refund_fee=" + refund_fee +
                ", settlement_refund_fee=" + settlement_refund_fee +
                ", total_fee=" + total_fee +
                ", settlement_total_fee=" + settlement_total_fee +
                ", fee_type='" + fee_type + '\'' +
                ", cash_fee=" + cash_fee +
                ", cash_fee_type='" + cash_fee_type + '\'' +
                ", cash_refund_fee=" + cash_refund_fee +
                ", coupon_refund_fee=" + coupon_refund_fee +
                ", coupon_refund_count=" + coupon_refund_count +
                '}';
    }

}
