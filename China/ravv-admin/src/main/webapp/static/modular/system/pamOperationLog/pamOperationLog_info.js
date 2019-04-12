/**
 * 初始化PAM_操作日志详情对话框
 */
var PamOperationLogInfoDlg = {
    pamOperationLogInfoData : {}
};

/**
 * 清除数据
 */
PamOperationLogInfoDlg.clearData = function() {
    this.pamOperationLogInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PamOperationLogInfoDlg.set = function(key, val) {
    this.pamOperationLogInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
PamOperationLogInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
PamOperationLogInfoDlg.close = function() {
    parent.layer.close(window.parent.PamOperationLog.layerIndex);
}

/**
 * 收集数据
 */
PamOperationLogInfoDlg.collectData = function() {
    this
    .set('id')
    .set('logtype')
    .set('logname')
    .set('classname')
    .set('method')
    .set('succeed')
    .set('message')
    .set('memberId')
    .set('pamMemberId')
    .set('gmtCreate')
    .set('gmtModified')
    .set('remark');
}

/**
 * 提交添加
 */
PamOperationLogInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/pamOperationLog/add", function(data){
        Feng.success("添加成功!");
        window.parent.PamOperationLog.table.refresh();
        PamOperationLogInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.pamOperationLogInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
PamOperationLogInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/pamOperationLog/update", function(data){
        Feng.success("修改成功!");
        window.parent.PamOperationLog.table.refresh();
        PamOperationLogInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.pamOperationLogInfoData);
    ajax.start();
}

$(function() {

});
