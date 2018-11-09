/**
 * 延迟加载时间
 * @type {number}
 */
var loadingTime = 500;

/**
 * 成功返回码
 * @type {number}
 */
var code_success = 0;

/**
 * 失败返回码
 * @type {number}
 */
var code_fail = 1;

var table, layer, form;

/**
 * 定义模块
 * @param exports 用于输出该模块的接口
 */
layui.define(['layer', 'table', 'form'], function(exports){
    layer = layui.layer;
    table = layui.table;
    form = layui.form;

    // 动态表格
    exports('renderTable', function(tableSelector, tableId, url, cols){
        var tableIns = table.render({
            elem: tableSelector //指定原始表格元素选择器（推荐id选择器）
            , id: tableId
            , url: url
            , page: true
            , height: "full-125"
            , limit: 10
            , limits: [10, 15, 20, 25, 30]
            , request: {
                pageName: 'curr' //页码的参数名称，默认：page
            }
            , parseData: function (res) { //res 即为原始返回的数据
                return {
                    "code": res.code, //解析接口状态
                    "msg": res.msg, //解析提示文本
                    "count": res.pageBean.count, //解析数据长度
                    "data": res.pageBean.data //解析数据列表
                };
            }
            , cols: cols
        });
    });

    // 新增数据
    exports('formSubmit', function(submitFilter, url){
        form.on('submit('+submitFilter+')', function (data) {
            var submitBtn = $(this);
            if (!submitBtn.hasClass("layui-btn-disabled")) {
                submitBtn.addClass("layui-btn-disabled");
                var index = parent.layer.getFrameIndex(window.name);
                $.post(url, data.field, function (data) {
                    if (data.code === code_success) {
                        layer.msg(data.msg);
                        setTimeout(function() {
                            parent.layer.close(index); //关闭
                            parent.location.reload(); //刷新父页面
                        }, loadingTime);
                    } else {
                        var msg = data.msg;
                        // if (msg == null || msg == '' || msg == undefined) {
                        //     msg = '操作失败，请稍后重试';
                        // }
                        layer.msg(msg, function () {
                            //关闭后的操作
                            submitBtn.removeClass("layui-btn-disabled");
                        });
                    }
                }, "json");
            }
            return false;
        });
    });
});

/**
 * 关键词搜索
 * @param tableId 容器唯一ID
 */
function keywordSearch(tableId) {
    var active = {
        reload: function () {
            var keywordSearch = $('.searchVal');
            //执行重载
            table.reload(tableId, {
                page: {
                    curr: 1 //重新从第 1 页开始
                }
                , where: {
                    keyword: keywordSearch.val()
                }
            });
        }
    };

    $('#keywordRefresh').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });
}

/**
 * 弹窗最大化
 * @param layer
 * @param title
 * @param url
 */
function alertByFull(title, url) {
    layer.open({
        title : title,
        type : 2,
        maxmin: true, //开启最大化最小化按钮
        area: ['1190px', '680px'],
        // area: ['893px', '600px'],
        content : url,
        success : function(layero, index) {
            setTimeout(function() {
                layui.layer.tips('点击这里返回',
                    '.layui-layer-setwin .layui-layer-close', {
                        tips : [3, '#3595CC'],
                        time: 800
                    });
            }, loadingTime)
        }
    });
    // layer.open({
    //     title: title,
    //     type: 2,
    //     shift: 0,
    //     area: ['100%', '100%'],
    //     content: url,
    //     success: function (layero, index) {
    //         layer.full(index);
    //     }
    // });
}

/**
 * 判断是否为空
 * @param data
 * @returns {boolean}
 */
function checkIsNotNull(data) {
    return data != null && typeof data !== "undefined" && typeof data.valueOf() === "string" && data.length > 0;
}

/**
 * 删除
 * @param deleteCheck
 * @param url
 * @param fun
 */
function doDelete(layer, deleteCheck, url, tableIns) {
    if (deleteCheck.length == 0) {
        layer.msg("请选择删除项");
    } else {
        $.ajax({
            url: url,
            type: "POST",
            traditional: true,
            data: {
                _method: "DELETE",
                deleteCheck: deleteCheck
            },
            dataType: "json",
            success: function (data) {
                if (data.code === code_success) {
                    layer.msg(data.msg, {
                        icon : 1,
                        time: loadingTime
                    }, function () {
                        tableIns.reload();
                    });
                } else {
                    layer.msg(data.msg, {
                        icon : 2,
                        time: loadingTime
                    });
                }
            }
        });
    }
}

/**
 * 分页
 * @param laypage
 * @param page_label
 * @param count
 * @param curr
 * @param fun
 */
function renderPage(laypage, page_label, count, curr, fun) {
    laypage.render({
        elem: page_label
        , count: count
        , curr: curr
        , layout: ['prev', 'page', 'next', 'count', 'skip']
        , jump: function (obj, first) {
            if (!first) {
                fun(obj.curr);
            }
        }
    });
}

/**
 * 公告
 * @param layer
 * @param noticeStartTime
 * @param noticeEndTime
 * @param noticeTitle
 * @param noticeContent
 */
function alertNotice(layer, noticeStartTime, noticeEndTime, noticeTitle, noticeContent) {
    var content = '<div style="padding: 50px; line-height: 22px; background-color: #393D49; color: #fff; font-weight: 300;">网站将在以下时间段进行升级维护:<br/>'
        + noticeStartTime + ' -- ' + noticeEndTime + '<br/><br/>本次升级内容:<br/>' + noticeContent + '</div>';
    if (checkIsNotNull(noticeStartTime)) {
        layer.open({
            type: 1
            ,title: noticeTitle
            ,closeBtn: false
            ,area: '400px;'
            ,shade: 0.8
            ,id: 'notice-id'
            ,btn: ['知道了']
            ,btnAlign: 'c'
            ,moveType: 1
            ,content: content
            ,success: function(layero){
            }
        });
    };
}