/**
 * 初始化会员修改密码验证记录详情对话框
 */
var MemberPwdlogInfoDlg = {
    memberPwdlogInfoData : {}
};

/**
 * 清除数据
 */
MemberPwdlogInfoDlg.clearData = function() {
    this.memberPwdlogInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberPwdlogInfoDlg.set = function(key, val) {
    this.memberPwdlogInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberPwdlogInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MemberPwdlogInfoDlg.close = function() {
    parent.layer.close(window.parent.MemberPwdlog.layerIndex);
}

/**
 * 收集数据
 */
MemberPwdlogInfoDlg.collectData = function() {
    this
    .set('id')
    .set('secret')
    .set('expireTime')
    .set('used')
    .set('memberId')
    .set('gmtCreate')
    .set('gmtModified')
    .set('remark');
}

/**
 * 提交添加
 */
MemberPwdlogInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberPwdlog/add", function(data){
        Feng.success("添加成功!");
        window.parent.MemberPwdlog.table.refresh();
        MemberPwdlogInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberPwdlogInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
MemberPwdlogInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberPwdlog/update", function(data){
        Feng.success("修改成功!");
        window.parent.MemberPwdlog.table.refresh();
        MemberPwdlogInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberPwdlogInfoData);
    ajax.start();
}

$(function() {

});
