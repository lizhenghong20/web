/**
 * 会员银行卡管理初始化
 */
var MemberBank = {
    id: "MemberBankTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
MemberBank.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        		{title: '银行卡ID', field: 'id', visible: false, align: 'center', valign: 'middle'},
            	{title: '会员ID', field: 'memberId', visible: true, align: 'center', valign: 'middle'},
            	{title: '银行名称', field: 'bankName', visible: true, align: 'center', valign: 'middle'},
            	{title: '银行卡类型', field: 'bankType', visible: true, align: 'center', valign: 'middle'},
            	{title: '银行卡号', field: 'cardNumber', visible: true, align: 'center', valign: 'middle'},
            	{title: '绑定时间', field: 'bindTime', visible: true, align: 'center', valign: 'middle'},
            	{title: '排序', field: 'sequence', visible: true, align: 'center', valign: 'middle'},
            	{title: '创建时间', field: 'gmtCreate', visible: true, align: 'center', valign: 'middle'},
            	{title: '修改时间', field: 'gmtModified', visible: true, align: 'center', valign: 'middle'},
            {title: '备注', field: 'remark', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
MemberBank.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        MemberBank.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加会员银行卡
 */
MemberBank.openAddMemberBank = function () {
    var index = layer.open({
        type: 2,
        title: '添加会员银行卡',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/memberBank/memberBank_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看会员银行卡详情
 */
MemberBank.openMemberBankDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '会员银行卡详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/memberBank/memberBank_update/' + MemberBank.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除会员银行卡
 */
MemberBank.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/memberBank/delete", function (data) {
            Feng.success("删除成功!");
            MemberBank.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("memberBankId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询会员银行卡列表
 */
MemberBank.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    MemberBank.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = MemberBank.initColumn();
    var table = new BSTable(MemberBank.id, "/memberBank/list", defaultColunms);
    table.setPaginationType("client");
    MemberBank.table = table.init();
});
