/**
 * 初始化身份证明详情对话框
 */
var MemberIdcardInfoDlg = {
    memberIdcardInfoData : {}
};

/**
 * 清除数据
 */
MemberIdcardInfoDlg.clearData = function() {
    this.memberIdcardInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberIdcardInfoDlg.set = function(key, val) {
    this.memberIdcardInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberIdcardInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MemberIdcardInfoDlg.close = function() {
    parent.layer.close(window.parent.MemberIdcard.layerIndex);
}

/**
 * 收集数据
 */
MemberIdcardInfoDlg.collectData = function() {
    this
    .set('id')
    .set('idcardName')
    .set('idcardNumber')
    .set('idcardFrontUrl')
    .set('idcardBackUrl')
    .set('memberId')
    .set('gmtCreate')
    .set('gmtModified')
    .set('remark');
}

/**
 * 提交添加
 */
MemberIdcardInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberIdcard/add", function(data){
        Feng.success("添加成功!");
        window.parent.MemberIdcard.table.refresh();
        MemberIdcardInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberIdcardInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
MemberIdcardInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberIdcard/update", function(data){
        Feng.success("修改成功!");
        window.parent.MemberIdcard.table.refresh();
        MemberIdcardInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberIdcardInfoData);
    ajax.start();
}

$(function() {

});
