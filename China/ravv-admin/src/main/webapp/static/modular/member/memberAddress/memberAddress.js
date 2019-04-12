/**
 * 会员地址管理初始化
 */
var MemberAddress = {
    id: "MemberAddressTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
MemberAddress.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        		{title: '主键', field: 'id', visible: false, align: 'center', valign: 'middle'},
            	{title: '名称', field: 'name', visible: true, align: 'center', valign: 'middle'},
            	{title: '姓', field: 'lastname', visible: true, align: 'center', valign: 'middle'},
            	{title: '父名', field: 'middleName', visible: true, align: 'center', valign: 'middle'},
            	{title: '名字', field: 'firstname', visible: true, align: 'center', valign: 'middle'},
            	{title: '地区名', field: 'areaName', visible: true, align: 'center', valign: 'middle'},
            	{title: '地区ID', field: 'areaId', visible: true, align: 'center', valign: 'middle'},
            	{title: '详细地址', field: 'address', visible: true, align: 'center', valign: 'middle'},
            	{title: '邮编', field: 'zip', visible: true, align: 'center', valign: 'middle'},
            	{title: '联系电话', field: 'mobile', visible: true, align: 'center', valign: 'middle'},
            	{title: '送货日期', field: 'deliveryDay', visible: true, align: 'center', valign: 'middle'},
            	{title: '送货时间', field: 'deliveryTime', visible: true, align: 'center', valign: 'middle'},
            	{title: '是否默认地址', field: 'defaultAddr', visible: true, align: 'center', valign: 'middle'},
            	{title: '会员ID', field: 'memberId', visible: true, align: 'center', valign: 'middle'},
            	{title: '创建时间', field: 'gmtCreate', visible: true, align: 'center', valign: 'middle'},
            	{title: '修改时间', field: 'gmtModified', visible: true, align: 'center', valign: 'middle'},
            {title: '备注', field: 'remark', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
MemberAddress.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        MemberAddress.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加会员地址
 */
MemberAddress.openAddMemberAddress = function () {
    var index = layer.open({
        type: 2,
        title: '添加会员地址',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/memberAddress/memberAddress_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看会员地址详情
 */
MemberAddress.openMemberAddressDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '会员地址详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/memberAddress/memberAddress_update/' + MemberAddress.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除会员地址
 */
MemberAddress.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/memberAddress/delete", function (data) {
            Feng.success("删除成功!");
            MemberAddress.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("memberAddressId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询会员地址列表
 */
MemberAddress.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    MemberAddress.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = MemberAddress.initColumn();
    var table = new BSTable(MemberAddress.id, "/memberAddress/list", defaultColunms);
    table.setPaginationType("client");
    MemberAddress.table = table.init();
});
