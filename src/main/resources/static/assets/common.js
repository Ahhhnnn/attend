var tableName = 'easyweb-open';  // 本地缓存表名
/*var base_server = 'http://47.100.214.147:8888/v1/';  // 接口地址*/
var base_server = 'http://127.0.0.1:8888/v1/';  // 接口地址

// 配置layui模块所在的位置
layui.config({
    base: getProjectUrl() + 'assets/module/'
}).extend({
    formSelects: 'formSelects/formSelects-v4'
}).use(['jquery', 'layer'], function () {
    var $ = layui.jquery;
    var layer = layui.layer;


});
console.log("项目的绝对路径为:"+getProjectUrl());
// 获取当前项目的绝对路径
function getProjectUrl() {
    var layuiDir = layui.cache.dir;
    if (!layuiDir) {
        var js = document.scripts, last = js.length - 1, src;
        for (var i = last; i > 0; i--) {
            if (js[i].readyState === 'interactive') {
                src = js[i].src;
                break;
            }
        }
        var jsPath = src || js[last].src;
        layuiDir = jsPath.substring(0, jsPath.lastIndexOf('/') + 1);
    }
    return layuiDir.substring(0, layuiDir.indexOf('assets'));
}
