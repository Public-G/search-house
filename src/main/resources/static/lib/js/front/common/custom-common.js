var layer;
var cities = $('.citytab');


layui.use('layer', function () {
    layer = layui.layer;

    // 显示支持的城市
    $.get('/rent/city', function (data) {
        if(data.code === 0){
            var cityHtml = parseCity(data.data);
            $(".citytabnr").append(cityHtml);
        }
    }, "json");

    // 监听城市点击事件
    cities.delegate('a','click',function () {
        window.location.href = '/?city=' + $(this).data("bind");
    });
});

function parseCity(data) {
    var cityHtml = '';
    var obj = {};

    $.ajax({
        url: '/lib/json/province_city.json'
        ,type: 'GET'
        ,async: false
        , success: function(area){
            $.each(area, function (index, item) {
                if (item.level == 0) {
                    return true; // continue
                }

                // 找到城市所属省份
                $.each(data, function (index, value) {
                    if(item.name == value) {
                        if (obj[item.parentId] == undefined) {
                            obj[item.parentId] = [value];
                        } else {
                            obj[item.parentId].push(value);
                        }
                        return false; // break
                    }
                });
            });

            $.each(area, function (index, item) {
                if (item.level == 1) {
                    return true; // continue
                }

                $.each(obj, function (key, value) {
                    if (key == item.id) {
                        cityHtml += '<br/><span class="initals">'+ item.name +'</span>';
                        $.each(value, function (i, val) {
                            cityHtml += '<a target="_blank" data-bind="'+ val +'" href="/?city='+ val +'">'+ val +'</a>';
                        });
                    }
                });
            });
        }
    });

    return cityHtml;
}