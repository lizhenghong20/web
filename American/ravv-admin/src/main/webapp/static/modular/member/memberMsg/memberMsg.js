/**
 * 会员站内信管理初始化
 */
var MemberMsg = {
    id: "MemberMsgTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
MemberMsg.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        		{title: '主键', field: 'id', visible: false, align: 'center', valign: 'middle'},
            	{title: '发信者名称', field: 'fromUname', visible: true, align: 'center', valign: 'middle'},
            	{title: '收信者名称', field: 'toUname', visible: true, align: 'center', valign: 'middle'},
            	{title: '发送时间', field: 'toTime', visible: true, align: 'center', valign: 'middle'},
            	{title: '主题', field: 'subject', visible: true, align: 'center', valign: 'middle'},
            	{title: '内容', field: 'content', visible: true, align: 'center', valign: 'middle'},
            	{title: '是否已读', field: 'read', visible: true, align: 'center', valign: 'middle'},
            	{title: '标为未读', field: 'keepUnread', visible: true, align: 'center', valign: 'middle'},
            	{title: '是否标星', field: 'hasStar', visible: true, align: 'center', valign: 'middle'},
            	{title: '是否发送', field: 'hasSent', visible: true, align: 'center', valign: 'middle'},
            	{title: '收信者ID', field: 'toId', visible: true, align: 'center', valign: 'middle'},
            	{title: '发信者ID', field: 'fromId', visible: true, align: 'center', valign: 'middle'},
            	{title: '目标ID', field: 'forId', visible: true, align: 'center', valign: 'middle'},
            	{title: '发信者类型', field: 'fromType', visible: true, align: 'center', valign: 'middle'},
            	{title: '订单ID', field: 'orderId', visible: true, align: 'center', valign: 'middle'},
            	{title: '创建时间', field: 'gmtCreate', visible: true, align: 'center', valign: 'middle'},
            	{title: '修改时间', field: 'gmtModified', visible: true, align: 'center', valign: 'middle'},
            {title: '备注', field: 'remark', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
MemberMsg.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        MemberMsg.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加会员站内信
 */
MemberMsg.openAddMemberMsg = function () {
    var index = layer.open({
        type: 2,
        title: '添加会员站内信',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/memberMsg/memberMsg_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看会员站内信详情
 */
MemberMsg.openMemberMsgDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '会员站内信详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/memberMsg/memberMsg_update/' + MemberMsg.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除会员站内信
 */
MemberMsg.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/memberMsg/delete", function (data) {
            Feng.success("删除成功!");
            MemberMsg.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("memberMsgId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询会员站内信列表
 */
MemberMsg.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    MemberMsg.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = MemberMsg.initColumn();
    var table = new BSTable(MemberMsg.id, "/memberMsg/list", defaultColunms);
    table.setPaginationType("client");
    MemberMsg.table = table.init();
});
