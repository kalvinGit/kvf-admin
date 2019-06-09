/**
 * EasyUI for jQuery 1.8.1
 * 
 * Copyright (c) 2009-2019 www.jeasyui.com. All rights reserved.
 *
 * Licensed under the freeware license: http://www.jeasyui.com/license_freeware.php
 * To use it on other terms please contact us: info@jeasyui.com
 *
 */
(function($){
var _1=1;
function _2(_3){
var _4=$("<span class=\"radiobutton inputbox\">"+"<span class=\"radiobutton-inner\" style=\"display:none\"></span>"+"<input type=\"radio\" class=\"radiobutton-value\">"+"</span>").insertAfter(_3);
var t=$(_3);
t.addClass("radiobutton-f").hide();
var _5=t.attr("name");
if(_5){
t.removeAttr("name").attr("radiobuttonName",_5);
_4.find(".radiobutton-value").attr("name",_5);
}
return _4;
};
function _6(_7){
var _8=$.data(_7,"radiobutton");
var _9=_8.options;
var _a=_8.radiobutton;
var _b="_easyui_radiobutton_"+(++_1);
var _c=_a.find(".radiobutton-value").attr("id",_b);
_c.unbind(".radiobutton").bind("change.radiobutton",function(e){
return false;
});
if(_9.label){
if(typeof _9.label=="object"){
_8.label=$(_9.label);
_8.label.attr("for",_b);
}else{
$(_8.label).remove();
_8.label=$("<label class=\"textbox-label\"></label>").html(_9.label);
_8.label.css("textAlign",_9.labelAlign).attr("for",_b);
if(_9.labelPosition=="after"){
_8.label.insertAfter(_a);
}else{
_8.label.insertBefore(_7);
}
_8.label.removeClass("textbox-label-left textbox-label-right textbox-label-top");
_8.label.addClass("textbox-label-"+_9.labelPosition);
}
}else{
$(_8.label).remove();
}
$(_7).radiobutton("setValue",_9.value);
_d(_7,_9.checked);
_e(_7,_9.readonly);
_f(_7,_9.disabled);
};
function _10(_11){
var _12=$.data(_11,"radiobutton");
var _13=_12.options;
var _14=_12.radiobutton;
_14.unbind(".radiobutton").bind("click.radiobutton",function(){
if(!_13.disabled&&!_13.readonly){
_d(_11,true);
}
});
};
function _15(_16){
var _17=$.data(_16,"radiobutton");
var _18=_17.options;
var _19=_17.radiobutton;
_19._size(_18,_19.parent());
if(_18.label&&_18.labelPosition){
if(_18.labelPosition=="top"){
_17.label._size({width:_18.labelWidth},_19);
}else{
_17.label._size({width:_18.labelWidth,height:_19.outerHeight()},_19);
_17.label.css("lineHeight",_19.outerHeight()+"px");
}
}
};
function _d(_1a,_1b){
if(_1b){
var f=$(_1a).closest("form");
var _1c=$(_1a).attr("radiobuttonName");
f.find(".radiobutton-f[radiobuttonName=\""+_1c+"\"]").each(function(){
if(this!=_1a){
_1d(this,false);
}
});
_1d(_1a,true);
}else{
_1d(_1a,false);
}
function _1d(b,c){
var _1e=$(b).radiobutton("options");
var _1f=$(b).data("radiobutton").radiobutton;
_1f.find(".radiobutton-inner").css("display",c?"":"none");
_1f.find(".radiobutton-value")._propAttr("checked",c);
if(_1e.checked!=c){
_1e.checked=c;
_1e.onChange.call($(b)[0],c);
$(b).closest("form").trigger("_change",[$(b)[0]]);
}
};
};
function _f(_20,_21){
var _22=$.data(_20,"radiobutton");
var _23=_22.options;
var _24=_22.radiobutton;
var rv=_24.find(".radiobutton-value");
_23.disabled=_21;
if(_21){
$(_20).add(rv)._propAttr("disabled",true);
_24.addClass("radiobutton-disabled");
$(_22.label).addClass("textbox-label-disabled");
}else{
$(_20).add(rv)._propAttr("disabled",false);
_24.removeClass("radiobutton-disabled");
$(_22.label).removeClass("textbox-label-disabled");
}
};
function _e(_25,_26){
var _27=$.data(_25,"radiobutton");
var _28=_27.options;
_28.readonly=_26==undefined?true:_26;
_27.radiobutton.removeClass("radiobutton-readonly").addClass(_28.readonly?"radiobutton-readonly":"");
};
$.fn.radiobutton=function(_29,_2a){
if(typeof _29=="string"){
return $.fn.radiobutton.methods[_29](this,_2a);
}
_29=_29||{};
return this.each(function(){
var _2b=$.data(this,"radiobutton");
if(_2b){
$.extend(_2b.options,_29);
}else{
_2b=$.data(this,"radiobutton",{options:$.extend({},$.fn.radiobutton.defaults,$.fn.radiobutton.parseOptions(this),_29),radiobutton:_2(this)});
}
_2b.options.originalChecked=_2b.options.checked;
_6(this);
_10(this);
_15(this);
});
};
$.fn.radiobutton.methods={options:function(jq){
var _2c=jq.data("radiobutton");
return $.extend(_2c.options,{value:_2c.radiobutton.find(".radiobutton-value").val()});
},setValue:function(jq,_2d){
return jq.each(function(){
$(this).val(_2d);
$.data(this,"radiobutton").radiobutton.find(".radiobutton-value").val(_2d);
});
},enable:function(jq){
return jq.each(function(){
_f(this,false);
});
},disable:function(jq){
return jq.each(function(){
_f(this,true);
});
},readonly:function(jq,_2e){
return jq.each(function(){
_e(this,_2e);
});
},check:function(jq){
return jq.each(function(){
_d(this,true);
});
},uncheck:function(jq){
return jq.each(function(){
_d(this,false);
});
},clear:function(jq){
return jq.each(function(){
_d(this,false);
});
},reset:function(jq){
return jq.each(function(){
var _2f=$(this).radiobutton("options");
_d(this,_2f.originalChecked);
});
}};
$.fn.radiobutton.parseOptions=function(_30){
var t=$(_30);
return $.extend({},$.parser.parseOptions(_30,["label","labelPosition","labelAlign",{labelWidth:"number"}]),{value:(t.val()||undefined),checked:(t.attr("checked")?true:undefined),disabled:(t.attr("disabled")?true:undefined),readonly:(t.attr("readonly")?true:undefined)});
};
$.fn.radiobutton.defaults={width:20,height:20,value:null,disabled:false,readonly:false,checked:false,label:null,labelWidth:"auto",labelPosition:"before",labelAlign:"left",onChange:function(_31){
}};
})(jQuery);

