/**
 * 会员管理初始化
 */
var Member = {
    id: "MemberTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Member.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        		{title: '会员ID', field: 'id', visible: false, align: 'center', valign: 'middle'},
            	{title: '名称', field: 'name', visible: true, align: 'center', valign: 'middle'},
            	{title: '密码', field: 'password', visible: true, align: 'center', valign: 'middle'},
            	{title: '支付密码', field: 'payPassword', visible: true, align: 'center', valign: 'middle'},
            	{title: '注册IP', field: 'regIp', visible: true, align: 'center', valign: 'middle'},
            	{title: '注册时间', field: 'registerTime', visible: true, align: 'center', valign: 'middle'},
            	{title: '审核状态', field: 'status', visible: true, align: 'center', valign: 'middle'},
            	{title: '所属地区', field: 'area', visible: true, align: 'center', valign: 'middle'},
            	{title: '地址', field: 'address', visible: true, align: 'center', valign: 'middle'},
            	{title: '手机', field: 'mobile', visible: true, align: 'center', valign: 'middle'},
            	{title: '固定电话', field: 'tel', visible: true, align: 'center', valign: 'middle'},
            	{title: '邮编', field: 'zip', visible: true, align: 'center', valign: 'middle'},
            	{title: '会员类型', field: 'memberType', visible: true, align: 'center', valign: 'middle'},
            	{title: '性别', field: 'sex', visible: true, align: 'center', valign: 'middle'},
            	{title: '出生年月', field: 'birthday', visible: true, align: 'center', valign: 'middle'},
            	{title: '邮箱', field: 'email', visible: true, align: 'center', valign: 'middle'},
            	{title: '昵称', field: 'nickName', visible: true, align: 'center', valign: 'middle'},
            	{title: '姓氏', field: 'lastname', visible: true, align: 'center', valign: 'middle'},
            	{title: '副名', field: 'middleName', visible: true, align: 'center', valign: 'middle'},
            	{title: '名字', field: 'firstname', visible: true, align: 'center', valign: 'middle'},
            	{title: '喜好的货币ID', field: 'favCurrencyId', visible: true, align: 'center', valign: 'middle'},
            	{title: '喜好的货币名称', field: 'favCurrencyName', visible: true, align: 'center', valign: 'middle'},
            	{title: '喜好的语言', field: 'favLang', visible: true, align: 'center', valign: 'middle'},
            	{title: '订单数', field: 'orderNum', visible: true, align: 'center', valign: 'middle'},
            	{title: '登录次数', field: 'loginCount', visible: true, align: 'center', valign: 'middle'},
            	{title: '积分', field: 'point', visible: true, align: 'center', valign: 'middle'},
            	{title: '当前预存款', field: 'deposit', visible: true, align: 'center', valign: 'middle'},
            	{title: '账户余额', field: 'advance', visible: true, align: 'center', valign: 'middle'},
            	{title: '冻结金额', field: 'advanceFreeze', visible: true, align: 'center', valign: 'middle'},
            	{title: '冻结金额', field: 'pointFreeze', visible: true, align: 'center', valign: 'middle'},
            	{title: '推荐码', field: 'referralCode', visible: true, align: 'center', valign: 'middle'},
            	{title: '头像', field: 'avator', visible: true, align: 'center', valign: 'middle'},
            	{title: '二维码', field: 'qrcode', visible: true, align: 'center', valign: 'middle'},
            	{title: '等级ID', field: 'levelId', visible: true, align: 'center', valign: 'middle'},
            	{title: '创建时间', field: 'gmtCreate', visible: true, align: 'center', valign: 'middle'},
            	{title: '修改时间', field: 'gmtModified', visible: true, align: 'center', valign: 'middle'},
            {title: '备注', field: 'remark', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Member.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Member.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加会员
 */
Member.openAddMember = function () {
    var index = layer.open({
        type: 2,
        title: '添加会员',
        area: ['800px', '420px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/member/member_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看会员详情
 */
Member.openMemberDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '会员详情',
            area: ['800px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/member/member_update/' + Member.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除会员
 */
Member.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/member/delete", function (data) {
            Feng.success("删除成功!");
            Member.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.message + "!");
        });
        ajax.set("memberId",this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询会员列表
 */
Member.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Member.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Member.initColumn();
    var table = new BSTable(Member.id, "/member/list", defaultColunms);
    table.setPaginationType("client");
    Member.table = table.init();
});
