/**
 * PAM_会员登录日志管理初始化
 */
var PamLoginLog = {
    id: "PamLoginLogTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
PamLoginLog.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        		{title: '主键', field: 'id', visible: false, align: 'center', valign: 'middle'},
            	{title: '日志名称', field: 'logname', visible: true, align: 'center', valign: 'middle'},
            	{title: '是否执行成功', field: 'succeed', visible: true, align: 'center', valign: 'middle'},
            	{title: '具体消息', field: 'message', visible: true, align: 'center', valign: 'middle'},
            	{title: '登录ip', field: 'ip', visible: true, align: 'center', valign: 'middle'},
            	{title: '会员ID', field: 'memberId', visible: true, align: 'center', valign: 'middle'},
            {title: '登录账号ID', field: 'pamMemberId', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
PamLoginLog.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        PamLoginLog.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加PAM_会员登录日志
 */
PamLoginLog.openAddPamLoginLog = function () {
    var index = layer.open({
        type: 2,
        title: '添加PAM_会员登录日志',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/pamLoginLog/pamLoginLog_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看PAM_会员登录日志详情
 */
PamLoginLog.openPamLoginLogDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: 'PAM_会员登录日志详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/pamLoginLog/pamLoginLog_update/' + PamLoginLog.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除PAM_会员登录日志
 */
PamLoginLog.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/pamLoginLog/delete", function (data) {
            Feng.success("删除成功!");
            PamLoginLog.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("pamLoginLogId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询PAM_会员登录日志列表
 */
PamLoginLog.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    PamLoginLog.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = PamLoginLog.initColumn();
    var table = new BSTable(PamLoginLog.id, "/pamLoginLog/list", defaultColunms);
    table.setPaginationType("client");
    PamLoginLog.table = table.init();
});
