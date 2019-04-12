/**
 * 会员预付款日志管理初始化
 */
var MemberAdvanceLog = {
    id: "MemberAdvanceLogTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
MemberAdvanceLog.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        		{title: '预存款日志ID', field: 'id', visible: false, align: 'center', valign: 'middle'},
            	{title: '会员ID', field: 'memberId', visible: true, align: 'center', valign: 'middle'},
            	{title: '订单ID', field: 'orderId', visible: true, align: 'center', valign: 'middle'},
            	{title: '会员支付ID', field: 'paymentId', visible: true, align: 'center', valign: 'middle'},
            	{title: '预付款类型', field: 'advanceType', visible: true, align: 'center', valign: 'middle'},
            	{title: '记录显示标题', field: 'title', visible: true, align: 'center', valign: 'middle'},
            	{title: '出入金额', field: 'money', visible: true, align: 'center', valign: 'middle'},
            	{title: '管理备注', field: 'message', visible: true, align: 'center', valign: 'middle'},
            	{title: '交易时间', field: 'bizTime', visible: true, align: 'center', valign: 'middle'},
            	{title: '支付方式', field: 'paymethod', visible: true, align: 'center', valign: 'middle'},
            	{title: '业务摘要', field: 'memo', visible: true, align: 'center', valign: 'middle'},
            	{title: '存入金额', field: 'importMoney', visible: true, align: 'center', valign: 'middle'},
            	{title: '支出金额', field: 'explodeMoney', visible: true, align: 'center', valign: 'middle'},
            	{title: '当前余额', field: 'memberAdvance', visible: true, align: 'center', valign: 'middle'},
            	{title: '是否有效', field: 'enable', visible: true, align: 'center', valign: 'middle'},
            	{title: '是否冻结', field: 'frozen', visible: true, align: 'center', valign: 'middle'},
            	{title: '解冻时间', field: 'unfreezeTime', visible: true, align: 'center', valign: 'middle'},
            	{title: '创建时间', field: 'gmtCreate', visible: true, align: 'center', valign: 'middle'},
            	{title: '修改时间', field: 'gmtModified', visible: true, align: 'center', valign: 'middle'},
            {title: '备注', field: 'remark', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
MemberAdvanceLog.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        MemberAdvanceLog.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加会员预付款日志
 */
MemberAdvanceLog.openAddMemberAdvanceLog = function () {
    var index = layer.open({
        type: 2,
        title: '添加会员预付款日志',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/memberAdvanceLog/memberAdvanceLog_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看会员预付款日志详情
 */
MemberAdvanceLog.openMemberAdvanceLogDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '会员预付款日志详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/memberAdvanceLog/memberAdvanceLog_update/' + MemberAdvanceLog.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除会员预付款日志
 */
MemberAdvanceLog.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/memberAdvanceLog/delete", function (data) {
            Feng.success("删除成功!");
            MemberAdvanceLog.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("memberAdvanceLogId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询会员预付款日志列表
 */
MemberAdvanceLog.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    MemberAdvanceLog.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = MemberAdvanceLog.initColumn();
    var table = new BSTable(MemberAdvanceLog.id, "/memberAdvanceLog/list", defaultColunms);
    table.setPaginationType("client");
    MemberAdvanceLog.table = table.init();
});
