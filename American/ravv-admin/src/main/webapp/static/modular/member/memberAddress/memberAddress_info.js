/**
 * 初始化会员地址详情对话框
 */
var MemberAddressInfoDlg = {
    memberAddressInfoData : {}
};

/**
 * 清除数据
 */
MemberAddressInfoDlg.clearData = function() {
    this.memberAddressInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberAddressInfoDlg.set = function(key, val) {
    this.memberAddressInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberAddressInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MemberAddressInfoDlg.close = function() {
    parent.layer.close(window.parent.MemberAddress.layerIndex);
}

/**
 * 收集数据
 */
MemberAddressInfoDlg.collectData = function() {
    this
    .set('id')
    .set('name')
    .set('lastname')
    .set('middleName')
    .set('firstname')
    .set('areaName')
    .set('areaId')
    .set('address')
    .set('zip')
    .set('mobile')
    .set('deliveryDay')
    .set('deliveryTime')
    .set('defaultAddr')
    .set('memberId')
    .set('gmtCreate')
    .set('gmtModified')
    .set('remark');
}

/**
 * 提交添加
 */
MemberAddressInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberAddress/add", function(data){
        Feng.success("添加成功!");
        window.parent.MemberAddress.table.refresh();
        MemberAddressInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberAddressInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
MemberAddressInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberAddress/update", function(data){
        Feng.success("修改成功!");
        window.parent.MemberAddress.table.refresh();
        MemberAddressInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberAddressInfoData);
    ajax.start();
}

$(function() {

});
