/**
 * 初始化会员积分日志详情对话框
 */
var MemberPointLogInfoDlg = {
    memberPointLogInfoData : {}
};

/**
 * 清除数据
 */
MemberPointLogInfoDlg.clearData = function() {
    this.memberPointLogInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberPointLogInfoDlg.set = function(key, val) {
    this.memberPointLogInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberPointLogInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MemberPointLogInfoDlg.close = function() {
    parent.layer.close(window.parent.MemberPointLog.layerIndex);
}

/**
 * 收集数据
 */
MemberPointLogInfoDlg.collectData = function() {
    this
    .set('id')
    .set('oldPoint')
    .set('newPoint')
    .set('changePoint')
    .set('consumePoint')
    .set('reason')
    .set('relatedId')
    .set('operatorId')
    .set('memberId')
    .set('pointRuleId')
    .set('gmtCreate')
    .set('gmtModified')
    .set('remark');
}

/**
 * 提交添加
 */
MemberPointLogInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberPointLog/add", function(data){
        Feng.success("添加成功!");
        window.parent.MemberPointLog.table.refresh();
        MemberPointLogInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberPointLogInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
MemberPointLogInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberPointLog/update", function(data){
        Feng.success("修改成功!");
        window.parent.MemberPointLog.table.refresh();
        MemberPointLogInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberPointLogInfoData);
    ajax.start();
}

$(function() {

});
