/**
 * 初始化会员站内信详情对话框
 */
var MemberMsgInfoDlg = {
    memberMsgInfoData : {}
};

/**
 * 清除数据
 */
MemberMsgInfoDlg.clearData = function() {
    this.memberMsgInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberMsgInfoDlg.set = function(key, val) {
    this.memberMsgInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberMsgInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MemberMsgInfoDlg.close = function() {
    parent.layer.close(window.parent.MemberMsg.layerIndex);
}

/**
 * 收集数据
 */
MemberMsgInfoDlg.collectData = function() {
    this
    .set('id')
    .set('fromUname')
    .set('toUname')
    .set('toTime')
    .set('subject')
    .set('content')
    .set('read')
    .set('keepUnread')
    .set('hasStar')
    .set('hasSent')
    .set('toId')
    .set('fromId')
    .set('forId')
    .set('fromType')
    .set('orderId')
    .set('gmtCreate')
    .set('gmtModified')
    .set('remark');
}

/**
 * 提交添加
 */
MemberMsgInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberMsg/add", function(data){
        Feng.success("添加成功!");
        window.parent.MemberMsg.table.refresh();
        MemberMsgInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberMsgInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
MemberMsgInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberMsg/update", function(data){
        Feng.success("修改成功!");
        window.parent.MemberMsg.table.refresh();
        MemberMsgInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberMsgInfoData);
    ajax.start();
}

$(function() {

});
