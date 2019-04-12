/**
 * 系统参数管理初始化
 */
var Param = {
    id: "ParamTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Param.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        		{title: '主键', field: 'id', visible: false, align: 'center', valign: 'middle'},
            	{title: '参数编码', field: 'paramKey', visible: true, align: 'center', valign: 'middle'},
            	{title: '参数值', field: 'paramValue', visible: true, align: 'center', valign: 'middle'},
            {title: '备注', field: 'remark', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Param.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Param.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加系统参数
 */
Param.openAddParam = function () {
    var index = layer.open({
        type: 2,
        title: '添加系统参数',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/param/param_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看系统参数详情
 */
Param.openParamDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '系统参数详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/param/param_update/' + Param.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除系统参数
 */
Param.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/param/delete", function (data) {
            Feng.success("删除成功!");
            Param.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("paramId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询系统参数列表
 */
Param.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Param.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Param.initColumn();
    var table = new BSTable(Param.id, "/param/list", defaultColunms);
    table.setPaginationType("client");
    Param.table = table.init();
});
