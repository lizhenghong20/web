/**
 * 身份证明管理初始化
 */
var MemberIdcard = {
    id: "MemberIdcardTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
MemberIdcard.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        		{title: '等同于会员ID', field: 'id', visible: false, align: 'center', valign: 'middle'},
            	{title: '身份证姓名', field: 'idcardName', visible: true, align: 'center', valign: 'middle'},
            	{title: '身份证号码', field: 'idcardNumber', visible: true, align: 'center', valign: 'middle'},
            	{title: '身份证正面', field: 'idcardFrontUrl', visible: true, align: 'center', valign: 'middle'},
            	{title: '身份证反面', field: 'idcardBackUrl', visible: true, align: 'center', valign: 'middle'},
            	{title: '会员ID', field: 'memberId', visible: true, align: 'center', valign: 'middle'},
            	{title: '创建时间', field: 'gmtCreate', visible: true, align: 'center', valign: 'middle'},
            	{title: '修改时间', field: 'gmtModified', visible: true, align: 'center', valign: 'middle'},
            {title: '备注', field: 'remark', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
MemberIdcard.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        MemberIdcard.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加身份证明
 */
MemberIdcard.openAddMemberIdcard = function () {
    var index = layer.open({
        type: 2,
        title: '添加身份证明',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/memberIdcard/memberIdcard_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看身份证明详情
 */
MemberIdcard.openMemberIdcardDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '身份证明详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/memberIdcard/memberIdcard_update/' + MemberIdcard.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除身份证明
 */
MemberIdcard.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/memberIdcard/delete", function (data) {
            Feng.success("删除成功!");
            MemberIdcard.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("memberIdcardId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询身份证明列表
 */
MemberIdcard.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    MemberIdcard.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = MemberIdcard.initColumn();
    var table = new BSTable(MemberIdcard.id, "/memberIdcard/list", defaultColunms);
    table.setPaginationType("client");
    MemberIdcard.table = table.init();
});
