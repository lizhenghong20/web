/**
 * 初始化会员级别详情对话框
 */
var MemberLevelInfoDlg = {
    memberLevelInfoData : {}
};

/**
 * 清除数据
 */
MemberLevelInfoDlg.clearData = function() {
    this.memberLevelInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberLevelInfoDlg.set = function(key, val) {
    this.memberLevelInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberLevelInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MemberLevelInfoDlg.close = function() {
    parent.layer.close(window.parent.MemberLevel.layerIndex);
}

/**
 * 收集数据
 */
MemberLevelInfoDlg.collectData = function() {
    this
    .set('id')
    .set('name')
    .set('sequence')
    .set('discountRate')
    .set('freeShipping')
    .set('icon')
    .set('defaultLevel')
    .set('depositFreezeTime')
    .set('deposit')
    .set('morePoint')
    .set('needPoint')
    .set('enable')
    .set('pid')
    .set('gmtCreate')
    .set('gmtModified')
    .set('remark');
}

/**
 * 提交添加
 */
MemberLevelInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberLevel/add", function(data){
        Feng.success("添加成功!");
        window.parent.MemberLevel.table.refresh();
        MemberLevelInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberLevelInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
MemberLevelInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberLevel/update", function(data){
        Feng.success("修改成功!");
        window.parent.MemberLevel.table.refresh();
        MemberLevelInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberLevelInfoData);
    ajax.start();
}

$(function() {

});
