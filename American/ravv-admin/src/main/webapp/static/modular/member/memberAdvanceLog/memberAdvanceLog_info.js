/**
 * 初始化会员预付款日志详情对话框
 */
var MemberAdvanceLogInfoDlg = {
    memberAdvanceLogInfoData : {}
};

/**
 * 清除数据
 */
MemberAdvanceLogInfoDlg.clearData = function() {
    this.memberAdvanceLogInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberAdvanceLogInfoDlg.set = function(key, val) {
    this.memberAdvanceLogInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberAdvanceLogInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MemberAdvanceLogInfoDlg.close = function() {
    parent.layer.close(window.parent.MemberAdvanceLog.layerIndex);
}

/**
 * 收集数据
 */
MemberAdvanceLogInfoDlg.collectData = function() {
    this
    .set('id')
    .set('memberId')
    .set('orderId')
    .set('paymentId')
    .set('advanceType')
    .set('title')
    .set('money')
    .set('message')
    .set('bizTime')
    .set('paymethod')
    .set('memo')
    .set('importMoney')
    .set('explodeMoney')
    .set('memberAdvance')
    .set('enable')
    .set('frozen')
    .set('unfreezeTime')
    .set('gmtCreate')
    .set('gmtModified')
    .set('remark');
}

/**
 * 提交添加
 */
MemberAdvanceLogInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberAdvanceLog/add", function(data){
        Feng.success("添加成功!");
        window.parent.MemberAdvanceLog.table.refresh();
        MemberAdvanceLogInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberAdvanceLogInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
MemberAdvanceLogInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberAdvanceLog/update", function(data){
        Feng.success("修改成功!");
        window.parent.MemberAdvanceLog.table.refresh();
        MemberAdvanceLogInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberAdvanceLogInfoData);
    ajax.start();
}

$(function() {

});
