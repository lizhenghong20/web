/**
 * 初始化PAM_会员登录账号详情对话框
 */
var PamMemberInfoDlg = {
    pamMemberInfoData : {}
};

/**
 * 清除数据
 */
PamMemberInfoDlg.clearData = function() {
    this.pamMemberInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PamMemberInfoDlg.set = function(key, val) {
    this.pamMemberInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PamMemberInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
PamMemberInfoDlg.close = function() {
    parent.layer.close(window.parent.PamMember.layerIndex);
}

/**
 * 收集数据
 */
PamMemberInfoDlg.collectData = function() {
    this
    .set('id')
    .set('type')
    .set('account')
    .set('appendAccount')
    .set('password')
    .set('salt')
    .set('enabled')
    .set('loginstyle')
    .set('memberId')
    .set('gmtCreate')
    .set('gmtModified')
    .set('remark');
}

/**
 * 提交添加
 */
PamMemberInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/pamMember/add", function(data){
        Feng.success("添加成功!");
        window.parent.PamMember.table.refresh();
        PamMemberInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.pamMemberInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
PamMemberInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/pamMember/update", function(data){
        Feng.success("修改成功!");
        window.parent.PamMember.table.refresh();
        PamMemberInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.pamMemberInfoData);
    ajax.start();
}

$(function() {

});
