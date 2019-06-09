/**
 * loa通用配置
 * @type {{}}
 */
var config = {
    logEnable: true,    // 日志开关
    layui: {    // layui配置
        table: {
            cellMinWidth: 60
            ,request: {pageName : 'current', limitName : 'size'}
            ,response : {statusName : 'code', statusCode : 200, msgName : 'msg', dataName : 'data', countName: 'total'}
            ,height: 490
            ,defaultToolbar: ['filter', 'exports', 'print']  // 表格头部右侧按钮，若不需要，直接配置空数组
            ,page: {    //开启分页
                curr : 1,
                limit : 10
            }
        }
    },
    easyui: {   // easyui配置
        treegrid: {
            pagination: true,
            pageNumber: 1,
            pageSize: 10,
            pageList: [10, 20, 30, 40, 50],
            showHeader: true,
            nowrap: true,
            animate: true,
            scrollbarSize: 18,
            scrollOnSelect: false,
            fitColumns: true,
            checkbox: true,
            checkOnSelect: true,
            emptyMsg: '无数据',
            onBeforeLoad: function (row, param) {   // 若该方法被重写，需要把下面代码也要到重写方法中
                treeGridKit.handlePageParam(param);
            }
        }
    }
};

var req = {
    status: {
        ok: 200,
        fail: 400,
        other: 333
    },
    type: {
        get: 'GET',
        post: 'POST'
    }
};

/**
 * loaApi接口
 * @type {{}}
 */
var api = {
    homeView: loaCtx + '', // loa主页地址
    login: loaCtx + 'login',
    comm: {
        selUserView: loaCtx + 'comm/selUser',
        userMenus: loaCtx + 'index/menus',  // 用户目录菜单（左侧）
        userNavMenus: loaCtx + 'index/navMenus',  // 用户导航菜单（横向）
        fileUpload: loaCtx + 'comm/fileUpload'
    },
    sys: {  // 系统模块接口
        userListData: loaCtx + 'sys/user/list/data',
        userRm: loaCtx + 'sys/user/remove/',
        userRmBatch: loaCtx + 'sys/user/removeBatch',
        userEditView: loaCtx + 'sys/user/edit',
        userAdd: loaCtx + 'sys/user/add',
        userEdit: loaCtx + 'sys/user/edit',
        userResetPwd: loaCtx + 'sys/user/resetPwd',
        userChangePwd: loaCtx + 'sys/user/changePwd',
        menuListData: loaCtx + 'sys/menu/list/data',
        menuRm: loaCtx + 'sys/menu/remove/',
        menuRmBatch: loaCtx + 'sys/menu/removeBatch',
        menuEditView: loaCtx + 'sys/menu/edit',
        menuAdd: loaCtx + 'sys/menu/add',
        menuEdit: loaCtx + 'sys/menu/edit',
        menuListTree: loaCtx + 'sys/menu/list/tree',
        menuListTreeData: loaCtx + 'sys/menu/list/treeData',
        menuRoleTree: loaCtx + 'sys/menu/role/tree',
        deptListData: loaCtx + 'sys/dept/list/data',
        deptRm: loaCtx + 'sys/dept/remove/',
        deptRmBatch: loaCtx + 'sys/dept/removeBatch',
        deptEditView: loaCtx + 'sys/dept/edit',
        deptAdd: loaCtx + 'sys/dept/add',
        deptEdit: loaCtx + 'sys/dept/edit',
        deptListTree: loaCtx + 'sys/dept/list/tree',
        getDept: loaCtx + 'sys/dept/get/',
        roleListData: loaCtx + 'sys/role/list/data',
        roleListTree: loaCtx + 'sys/role/list/tree',
        roleRm: loaCtx + 'sys/role/remove/',
        roleRmBatch: loaCtx + 'sys/role/removeBatch',
        roleEditView: loaCtx + 'sys/role/edit',
        rolePermissionView: loaCtx + 'sys/role/permission',
        roleAdd: loaCtx + 'sys/role/add',
        roleEdit: loaCtx + 'sys/role/edit',
        roleSetPermission: loaCtx + 'sys/role/set/permission',
        /*roleTreeListData: loaCtx + 'sys/roleTree/list/data',
        roleTreeRm: loaCtx + 'sys/roleTree/remove/',
        roleTreeRmBatch: loaCtx + 'sys/roleTree/removeBatch',
        roleTreeEditView: loaCtx + 'sys/roleTree/edit',
        roleTreeSave: loaCtx + 'sys/roleTree/save',
        roleTreeListTree: loaCtx + 'sys/roleTree/list/tree',*/
        userRoleView: loaCtx + 'sys/userRole/index',
        userRoleListData: loaCtx + 'sys/userRole/list/data',
        userRoleSave: loaCtx + 'sys/userRole/save',
        userRoleRmBatch: loaCtx + 'sys/userRole/removeBatch',
        userRoleCount: loaCtx + 'sys/userRole/count',
        getUserRoleNames: loaCtx + 'sys/userRole/get/roleNames/',
        logListData: loaCtx + 'sys/log/list/data'

    },
    bus: {} // 业务模块接口
};

/**
 * layui 全局配置
 */
layui.use(['layer'], function () {
    var layer = layui.layer;
    layer.config({
        // anim: 1, //默认动画风格
        // skin: 'layui-layer-molv' //默认皮肤
    });
});

/**
 * ajax 全局拦截器
 */
$.ajaxSetup({
    contentType:"application/x-www-form-urlencoded;charset=utf-8",
    complete:function(xhr, textStatus){
        // 通过XMLHttpRequest取得响应头，sessionstatus，
        var sessionStatus = xhr.getResponseHeader("session-status");
        log('sessionStatus=', sessionStatus);
        if(sessionStatus === "timeout"){
            // 如果超时就处理 ，指定要跳转的页面(比如登陆页)
            layer.confirm('未登录或登录超时，是否重新登录?', function () {
                parent.window.location.replace(api.login);
            });
        }
    },
    // type: req.type.post,
    error: function(jqXHR, textStatus, errorThrown) {
        var errorMsg = jqXHR.responseJSON.message;
        log('errorMsg=', errorMsg);
        switch (jqXHR.status) {
            case(401):
                alert("未登录");
                break;
            case(403):
                alert("权限不足。403");
                break;
            case 404:
                alert('操作失败，没有此服务。404');
                break;
            case(408):
                alert("请求超时");
                break;
            case 500:
                alert('服务器错误(500)：' + errorMsg);
                break;
            case 504:
                alert('操作失败，服务器没有响应。504');
                break;
            default:
                alert("未知错误：" + errorMsg);
        }
    },
    success: function(r) {},
    statusCode:{
        200: function(r) {
            // log('statusCode 200 r=', r);
        }
    },
    beforeSend: function(jqXHR) {
        // log('beforeSend = ', jqXHR);
    }
});
