var regionCountMap = {}, // 地区数据
    map,
    customLayer; // 麻点图


function load(city, aggData) {
    map = new BMap.Map("allmap");  // 创建实例
    map.centerAndZoom(city, 12);  // 设置城市及地图显示级别
    map.setMinZoom(12); // 设置地图允许的最小级别
    map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
    var navigationControl = new BMap.NavigationControl({enableGeolocation: true});
    var scaleControl = new BMap.ScaleControl({anchor: BMAP_ANCHOR_TOP_LEFT});
    map.addControl(navigationControl); // 平移缩放控件
    map.addControl(scaleControl); // 比例尺控件，左上角

    for (var i = 0; i < aggData.length; i++) {
        regionCountMap[aggData[i].key] = aggData[i].count;
    }

    map.addEventListener("zoomend", function () {
        if (this.getZoom() > 12) {
            navigationControl.show();
            scaleControl.show();

            if (customLayer) {
                map.addTileLayer(customLayer);

            } else {
                // LBS云麻点展现
                customLayer = new BMap.CustomLayer({
                    geotableId: 196748,
                    q: '', // 检索关键字
                    tags: '', // 空格分隔的字符串
                    filter: '', // 过滤条件，参考：http://lbsyun.baidu.com/index.php?title=lbscloud/api/geosearch
                    pointDensityType: BMAP_POINT_DENSITY_MEDIUM // 麻点密度高
                });
                map.addTileLayer(customLayer); // 添加自定义图层
                customLayer.addEventListener('hotspotclick', houseTip); // 单击图层事件
            }

        } else {
            if (customLayer) {
                map.removeTileLayer(customLayer);
            }
            navigationControl.hide();
            scaleControl.hide();
        }
    });

    drawRegion(city);
}

function drawRegion(city) {
    var boundary = new BMap.Boundary(); // 创建行政区域搜索的对象实例
    var polygonContext = {};
    var regionList = [];
    var regionPoint;

    $.ajax({
        url: '/lib/json/city_region.json',
        method: "GET",
        async: false,
        success: function (area) {
            $.each(area, function (index, item) {
                if (item.name === city) {
                    parentId = item.id;
                    return false; // break
                }
            });

            $.each(area, function (index, item) {
                if (item.parentId === parentId) {
                    regionList.push(item);
                }
            });
        }
    });

    for (var i = 0; i < regionList.length; i++) {
        regionPoint = new BMap.Point(regionList[i].lng, regionList[i].lat);

        var houseCount = 0;
        if (regionList[i].name in regionCountMap) {
            houseCount = regionCountMap[regionList[i].name];
        }

        var textContent = '<p style="margin-top: 20px; pointer-events: none">' +
            regionList[i].name + '</p>' + '<p style="pointer-events: none">' +
            houseCount + '套</p>';

        // 文本标注
        textLabel = new BMap.Label(textContent, {
            position: regionPoint, // 指定了文本标注所在的地理位置
            offset: new BMap.Size(-40, 20) // 文本偏移量
        });

        // 设置文本标注样式，该样式将作用于文本标注的容器元素上
        textLabel.setStyle({
            height: '78px',
            width: '78px',
            color: '#fff',
            backgroundColor: '#00AC97',
            border: '0px solid rgb(255, 0, 0)',
            borderRadius: "50%",
            fontWeight: 'bold',
            display: 'inline',
            lineHeight: 'normal',
            textAlign: 'center',
            opacity: '0.8',
            zIndex: 2,
            overflow: 'hidden'
        });
        map.addOverlay(textLabel); // 将标签画在地图上

        // 记录行政区域覆盖物
        polygonContext[textContent] = []; // 边界点集合

        (function (textContent) {
            boundary.get(city + regionList[i].name, function (rs) {

                var count = rs.boundaries.length; // 行政区域边界点集合长度
                if (count === 0) {
                    console.log('未能获取当前输入行政区域');
                    return;
                }

                for (var j = 0; j < count; j++) {
                    // 建立多边形覆盖物
                    var polygon = new BMap.Polygon(
                        rs.boundaries[j],
                        {
                            strokeWeight: 3, // 边线的宽度，以像素为单位
                            strokeColor: '#00AC97', // 边线颜色
                            fillOpacity: 0.2, // 填充的透明度，取值范围0 - 1
                            fillColor: '#7FCEC2' // 填充颜色。当参数为空时，折线覆盖物将没有填充效果
                        }
                    );
                    map.addOverlay(polygon); // 添加覆盖物
                    polygonContext[textContent].push(polygon);
                    polygon.hide(); // 隐藏覆盖物(初始化隐藏边界)
                }
            });
        })(textContent);

        // 监听鼠标移入事件
        textLabel.addEventListener('mouseover', function (event) {
            var label = event.target;
            var polygon = polygonContext[label.getContent()];

            label.setStyle({backgroundColor: '#FF0000'});
            for (var n = 0; n < polygon.length; n++) {
                polygon[n].show();
            }
        });

        // 监听鼠标移出事件
        textLabel.addEventListener('mouseout', function (event) {
            var label = event.target;
            var polygon = polygonContext[label.getContent()];

            label.setStyle({backgroundColor: '#00AC97'});
            for (var n = 0; n < polygon.length; n++) {
                polygon[n].hide();
            }
        });

        // 监听点击
        textLabel.addEventListener('click', function (event) {
            map.zoomIn(map.getZoom()); // 放大一级视图
            map.panTo(event.point); // 将地图的中心点更改为给定的点(区域坐标)

            // this.hide();
        });

    }

}

// 单击热点图层
function houseTip(e) {
    var customPoi = e.customPoi; // poi的默认字段
    var contentPoi = e.content; // poi的自定义字段

    var content = '<p style="width:280px;margin: 0; line-height: 20px;">地址：' +
        customPoi.address + '<br/>价格：' + contentPoi.price + '元/月<br/>面积：'
        + contentPoi.square + '平方米<br/><a target="_blank" href="/rent/search/'+contentPoi.sourceUrlId+'">查看详情>></a></p>';

    // 定义检索模版
    var searchInfoWindow = new BMapLib.SearchInfoWindow(map, content, {
        title: customPoi.title, // 标题
        width: 300,
        height: 80,
        panel: "panel", // 搜索结果面板
        enableAutoPan: true, // 自动平移
        enableSendToPhone: false, // 是否显示发送到手机按钮
        searchTypes: [
            BMAPLIB_TAB_SEARCH, // 周边检索
            BMAPLIB_TAB_TO_HERE, // 到这里去
            BMAPLIB_TAB_FROM_HERE // 从这里出发
        ]
    });

    var point = new BMap.Point(customPoi.point.lng, customPoi.point.lat);
    searchInfoWindow.open(point);
}



