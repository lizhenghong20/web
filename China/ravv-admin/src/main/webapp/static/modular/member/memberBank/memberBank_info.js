/**
 * 初始化会员银行卡详情对话框
 */
var MemberBankInfoDlg = {
    memberBankInfoData : {}
};

/**
 * 清除数据
 */
MemberBankInfoDlg.clearData = function() {
    this.memberBankInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberBankInfoDlg.set = function(key, val) {
    this.memberBankInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberBankInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MemberBankInfoDlg.close = function() {
    parent.layer.close(window.parent.MemberBank.layerIndex);
}

/**
 * 收集数据
 */
MemberBankInfoDlg.collectData = function() {
    this
    .set('id')
    .set('memberId')
    .set('bankName')
    .set('bankType')
    .set('cardNumber')
    .set('bindTime')
    .set('sequence')
    .set('gmtCreate')
    .set('gmtModified')
    .set('remark');
}

/**
 * 提交添加
 */
MemberBankInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberBank/add", function(data){
        Feng.success("添加成功!");
        window.parent.MemberBank.table.refresh();
        MemberBankInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberBankInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
MemberBankInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberBank/update", function(data){
        Feng.success("修改成功!");
        window.parent.MemberBank.table.refresh();
        MemberBankInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberBankInfoData);
    ajax.start();
}

$(function() {

});
