package cn.farwalker.ravv.service.member.accountflow.model;

import lombok.Data;

import java.util.List;

@Data
public class MemberTransactionListVo {

    List<MemberAccountFlowBo> transacntionList;
    boolean listIsNull;
}
