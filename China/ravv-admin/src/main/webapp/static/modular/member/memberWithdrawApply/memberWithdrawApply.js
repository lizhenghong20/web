/**
 * 提现申请管理初始化
 */
var MemberWithdrawApply = {
    id: "MemberWithdrawApplyTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
MemberWithdrawApply.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        		{title: '提现申请ID', field: 'id', visible: false, align: 'center', valign: 'middle'},
            	{title: '会员ID', field: 'memberId', visible: true, align: 'center', valign: 'middle'},
            	{title: '银行卡ID', field: 'bankId', visible: true, align: 'center', valign: 'middle'},
            	{title: '银行名称', field: 'bankName', visible: true, align: 'center', valign: 'middle'},
            	{title: '银行卡类型', field: 'bankType', visible: true, align: 'center', valign: 'middle'},
            	{title: '银行卡号', field: 'cardNumber', visible: true, align: 'center', valign: 'middle'},
            	{title: '申请编号', field: 'applyNo', visible: true, align: 'center', valign: 'middle'},
            	{title: '申请提现金额', field: 'applyFee', visible: true, align: 'center', valign: 'middle'},
            	{title: '手续费', field: 'paycost', visible: true, align: 'center', valign: 'middle'},
            	{title: '手续费百分比', field: 'percentage', visible: true, align: 'center', valign: 'middle'},
            	{title: '申请时间', field: 'applyTime', visible: true, align: 'center', valign: 'middle'},
            	{title: '审核时间', field: 'checkTime', visible: true, align: 'center', valign: 'middle'},
            	{title: '审核人ID', field: 'checkerId', visible: true, align: 'center', valign: 'middle'},
            	{title: '审核人姓名', field: 'checkerName', visible: true, align: 'center', valign: 'middle'},
            	{title: '完成提现时间', field: 'withdrawTime', visible: true, align: 'center', valign: 'middle'},
            	{title: '创建时间', field: 'gmtCreate', visible: true, align: 'center', valign: 'middle'},
            	{title: '修改时间', field: 'gmtModified', visible: true, align: 'center', valign: 'middle'},
            {title: '备注', field: 'remark', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
MemberWithdrawApply.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        MemberWithdrawApply.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加提现申请
 */
MemberWithdrawApply.openAddMemberWithdrawApply = function () {
    var index = layer.open({
        type: 2,
        title: '添加提现申请',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/memberWithdrawApply/memberWithdrawApply_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看提现申请详情
 */
MemberWithdrawApply.openMemberWithdrawApplyDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '提现申请详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/memberWithdrawApply/memberWithdrawApply_update/' + MemberWithdrawApply.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除提现申请
 */
MemberWithdrawApply.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/memberWithdrawApply/delete", function (data) {
            Feng.success("删除成功!");
            MemberWithdrawApply.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("memberWithdrawApplyId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询提现申请列表
 */
MemberWithdrawApply.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    MemberWithdrawApply.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = MemberWithdrawApply.initColumn();
    var table = new BSTable(MemberWithdrawApply.id, "/memberWithdrawApply/list", defaultColunms);
    table.setPaginationType("client");
    MemberWithdrawApply.table = table.init();
});
