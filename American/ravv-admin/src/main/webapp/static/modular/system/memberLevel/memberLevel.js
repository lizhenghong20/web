/**
 * 会员等级管理初始化
 */
var MemberLevel = {
    id: "MemberLevelTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
MemberLevel.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        		{title: '主键', field: 'id', visible: false, align: 'center', valign: 'middle'},
            	{title: '等级名称', field: 'name', visible: true, align: 'center', valign: 'middle'},
            	{title: '排序', field: 'sequence', visible: true, align: 'center', valign: 'middle'},
            	{title: '折扣率', field: 'discountRate', visible: true, align: 'center', valign: 'middle'},
            	{title: '是否免邮费', field: 'freeShipping', visible: true, align: 'center', valign: 'middle'},
            	{title: '图标', field: 'icon', visible: true, align: 'center', valign: 'middle'},
            	{title: '前一级ID', field: 'preId', visible: true, align: 'center', valign: 'middle'},
            	{title: '默认等级', field: 'defaultLevel', visible: true, align: 'center', valign: 'middle'},
            	{title: '保证金解冻时间', field: 'depositFreezeTime', visible: true, align: 'center', valign: 'middle'},
            	{title: '保证金', field: 'deposit', visible: true, align: 'center', valign: 'middle'},
            	{title: '下一级积分', field: 'morePoint', visible: true, align: 'center', valign: 'middle'},
            	{title: '所需积分', field: 'needPoint', visible: true, align: 'center', valign: 'middle'},
            	{title: '是否启用', field: 'disabled', visible: true, align: 'center', valign: 'middle'},
            	{title: '备注', field: 'remark', visible: true, align: 'center', valign: 'middle'},
            	{title: '创建时间', field: 'gmtCreate', visible: true, align: 'center', valign: 'middle'},
            	{title: '修改时间', field: 'gmtModified', visible: true, align: 'center', valign: 'middle'},
            	{title: '位标志', field: 'options', visible: true, align: 'center', valign: 'middle'},
            {title: 'KV扩展', field: 'feature', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
MemberLevel.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        MemberLevel.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加会员等级
 */
MemberLevel.openAddMemberLevel = function () {
    var index = layer.open({
        type: 2,
        title: '添加会员等级',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/memberLevel/memberLevel_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看会员等级详情
 */
MemberLevel.openMemberLevelDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '会员等级详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/memberLevel/memberLevel_update/' + MemberLevel.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除会员等级
 */
MemberLevel.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/memberLevel/delete", function (data) {
            Feng.success("删除成功!");
            MemberLevel.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("memberLevelId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询会员等级列表
 */
MemberLevel.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    MemberLevel.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = MemberLevel.initColumn();
    var table = new BSTable(MemberLevel.id, "/memberLevel/list", defaultColunms);
    table.setPaginationType("client");
    MemberLevel.table = table.init();
});
