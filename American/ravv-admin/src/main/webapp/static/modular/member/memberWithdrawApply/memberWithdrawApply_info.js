/**
 * 初始化提现申请详情对话框
 */
var MemberWithdrawApplyInfoDlg = {
    memberWithdrawApplyInfoData : {}
};

/**
 * 清除数据
 */
MemberWithdrawApplyInfoDlg.clearData = function() {
    this.memberWithdrawApplyInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberWithdrawApplyInfoDlg.set = function(key, val) {
    this.memberWithdrawApplyInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberWithdrawApplyInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MemberWithdrawApplyInfoDlg.close = function() {
    parent.layer.close(window.parent.MemberWithdrawApply.layerIndex);
}

/**
 * 收集数据
 */
MemberWithdrawApplyInfoDlg.collectData = function() {
    this
    .set('id')
    .set('memberId')
    .set('bankId')
    .set('bankName')
    .set('bankType')
    .set('cardNumber')
    .set('applyNo')
    .set('applyFee')
    .set('paycost')
    .set('percentage')
    .set('applyTime')
    .set('checkTime')
    .set('checkerId')
    .set('checkerName')
    .set('withdrawTime')
    .set('gmtCreate')
    .set('gmtModified')
    .set('remark');
}

/**
 * 提交添加
 */
MemberWithdrawApplyInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberWithdrawApply/add", function(data){
        Feng.success("添加成功!");
        window.parent.MemberWithdrawApply.table.refresh();
        MemberWithdrawApplyInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberWithdrawApplyInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
MemberWithdrawApplyInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberWithdrawApply/update", function(data){
        Feng.success("修改成功!");
        window.parent.MemberWithdrawApply.table.refresh();
        MemberWithdrawApplyInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberWithdrawApplyInfoData);
    ajax.start();
}

$(function() {

});
