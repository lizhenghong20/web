/**
 * 会员积分日志管理初始化
 */
var MemberPointLog = {
    id: "MemberPointLogTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
MemberPointLog.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        		{title: '主键', field: 'id', visible: false, align: 'center', valign: 'middle'},
            	{title: '旧积分', field: 'oldPoint', visible: true, align: 'center', valign: 'middle'},
            	{title: '新积分', field: 'newPoint', visible: true, align: 'center', valign: 'middle'},
            	{title: '改变的积分', field: 'changePoint', visible: true, align: 'center', valign: 'middle'},
            	{title: '消费的积分', field: 'consumePoint', visible: true, align: 'center', valign: 'middle'},
            	{title: '变化理由', field: 'reason', visible: true, align: 'center', valign: 'middle'},
            	{title: '关联ID', field: 'relatedId', visible: true, align: 'center', valign: 'middle'},
            	{title: '操作者ID', field: 'operatorId', visible: true, align: 'center', valign: 'middle'},
            	{title: '会员ID', field: 'memberId', visible: true, align: 'center', valign: 'middle'},
            	{title: '积分规则ID', field: 'pointRuleId', visible: true, align: 'center', valign: 'middle'},
            	{title: '创建时间', field: 'gmtCreate', visible: true, align: 'center', valign: 'middle'},
            	{title: '修改时间', field: 'gmtModified', visible: true, align: 'center', valign: 'middle'},
            {title: '备注', field: 'remark', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
MemberPointLog.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        MemberPointLog.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加会员积分日志
 */
MemberPointLog.openAddMemberPointLog = function () {
    var index = layer.open({
        type: 2,
        title: '添加会员积分日志',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/memberPointLog/memberPointLog_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看会员积分日志详情
 */
MemberPointLog.openMemberPointLogDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '会员积分日志详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/memberPointLog/memberPointLog_update/' + MemberPointLog.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除会员积分日志
 */
MemberPointLog.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/memberPointLog/delete", function (data) {
            Feng.success("删除成功!");
            MemberPointLog.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("memberPointLogId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询会员积分日志列表
 */
MemberPointLog.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    MemberPointLog.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = MemberPointLog.initColumn();
    var table = new BSTable(MemberPointLog.id, "/memberPointLog/list", defaultColunms);
    table.setPaginationType("client");
    MemberPointLog.table = table.init();
});
