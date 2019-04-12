/**
 * 会员支付错误记录管理初始化
 */
var MemberPaymentError = {
    id: "MemberPaymentErrorTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
MemberPaymentError.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        		{title: '主键', field: 'id', visible: false, align: 'center', valign: 'middle'},
            	{title: '错误时间', field: 'errorTime', visible: true, align: 'center', valign: 'middle'},
            	{title: '错误计数', field: 'errorNum', visible: true, align: 'center', valign: 'middle'},
            	{title: '错误类型', field: 'type', visible: true, align: 'center', valign: 'middle'},
            	{title: '会员ID', field: 'memberId', visible: true, align: 'center', valign: 'middle'},
            	{title: '会员支付ID', field: 'paymentId', visible: true, align: 'center', valign: 'middle'},
            	{title: '创建时间', field: 'gmtCreate', visible: true, align: 'center', valign: 'middle'},
            	{title: '修改时间', field: 'gmtModified', visible: true, align: 'center', valign: 'middle'},
            {title: '备注', field: 'remark', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
MemberPaymentError.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        MemberPaymentError.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加会员支付错误记录
 */
MemberPaymentError.openAddMemberPaymentError = function () {
    var index = layer.open({
        type: 2,
        title: '添加会员支付错误记录',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/memberPaymentError/memberPaymentError_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看会员支付错误记录详情
 */
MemberPaymentError.openMemberPaymentErrorDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '会员支付错误记录详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/memberPaymentError/memberPaymentError_update/' + MemberPaymentError.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除会员支付错误记录
 */
MemberPaymentError.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/memberPaymentError/delete", function (data) {
            Feng.success("删除成功!");
            MemberPaymentError.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("memberPaymentErrorId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询会员支付错误记录列表
 */
MemberPaymentError.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    MemberPaymentError.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = MemberPaymentError.initColumn();
    var table = new BSTable(MemberPaymentError.id, "/memberPaymentError/list", defaultColunms);
    table.setPaginationType("client");
    MemberPaymentError.table = table.init();
});
