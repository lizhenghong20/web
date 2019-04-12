/**
 * 初始化会员支付日志详情对话框
 */
var MemberPaymentLogInfoDlg = {
    memberPaymentLogInfoData : {}
};

/**
 * 清除数据
 */
MemberPaymentLogInfoDlg.clearData = function() {
    this.memberPaymentLogInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberPaymentLogInfoDlg.set = function(key, val) {
    this.memberPaymentLogInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberPaymentLogInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MemberPaymentLogInfoDlg.close = function() {
    parent.layer.close(window.parent.MemberPaymentLog.layerIndex);
}

/**
 * 收集数据
 */
MemberPaymentLogInfoDlg.collectData = function() {
    this
    .set('id')
    .set('money')
    .set('curMoney')
    .set('status')
    .set('payMethod')
    .set('payType')
    .set('payedTime')
    .set('operatorId')
    .set('account')
    .set('bank')
    .set('payAccount')
    .set('currencyCode')
    .set('paycost')
    .set('ip')
    .set('payBeginTime')
    .set('payConfirmTime')
    .set('memo')
    .set('returnUrl')
    .set('disabled')
    .set('tradeNo')
    .set('memberId')
    .set('orderId')
    .set('gmtCreate')
    .set('gmtModified')
    .set('remark');
}

/**
 * 提交添加
 */
MemberPaymentLogInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberPaymentLog/add", function(data){
        Feng.success("添加成功!");
        window.parent.MemberPaymentLog.table.refresh();
        MemberPaymentLogInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberPaymentLogInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
MemberPaymentLogInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberPaymentLog/update", function(data){
        Feng.success("修改成功!");
        window.parent.MemberPaymentLog.table.refresh();
        MemberPaymentLogInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberPaymentLogInfoData);
    ajax.start();
}

$(function() {

});
