/**
 * 初始化会员详情对话框
 */
var MemberInfoDlg = {
    memberInfoData : {}
};

/**
 * 清除数据
 */
MemberInfoDlg.clearData = function() {
    this.memberInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberInfoDlg.set = function(key, val) {
    this.memberInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MemberInfoDlg.close = function() {
    parent.layer.close(window.parent.Member.layerIndex);
}

/**
 * 收集数据
 */
MemberInfoDlg.collectData = function() {
    this
    .set('id')
    .set('name')
    .set('password')
    .set('payPassword')
    .set('regIp')
    .set('registerTime')
    .set('status')
    .set('area')
    .set('address')
    .set('mobile')
    .set('tel')
    .set('zip')
    .set('memberType')
    .set('sex')
    .set('birthday')
    .set('email')
    .set('nickName')
    .set('lastname')
    .set('middleName')
    .set('firstname')
    .set('favCurrencyId')
    .set('favCurrencyName')
    .set('favLang')
    .set('orderNum')
    .set('loginCount')
    .set('point')
    .set('deposit')
    .set('advance')
    .set('advanceFreeze')
    .set('pointFreeze')
    .set('referralCode')
    .set('avator')
    .set('qrcode')
    .set('levelId')
    .set('gmtCreate')
    .set('gmtModified')
    .set('remark');
}

/**
 * 提交添加
 */
MemberInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/member/add", function(data){
        Feng.success("添加成功!");
        window.parent.Member.table.refresh();
        MemberInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
MemberInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/member/update", function(data){
        Feng.success("修改成功!");
        window.parent.Member.table.refresh();
        MemberInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberInfoData);
    ajax.start();
}

$(function() {

});
