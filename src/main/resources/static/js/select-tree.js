//id div 的id，isMultiple 是否多选，chkboxType 多选框类型{"Y": "ps", "N": "s"} 详细请看ztree官网
function initSelectTree(id, setting, zNodes) {
    setting = setting || {
        check: {
            enable: false,
            chkboxType: {"Y": "ps", "N": "s"}
        },
        callback: {
            onClick: onClick,
            onCheck: onCheck
        }
    };
    if (!setting.view || $isEmpty(setting.view)) {
        setting.view = {showTitle: true};
    }
    if (!setting.data || $isEmpty(setting.data)) {
        setting.data = {simpleData: {enable: true, idKey: "id", pIdKey: "parentId", rootPId: 0}};
    }
    if (!setting.callback) {
        setting.callback = {};
        setting.callback.onClick = onClick;
        setting.callback.onCheck = onCheck;
    } else {
        if (!setting.callback.onCheck) {
            setting.callback.onCheck = onCheck;
        }
        if (!setting.callback.onClick) {
            setting.callback.onClick = onClick;
        }
    }

    if (!zNodes) {
        zNodes = [{id:0, name:"root", isParent:true}];
    }

    var $objEl = $("#" + id);
    var tVal = $objEl.val();    // 默认选中值
    var tShowName = $objEl.attr('data-name') || '';    // 选中显示值
    var html = '<div class = "layui-select-title" >' +
        '<input id="' + id + 'Show"' + 'type = "text" placeholder = "请选择" value = "' + tShowName + '" class = "layui-input" readonly>' +
        '<i class= "layui-edge" ></i>' +
        '</div>';

    $objEl.after('<div id="' + id + '" class="layui-form-select select-tree"></div>');
    var $targetEl = $objEl.next();
    $objEl.remove();
    $targetEl.append(html);
    $targetEl.parent().append('<div class="tree-content scrollbar">' +
        '<input hidden id="' + id + 'Hide" value="' + tVal + '" ' +
        'name="' + id + '">' +
        '<ul id="' + id + 'Tree" class="ztree scrollbar" style="margin-top:0;"></ul>' +
        '</div>');
    $targetEl.bind("click", function () {
        if ($(this).parent().find(".tree-content").css("display") !== "none") {
            hideMenu()
        } else {
            $(this).addClass("layui-form-selected");
            var Offset = $(this).offset();
            var width = $(this).width() - 2;
            $(this).parent().find(".tree-content").css({
                left: Offset.left + "px",
                top: Offset.top + $(this).height() + "px"
            }).slideDown("fast");
            $(this).parent().find(".tree-content").css({
                width: width
            });
            $("body").bind("mousedown", onBodyDown);
        }
    });
    return $.fn.zTree.init($("#" + id + "Tree"), setting, zNodes);
}

function beforeClick(treeId, treeNode) {
    var check = (treeNode && !treeNode.isParent);
    if (!check) alert("只能选择城市...");
    return check;
}

function onClick(event, treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj(treeId);
    if (zTree.setting.check.enable == true) {
        zTree.checkNode(treeNode, !treeNode.checked, false)
        assignment(treeId, zTree.getCheckedNodes());
    } else {
        assignment(treeId, zTree.getSelectedNodes());
        hideMenu();
    }
}

function onCheck(event, treeId, treeNode) {
    var zTree = $.fn.zTree.getZTreeObj(treeId);
    assignment(treeId, zTree.getCheckedNodes());
}

function hideMenu() {
    $(".select-tree").removeClass("layui-form-selected");
    $(".tree-content").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);
}

function assignment(treeId, nodes) {
    var names = "";
    var ids = "";
    for (var i = 0, l = nodes.length; i < l; i++) {
        names += nodes[i].name + ",";
        ids += nodes[i].id + ",";
    }
    if (names.length > 0) {
        names = names.substring(0, names.length - 1);
        ids = ids.substring(0, ids.length - 1);
    }
    treeId = treeId.substring(0, treeId.length - 4);
    $("#" + treeId + "Show").attr("value", names);
    $("#" + treeId + "Show").attr("title", names);
    $("#" + treeId + "Hide").attr("value", ids);
}

function onBodyDown(event) {
    if ($(event.target).parents(".tree-content").html() == null) {
        hideMenu();
    }
}