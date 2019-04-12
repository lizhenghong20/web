/**
 * PAM_会员登录账号管理初始化
 */
var PamMember = {
    id: "PamMemberTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
PamMember.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        		{title: '主键', field: 'id', visible: false, align: 'center', valign: 'middle'},
            	{title: '账户类型', field: 'type', visible: true, align: 'center', valign: 'middle'},
            	{title: '登录名', field: 'account', visible: true, align: 'center', valign: 'middle'},
            	{title: '附加账号', field: 'appendAccount', visible: true, align: 'center', valign: 'middle'},
            	{title: '登录密码', field: 'password', visible: true, align: 'center', valign: 'middle'},
            	{title: '密码盐', field: 'salt', visible: true, align: 'center', valign: 'middle'},
            	{title: '启用状态', field: 'enabled', visible: true, align: 'center', valign: 'middle'},
            	{title: '登录方式', field: 'loginstyle', visible: true, align: 'center', valign: 'middle'},
            	{title: '会员ID', field: 'memberId', visible: true, align: 'center', valign: 'middle'},
            	{title: '创建时间', field: 'gmtCreate', visible: true, align: 'center', valign: 'middle'},
            	{title: '修改时间', field: 'gmtModified', visible: true, align: 'center', valign: 'middle'},
            {title: '备注', field: 'remark', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
PamMember.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        PamMember.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加PAM_会员登录账号
 */
PamMember.openAddPamMember = function () {
    var index = layer.open({
        type: 2,
        title: '添加PAM_会员登录账号',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/pamMember/pamMember_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看PAM_会员登录账号详情
 */
PamMember.openPamMemberDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: 'PAM_会员登录账号详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/pamMember/pamMember_update/' + PamMember.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除PAM_会员登录账号
 */
PamMember.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/pamMember/delete", function (data) {
            Feng.success("删除成功!");
            PamMember.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("pamMemberId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询PAM_会员登录账号列表
 */
PamMember.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    PamMember.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = PamMember.initColumn();
    var table = new BSTable(PamMember.id, "/pamMember/list", defaultColunms);
    table.setPaginationType("client");
    PamMember.table = table.init();
});
