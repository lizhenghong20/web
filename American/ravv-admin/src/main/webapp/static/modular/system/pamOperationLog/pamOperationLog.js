/**
 * PAM_操作日志管理初始化
 */
var PamOperationLog = {
    id: "PamOperationLogTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
PamOperationLog.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        		{title: '主键', field: 'id', visible: false, align: 'center', valign: 'middle'},
            	{title: '日志类型', field: 'logtype', visible: true, align: 'center', valign: 'middle'},
            	{title: '日志名称', field: 'logname', visible: true, align: 'center', valign: 'middle'},
            	{title: '类名称', field: 'classname', visible: true, align: 'center', valign: 'middle'},
            	{title: '方法名称', field: 'method', visible: true, align: 'center', valign: 'middle'},
            	{title: '是否成功', field: 'succeed', visible: true, align: 'center', valign: 'middle'},
            	{title: '具体内容', field: 'message', visible: true, align: 'center', valign: 'middle'},
            	{title: '会员ID', field: 'memberId', visible: true, align: 'center', valign: 'middle'},
            	{title: '登录账号ID', field: 'pamMemberId', visible: true, align: 'center', valign: 'middle'},
            	{title: '创建时间', field: 'gmtCreate', visible: true, align: 'center', valign: 'middle'},
            	{title: '修改时间', field: 'gmtModified', visible: true, align: 'center', valign: 'middle'},
            {title: '备注', field: 'remark', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
PamOperationLog.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        PamOperationLog.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加PAM_操作日志
 */
PamOperationLog.openAddPamOperationLog = function () {
    var index = layer.open({
        type: 2,
        title: '添加PAM_操作日志',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/pamOperationLog/pamOperationLog_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看PAM_操作日志详情
 */
PamOperationLog.openPamOperationLogDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: 'PAM_操作日志详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/pamOperationLog/pamOperationLog_update/' + PamOperationLog.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除PAM_操作日志
 */
PamOperationLog.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/pamOperationLog/delete", function (data) {
            Feng.success("删除成功!");
            PamOperationLog.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("pamOperationLogId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询PAM_操作日志列表
 */
PamOperationLog.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    PamOperationLog.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = PamOperationLog.initColumn();
    var table = new BSTable(PamOperationLog.id, "/pamOperationLog/list", defaultColunms);
    table.setPaginationType("client");
    PamOperationLog.table = table.init();
});
