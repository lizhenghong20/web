/**
 * 初始化PAM_会员登录日志详情对话框
 */
var PamLoginLogInfoDlg = {
    pamLoginLogInfoData : {}
};

/**
 * 清除数据
 */
PamLoginLogInfoDlg.clearData = function() {
    this.pamLoginLogInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PamLoginLogInfoDlg.set = function(key, val) {
    this.pamLoginLogInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PamLoginLogInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
PamLoginLogInfoDlg.close = function() {
    parent.layer.close(window.parent.PamLoginLog.layerIndex);
}

/**
 * 收集数据
 */
PamLoginLogInfoDlg.collectData = function() {
    this
    .set('id')
    .set('logname')
    .set('succeed')
    .set('message')
    .set('ip')
    .set('memberId')
    .set('pamMemberId');
}

/**
 * 提交添加
 */
PamLoginLogInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/pamLoginLog/add", function(data){
        Feng.success("添加成功!");
        window.parent.PamLoginLog.table.refresh();
        PamLoginLogInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.pamLoginLogInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
PamLoginLogInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/pamLoginLog/update", function(data){
        Feng.success("修改成功!");
        window.parent.PamLoginLog.table.refresh();
        PamLoginLogInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.pamLoginLogInfoData);
    ajax.start();
}

$(function() {

});
