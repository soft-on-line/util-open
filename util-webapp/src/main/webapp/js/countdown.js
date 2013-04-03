/**
 * author: Qin Zhipeng 2010-09-16
 * 用法：页面只需要在用户点击的按钮或链接上加上 id="countDown" 即可。
 * 另外使用该js要调用jQuery。
 * <div id="countDown" onclick="javascript:document.location.href='http://www.baidu.com/';">返回</div>
 * <input id="countDown" type="button" value="确定" onclick="javascript:document.location.href='http://www.baidu.com/';"/>
 * <a id="countDown" href="http://www.baidu.com/">百度</a>
 */
var __count_down_second = 3;
var __count_down_object;
var __count_down_value;
var __count_down_title;
$(document).ready(function(){
	__count_down_object = $('#countDown')[0];
	if(null != __count_down_object)
	{
		if('INPUT'==__count_down_object.tagName){
			__count_down_value = __count_down_object.value;
		}else if('DIV'==__count_down_object.tagName || 'A'==__count_down_object.tagName){
			__count_down_value = __count_down_object.innerHTML;
		}
		
		__count_down_title = __count_down_object.title;
		
		window.setInterval(reduce, 1000);
	}
})
function reduce()
{
	if(__count_down_second <= 0){
		__count_down_object.click();
		
		return ;	
	}
	
	if('INPUT'==__count_down_object.tagName){
		__count_down_object.value = __count_down_value + "(" + __count_down_second + ")";
	}else if('DIV'==__count_down_object.tagName || 'A'==__count_down_object.tagName){
		__count_down_object.innerHTML = __count_down_value + "(" + __count_down_second + ")";
	}
	
	__count_down_object.title = __count_down_title + "（" + __count_down_second + "秒倒计时自动跳转）";
	
	__count_down_second -= 1;
}