/**
 * 会员支付日志管理初始化
 */
var MemberPaymentLog = {
    id: "MemberPaymentLogTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
MemberPaymentLog.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        		{title: '支付单号', field: 'id', visible: false, align: 'center', valign: 'middle'},
            	{title: '金额', field: 'money', visible: true, align: 'center', valign: 'middle'},
            	{title: '货币金额', field: 'curMoney', visible: true, align: 'center', valign: 'middle'},
            	{title: '支付状态', field: 'status', visible: true, align: 'center', valign: 'middle'},
            	{title: '付款方式', field: 'payMethod', visible: true, align: 'center', valign: 'middle'},
            	{title: '支付类型', field: 'payType', visible: true, align: 'center', valign: 'middle'},
            	{title: '支付完成时间', field: 'payedTime', visible: true, align: 'center', valign: 'middle'},
            	{title: '操作员', field: 'operatorId', visible: true, align: 'center', valign: 'middle'},
            	{title: '平台对应的账号', field: 'account', visible: true, align: 'center', valign: 'middle'},
            	{title: '银行', field: 'bank', visible: true, align: 'center', valign: 'middle'},
            	{title: '支付账户', field: 'payAccount', visible: true, align: 'center', valign: 'middle'},
            	{title: '货币编码', field: 'currencyCode', visible: true, align: 'center', valign: 'middle'},
            	{title: '手续费', field: 'paycost', visible: true, align: 'center', valign: 'middle'},
            	{title: '支付IP', field: 'ip', visible: true, align: 'center', valign: 'middle'},
            	{title: '支付开始时间', field: 'payBeginTime', visible: true, align: 'center', valign: 'middle'},
            	{title: '支付确认时间', field: 'payConfirmTime', visible: true, align: 'center', valign: 'middle'},
            	{title: '支付注释', field: 'memo', visible: true, align: 'center', valign: 'middle'},
            	{title: '返回URL', field: 'returnUrl', visible: true, align: 'center', valign: 'middle'},
            	{title: '取消该笔支付', field: 'disabled', visible: true, align: 'center', valign: 'middle'},
            	{title: '交易编号', field: 'tradeNo', visible: true, align: 'center', valign: 'middle'},
            	{title: '会员ID', field: 'memberId', visible: true, align: 'center', valign: 'middle'},
            	{title: '订单ID', field: 'orderId', visible: true, align: 'center', valign: 'middle'},
            	{title: '创建时间', field: 'gmtCreate', visible: true, align: 'center', valign: 'middle'},
            	{title: '修改时间', field: 'gmtModified', visible: true, align: 'center', valign: 'middle'},
            {title: '备注', field: 'remark', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
MemberPaymentLog.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        MemberPaymentLog.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加会员支付日志
 */
MemberPaymentLog.openAddMemberPaymentLog = function () {
    var index = layer.open({
        type: 2,
        title: '添加会员支付日志',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/memberPaymentLog/memberPaymentLog_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看会员支付日志详情
 */
MemberPaymentLog.openMemberPaymentLogDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '会员支付日志详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/memberPaymentLog/memberPaymentLog_update/' + MemberPaymentLog.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除会员支付日志
 */
MemberPaymentLog.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/memberPaymentLog/delete", function (data) {
            Feng.success("删除成功!");
            MemberPaymentLog.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("memberPaymentLogId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询会员支付日志列表
 */
MemberPaymentLog.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    MemberPaymentLog.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = MemberPaymentLog.initColumn();
    var table = new BSTable(MemberPaymentLog.id, "/memberPaymentLog/list", defaultColunms);
    table.setPaginationType("client");
    MemberPaymentLog.table = table.init();
});
