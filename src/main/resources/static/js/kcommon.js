/**
 * js工具
 * @type {{post: (function(*=, *=, *=): (*|string)), bindLeftTreeFlexEvent: kvfKit.bindLeftTreeFlexEvent, get: (function(*=, *=, *=): (*|string)), aGet: kvfKit.aGet, initTableSet: kvfKit.initTableSet, activeInit: kvfKit.activeInit, aPost: kvfKit.aPost, ajx: kvfKit.ajx, getUrlParam: kvfKit.getUrlParam, bindEnterEventForLayuiForm: kvfKit.bindEnterEventForLayuiForm}}
 */
var kvfKit = {
    /**
     * ajax 同步请求
     * @param url
     * @param params
     * @param method
     * @param callback
     * @returns {string}
     */
    ajx: function (url, params, method, callback) {
        var result = "";
        if (!url) {
            return result;
        }
        params = params || {};
        method = method || req.type.get;
        callback = callback || function (r) {
            result = r;
        };
        $.ajax({
            type: method,
            url: url,
            data: params,
            async: false,
            success: callback
        });
        return result;
    },
    /**
     * 异步get请求
     * @param url
     * @param params
     * @param callback
     */
    aGet: function (url, params, callback) {
        if (arguments.length === 2 && $.isFunction(arguments[1])) {
            callback = arguments[1];
            params = {};
        }
        $.ajax({
            type: req.type.get,
            url: url,
            data: params,
            success: callback
        });
    },
    /**
     * 异步post请求
     * @param url
     * @param params
     * @param callback
     */
    aPost: function (url, params, callback) {
        if (arguments.length === 2 && $.isFunction(arguments[1])) {
            callback = arguments[1];
            params = {};
        }
        $.ajax({
            type: req.type.post,
            url: url,
            // dataType : 'JSON',
            // contentType : 'application/json;charset=utf-8',
            data: params,
            success: callback
        });
    },
    /**
     * 同步get
     * @param url 请求地址：image/ascii
     * @param params 参数：{name:'kalvin'}
     * @param callback 回调函数
     */
    get: function (url, params, callback) {
        if (arguments.length === 2 && $.isFunction(arguments[1])) {
            callback = arguments[1];
            params = {};
        }
        return this.ajx(url, params, req.type.get, callback);
    },
    /**
     * 同步post
     * @param url 请求地址：image/ascii
     * @param params 参数：{name:'kalvin'}
     * @param callback 回调函数
     */
    post: function (url, params, callback) {
        if (arguments.length === 2 && $.isFunction(arguments[1])) {
            callback = arguments[1];
            params = {};
        }
        return this.ajx(url, params, req.type.post, callback);
    },

    /**
     * 结合data-event 事件属性使用
     * @param active 事件数组
     */
    activeInit: function (active) {
        $('*[data-event]').click(function () {
            var elem = $(this),
                event = elem.attr('data-event');
            typeof active[event] === 'function' && active[event].apply(this);
        });
    },
    initTableSet: function(table) {
        table.set({
            cellMinWidth: 60
            ,request: {pageName : 'current', limitName : 'size'}
            ,response : {statusName : 'code', statusCode : 200, msgName : 'msg', dataName : 'data', countName: 'total'}
            ,height: 472
            ,page: {    //开启分页
                curr : 1,
                limit : 10
            }
        });
    },
    /**
     * 内容块左侧树伸缩事件
     * @param tableIns table实例（主要为了解决左侧树收缩时表格不自适应问题）
     */
    bindLeftTreeFlexEvent: function (tableIns) {
        // 树菜单伸缩事件
        var windowHeight = $(window).height();

        var isShowTree = true, isMbShowTree = false, isPc = true;
        var $kPageEl = $('#kPage');
        var $kContentLeftTreeCardEl = $('#kContentLeftTreeCard');
        var $kContentLeftTreeColEl = $('#kContentLeftTreeCol');
        var $kMainContentColEl = $('#kMainContentCol');
        $kContentLeftTreeCardEl.find('ul.ztree').height(windowHeight - 65);

        autoLeftTree();

        // 左侧树窗口变化事件
        $(window).resize(function () {
            autoLeftTree();
        });

        function autoLeftTree() {
            var windowWidth = $(window).width();    // 974
            if (windowWidth < 975) {
                $kContentLeftTreeColEl.removeAttr('style');
                $kContentLeftTreeCardEl.find('i').removeClass('fa-angle-double-left');
                $kContentLeftTreeCardEl.find('i').removeClass('fa-angle-double-right');
                $kContentLeftTreeCardEl.find('i').addClass('fa-angle-double-down');
                $kMainContentColEl.removeAttr('style');
                isPc = false;
            } else {
                $kContentLeftTreeColEl.find('ul.ztree').height(windowHeight - 65);
                $kContentLeftTreeCardEl.find('i').removeClass('fa-angle-double-down');
                $kContentLeftTreeCardEl.find('i').removeClass('fa-angle-double-up');
                $kContentLeftTreeCardEl.find('i').addClass('fa-angle-double-left');
                isPc = true;
            }
        }

        $kContentLeftTreeCardEl.find('i').click(function () {
            if (isPc) {
                if (isShowTree) {
                    $kContentLeftTreeColEl.width(24);
                    // $kContentLeftTreeColEl.animate({width: 24}, 'fast');
                    $(this).removeClass('fa-angle-double-left');
                    $(this).addClass('fa-angle-double-right');
                    $kMainContentColEl.attr('style', 'width:97%');
                    // $kMainContentColEl.animate({width: '97%'}, 'fast');
                    $kContentLeftTreeColEl.find('ul.ztree').attr('style', 'display:none');
                    isShowTree = false;
                } else {
                    $kContentLeftTreeColEl.removeAttr('style');
                    $(this).removeClass('fa-angle-double-right');
                    $(this).addClass('fa-angle-double-left');//
                    $kMainContentColEl.removeAttr('style');
                    $kContentLeftTreeColEl.find('ul.ztree').removeAttr('style');
                    $kContentLeftTreeColEl.find('ul.ztree').height(windowHeight - 65);
                    isShowTree = true;
                }
                tableIns.reload();
            } else {
                if (isMbShowTree) {
                    $kContentLeftTreeColEl.find('ul.ztree').height(windowHeight - 65);
                    $kPageEl.removeClass('kvf-flex');
                    $(this).removeClass('fa-angle-double-up');
                    $(this).addClass('fa-angle-double-down');
                    isMbShowTree = false;
                } else {
                    $kContentLeftTreeColEl.find('ul.ztree').height('auto');
                    $kPageEl.addClass('kvf-flex');
                    $(this).removeClass('fa-angle-double-down');
                    $(this).addClass('fa-angle-double-up');
                    isMbShowTree = true;
                }
            }
        });
    },
    /**
     * 获取url参数值
     * @param name
     * @returns {string|null}
     */
    getUrlParam: function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var l = decodeURI(window.location.search);  // 解决中文乱码问题
        var r = l.substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    },

    /**
     * 获取form 表单json格式内容
     * @param form
     */
    getFormJson: function (form) {
        var json = {};
        var sArr = $(form).serializeArray();
        sArr.forEach(function (item) {
            if (item.value) {
                if (json.hasOwnProperty(item.name)) {
                    json[item.name] += ',' + item.value;
                } else {
                    json[item.name] = item.value;
                }
            } else {
                if (json.hasOwnProperty(item.name)) {
                    json[item.name] += ',';
                } else {
                    json[item.name] = '';
                }
            }
        });
        return json;
    },

    /**
     * 为form表单绑定回车事件，并实现触发提交表单事件
     */
    bindEnterEventForLayuiForm: function () {
        $('.layui-form').on('keyup', function (e) {
            if (e.keyCode === 13) {
                $(this).find('button[lay-submit]').trigger('click');
            }
        });
    },
    /**
     * 提示信息
     * @param msg 提示内容信息
     * @param iconCode 图标代码
     * @param isShake  是否抖动。默认false
     * @returns {*}
     */
    layMsg: function (msg, iconCode, isShake) {
        var options = isShake ? {icon: iconCode, anim: 6} : {icon: iconCode};
        return layer.msg(msg, options);
    },
    sucessMsg: function (msg, isShake) {
        return this.layMsg(msg, 1, isShake);
    },
    errorMsg: function (msg, isShake) {
        return this.layMsg(msg, 2, isShake);
    },
    warningMsg: function (msg, isShake) {
        return this.layMsg(msg, 0, isShake);
    },
    doubtMsg: function (msg, isShake) {
        return this.layMsg(msg, 3, isShake);
    },
    unhappyMsg: function (msg, isShake) {
        return this.layMsg(msg, 5, isShake);
    },
    happyMsg: function (msg, isShake) {
        return this.layMsg(msg, 6, isShake);
    },
    renderTpl: function (tplId, data) {
        var html = '';
        layui.use('laytpl', function (laytpl) {
            html = laytpl($('#' + tplId).html()).render(data);
        });
        return html;
    },
    underlineToHump: function (str) {
        var re=/_(\w)/g;
        return str.replace(re, function () {
            return arguments[1].toUpperCase();
        });
    },
    humpToUnderline: function (str) {
        var temp = str.replace(/[A-Z]/g, function (match) {
            return "_" + match.toLowerCase();
        });
        if(temp.slice(0,1) === '_'){ // 如果首字母是大写，执行replace时会多一个_，这里需要去掉
            temp = temp.slice(1);
        }
        return temp;
    },
    replaceAll: function (str, preRepStr, afterRepStr) {
        return str.replace(new RegExp(preRepStr, "gm"), afterRepStr)
    }

};

/**
 * treeGrid 工具方法
 * @type {{setPager: treeGridKit.setPager, reload: treeGridKit.reload, fitRightFrozenCol: treeGridKit.fitRightFrozenCol, handlePageParam: treeGridKit.handlePageParam}}
 */
var treeGridKit = {

    handlePageParam: function (param) {
        param.current = param.page;
        param.size = param.rows;
        delete param.page;
        delete param.rows;
        if (param.id) {
            param.size = -1;
        }
    },
    setPager: function (tg, options) {
        var p = $(tg).treegrid('getPager');
        $(p).pagination($.extend({
            beforePageText: '第',    // 页数文本框前显示的汉字
            afterPageText: '页    共 {pages} 页',
            displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录'
        }, options || {}));
    },
    /**
     * treegrid冻结右侧列自适应
     * @param tg
     */
    fitRightFrozenCol: function (tg) {
        $(tg).datagrid('resize', {
            width: function() {return document.body.clientWidth;}
        });
        var $datagridView1El = $('.datagrid-view1');
        var $datagridView2El = $('.datagrid-view2');
        var dgvW = $('.datagrid-view').width();
        var dgv1W = $datagridView1El.width();
        $datagridView1El.width(dgv1W + 18 + 14);
        $datagridView2El.width(dgvW - (dgv1W + 18 + 16));

        var $datagridView1BodyEl = $('.datagrid-view1 .datagrid-body');
        var databtableH = $datagridView1BodyEl.find('.datagrid-btable').height();
        $datagridView1BodyEl.width($datagridView1BodyEl.width() + 49);
        if (databtableH) {
            $datagridView1El.height(databtableH + 40);
        } else {
            $datagridView1El.height(40);
        }
    },
    reload: function (tg, p) {
        $(tg).treegrid('reload', p || {});
    }
};

/**
 * 日志输出
 */
var log = function () {
    if (config.logEnable) {
        var len = arguments.length;
        if (len === 1) {
            console.log(arguments[0]);
        }
        if (len === 2) {
            console.log(arguments[0], arguments[1]);
        }
    }
};

/**
 * 错误日志输出
 */
var error = function () {
    var len = arguments.length;
    if (len === 1) {
        console.error(arguments[0]);
    }
    if (len === 2) {
        console.error(arguments[0], arguments[1]);
    }
};

/**
 * 自定义stringBuilder
 * @constructor
 */
var StringBuilder =  function() {
    this._stringArray = [];
};

StringBuilder.prototype.append = function(str) {
    this._stringArray.push(str);
};

StringBuilder.prototype.length = function() {
    return this._stringArray.length;
};

StringBuilder.prototype.toString = function() {
    return this._stringArray.join("");
};

/**
 * 自定义startWith
 * @param str
 * @returns {boolean}
 */
String.prototype.startWith = function(str) {
    var reg = new RegExp("^"+str);
    return reg.test(this);
};

/**
 * 自定义endWith
 * @param str
 * @returns {boolean}
 */
String.prototype.endWith = function(str) {
    var reg = new RegExp(str+"$");
    return reg.test(this);
};

/**
 * 扩展 日期格式化
 * @param format
 * @returns {string | void}
 */
Date.prototype.format = function(format) {
    /*
    * 使用例子:format="yyyy-MM-dd hh:mm:ss";
    */
    var o = {
        "M+" : this.getMonth() + 1, // month
        "d+" : this.getDate(), // day
        "h+" : this.getHours(), // hour
        "m+" : this.getMinutes(), // minute
        "s+" : this.getSeconds(), // second
        "q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
        "S" : this.getMilliseconds()    // millisecond
    };

    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4
            - RegExp.$1.length));
    }

    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1
                ? o[k]
                : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
};
