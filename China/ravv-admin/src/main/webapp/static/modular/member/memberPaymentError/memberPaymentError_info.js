/**
 * 初始化会员支付错误记录详情对话框
 */
var MemberPaymentErrorInfoDlg = {
    memberPaymentErrorInfoData : {}
};

/**
 * 清除数据
 */
MemberPaymentErrorInfoDlg.clearData = function() {
    this.memberPaymentErrorInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberPaymentErrorInfoDlg.set = function(key, val) {
    this.memberPaymentErrorInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberPaymentErrorInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MemberPaymentErrorInfoDlg.close = function() {
    parent.layer.close(window.parent.MemberPaymentError.layerIndex);
}

/**
 * 收集数据
 */
MemberPaymentErrorInfoDlg.collectData = function() {
    this
    .set('id')
    .set('errorTime')
    .set('errorNum')
    .set('type')
    .set('memberId')
    .set('paymentId')
    .set('gmtCreate')
    .set('gmtModified')
    .set('remark');
}

/**
 * 提交添加
 */
MemberPaymentErrorInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberPaymentError/add", function(data){
        Feng.success("添加成功!");
        window.parent.MemberPaymentError.table.refresh();
        MemberPaymentErrorInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberPaymentErrorInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
MemberPaymentErrorInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberPaymentError/update", function(data){
        Feng.success("修改成功!");
        window.parent.MemberPaymentError.table.refresh();
        MemberPaymentErrorInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberPaymentErrorInfoData);
    ajax.start();
}

$(function() {

});
