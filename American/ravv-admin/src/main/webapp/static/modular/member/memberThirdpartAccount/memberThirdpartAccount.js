/**
 * 会员第三方账号绑定管理初始化
 */
var MemberThirdpartAccount = {
    id: "MemberThirdpartAccountTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
MemberThirdpartAccount.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        		{title: '主键', field: 'id', visible: false, align: 'center', valign: 'middle'},
            	{title: '第三方账号类型', field: 'accountType', visible: true, align: 'center', valign: 'middle'},
            	{title: '第三方账号昵称', field: 'accountNickname', visible: true, align: 'center', valign: 'middle'},
            	{title: '第三方账号', field: 'account', visible: true, align: 'center', valign: 'middle'},
            	{title: '验证状态', field: 'validateStatus', visible: true, align: 'center', valign: 'middle'},
            	{title: '是否启用', field: 'enable', visible: true, align: 'center', valign: 'middle'},
            	{title: '绑定时间', field: 'bindTime', visible: true, align: 'center', valign: 'middle'},
            	{title: '解除绑定时间', field: 'unbindTime', visible: true, align: 'center', valign: 'middle'},
            	{title: '会员ID', field: 'memberId', visible: true, align: 'center', valign: 'middle'},
            	{title: '创建时间', field: 'gmtCreate', visible: true, align: 'center', valign: 'middle'},
            	{title: '修改时间', field: 'gmtModified', visible: true, align: 'center', valign: 'middle'},
            {title: '备注', field: 'remark', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
MemberThirdpartAccount.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        MemberThirdpartAccount.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加会员第三方账号绑定
 */
MemberThirdpartAccount.openAddMemberThirdpartAccount = function () {
    var index = layer.open({
        type: 2,
        title: '添加会员第三方账号绑定',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/memberThirdpartAccount/memberThirdpartAccount_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看会员第三方账号绑定详情
 */
MemberThirdpartAccount.openMemberThirdpartAccountDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '会员第三方账号绑定详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/memberThirdpartAccount/memberThirdpartAccount_update/' + MemberThirdpartAccount.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除会员第三方账号绑定
 */
MemberThirdpartAccount.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/memberThirdpartAccount/delete", function (data) {
            Feng.success("删除成功!");
            MemberThirdpartAccount.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("memberThirdpartAccountId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询会员第三方账号绑定列表
 */
MemberThirdpartAccount.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    MemberThirdpartAccount.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = MemberThirdpartAccount.initColumn();
    var table = new BSTable(MemberThirdpartAccount.id, "/memberThirdpartAccount/list", defaultColunms);
    table.setPaginationType("client");
    MemberThirdpartAccount.table = table.init();
});
