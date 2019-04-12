/**
 * 会员修改密码验证记录管理初始化
 */
var MemberPwdlog = {
    id: "MemberPwdlogTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
MemberPwdlog.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        		{title: '主键', field: 'id', visible: false, align: 'center', valign: 'middle'},
            	{title: '加密串', field: 'secret', visible: true, align: 'center', valign: 'middle'},
            	{title: '过期时间', field: 'expireTime', visible: true, align: 'center', valign: 'middle'},
            	{title: '是否使用', field: 'used', visible: true, align: 'center', valign: 'middle'},
            	{title: '会员ID', field: 'memberId', visible: true, align: 'center', valign: 'middle'},
            	{title: '创建时间', field: 'gmtCreate', visible: true, align: 'center', valign: 'middle'},
            	{title: '修改时间', field: 'gmtModified', visible: true, align: 'center', valign: 'middle'},
            {title: '备注', field: 'remark', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
MemberPwdlog.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        MemberPwdlog.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加会员修改密码验证记录
 */
MemberPwdlog.openAddMemberPwdlog = function () {
    var index = layer.open({
        type: 2,
        title: '添加会员修改密码验证记录',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/memberPwdlog/memberPwdlog_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看会员修改密码验证记录详情
 */
MemberPwdlog.openMemberPwdlogDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '会员修改密码验证记录详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/memberPwdlog/memberPwdlog_update/' + MemberPwdlog.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除会员修改密码验证记录
 */
MemberPwdlog.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/memberPwdlog/delete", function (data) {
            Feng.success("删除成功!");
            MemberPwdlog.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("memberPwdlogId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询会员修改密码验证记录列表
 */
MemberPwdlog.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    MemberPwdlog.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = MemberPwdlog.initColumn();
    var table = new BSTable(MemberPwdlog.id, "/memberPwdlog/list", defaultColunms);
    table.setPaginationType("client");
    MemberPwdlog.table = table.init();
});
