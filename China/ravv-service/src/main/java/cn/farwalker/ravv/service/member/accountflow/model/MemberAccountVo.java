package cn.farwalker.ravv.service.member.accountflow.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MemberAccountVo {

    BigDecimal advance;
    boolean isPaymentPassword;
}
