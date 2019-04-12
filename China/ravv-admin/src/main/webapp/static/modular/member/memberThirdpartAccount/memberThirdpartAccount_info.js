/**
 * 初始化会员第三方账号绑定详情对话框
 */
var MemberThirdpartAccountInfoDlg = {
    memberThirdpartAccountInfoData : {}
};

/**
 * 清除数据
 */
MemberThirdpartAccountInfoDlg.clearData = function() {
    this.memberThirdpartAccountInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberThirdpartAccountInfoDlg.set = function(key, val) {
    this.memberThirdpartAccountInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MemberThirdpartAccountInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MemberThirdpartAccountInfoDlg.close = function() {
    parent.layer.close(window.parent.MemberThirdpartAccount.layerIndex);
}

/**
 * 收集数据
 */
MemberThirdpartAccountInfoDlg.collectData = function() {
    this
    .set('id')
    .set('accountType')
    .set('accountNickname')
    .set('account')
    .set('validateStatus')
    .set('enable')
    .set('bindTime')
    .set('unbindTime')
    .set('memberId')
    .set('gmtCreate')
    .set('gmtModified')
    .set('remark');
}

/**
 * 提交添加
 */
MemberThirdpartAccountInfoDlg.addSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberThirdpartAccount/add", function(data){
        Feng.success("添加成功!");
        window.parent.MemberThirdpartAccount.table.refresh();
        MemberThirdpartAccountInfoDlg.close();
    },function(data){
        Feng.error("添加失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberThirdpartAccountInfoData);
    ajax.start();
}

/**
 * 提交修改
 */
MemberThirdpartAccountInfoDlg.editSubmit = function() {

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/memberThirdpartAccount/update", function(data){
        Feng.success("修改成功!");
        window.parent.MemberThirdpartAccount.table.refresh();
        MemberThirdpartAccountInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.message + "!");
    });
    ajax.set(this.memberThirdpartAccountInfoData);
    ajax.start();
}

$(function() {

});
