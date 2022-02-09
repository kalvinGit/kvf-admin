// 流程业务表单逻辑控制JS在这里写
log('这是流程业务表单逻辑控制JS');

// 例子1：控制如果申请人是Kalvin，则自动设置部门为：主管部门
let ppObj = $('input[name="proposer"]');
ppObj.bind('keyup', function (event) {
    let proposer = ppObj.val();
    if (proposer === 'Kalvin') {
        $('input[name="dept"]').val('主管部门')
    }
});
