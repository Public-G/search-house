var usertype = $('.usertype');
var houseDes1 = $('.houseDes1');
var quietIcon = "/lib/images/front/map/marker1-normal.png";
var smallpoint = "/lib/images/front/mapicon/smallgreen.png";
// var marker1Full = "/static/image/mapicon/marker1-full";
var favoryellow = "/lib/images/front/mapicon/favoryellow.png";
var mapwrap = $('.mapwrap');
var backTop = $('.backTop');
var viewHousePoints = [];
var priceStart = $('.priceStart');
var priceEnd = $('.priceEnd');
var measureStart = $('.measureStart');
var measureEnd = $('.measureEnd');
var monthCost = $('.monthCost');
var houseMeasure = $('.houseMeasure');
var regions = $('.areas');
var selectMale = $('.selectMale');
var priceAdd = $('.priceTrue');
var areaAdd = $('.areaTrue');
var searchBox = $('.searchBox');
var searchButton = $('.searchButton');
var houseDescibtion = $('.houseDescibtion');
var signNum = $('.signNum');
var sign = $('.signLogin');
var demandSelect = $('.demandSelect');
var willSign = $('.willSign');
var favor = $('.favor');
var locations = $('.locations');
var setStyle = $('.setStyle');
var img = quietIcon;
var jumpIcon = new BMap.Icon("/lib/images/front/map/jump.png", new BMap.Size(25,30));
var xclose = $('.xclose');
var locationX = $('.locationX');
var locationY = $('.locationY');
var locationAdress = $('.locationAdress');
var test1 = 'test1';

// 点击喜欢成功关闭按钮
xclose.on('click',function(){
    $('.favorPrompt').hide();
});

// 性别限制条件动画
selectMale.hover(function(){
    $(this).children('ul').show();
},function(){
    $(this).children('ul').hide();
});

// 清空所有条件动画
$('.delAll').hover(function(){
    $(this).css({'opacity':'0.6'});
},function(){
    $(this).css({'opacity':'1'});
});

// 价格输入监听
priceStart.on('input',function(){
    price();
});

priceEnd.on('input',function(){
    price();
});

// 月租输入enter键事件
$('.monthCost input').keydown(function(e){
    if(e.keyCode === 13){
        searchButton.click();
    }
});





// 地点搜索提交
// searchButton.on('click',function(){
//     var query = searchBox.val();
//     alert(priceKey);
//     location.href = '/search?keyword='+query+"&regionCnName="+region+"&priceBlock="+priceKey+"&areaBlock="+areaKey;


    // if (query.trim() != '') {
    //     location.href = '/search?keyword='+query+'&priceBlock='+priceKey+'&areaBlock='+areaKey;
    // } else {
    //     location.href = '/search?priceBlock='+priceKey+'&areaBlock='+areaKey;
    // }

// // 地点提交时对价格的条件判断
//     if(typeof (priceStart.val()-0) == typeof (priceEnd.val()-0) && (priceStart.val()-0) < (priceEnd.val()-0)){
//         if((price1 != '' || price1 != '') && query != ''){
//             location.href = '/' + $('.typetype').html().trim() + $('.allIndex').html().trim() +'_1?'+'query='+query+'&price1='+price1+'&price2='+price2;
//         }
//     }else{
//         location.href = '/search?priceBlock='+priceKey+'&areaBlock='+areaKey;
//         // location.href = '/' + $('.typetype').html().trim() + $('.allIndex').html().trim() +'_1?'+'query='+query;
//     }
// });

function formatDistance(distance){
    if(distance>=1000){
        return (distance / 1000).toFixed(1) + 'km';
    }else{
        return Math.round((distance/10)) * 10 + 'm';
    }
}

// 判断是否有搜索地点坐标，有则显示距离
if(locationX.eq(0).html() != undefined){
    for (var i = $('.distanceMeter').length - 1; i >= 0; i--) {
        if($('.distanceMeter').eq(i).html() != ''){
            $('.houseDescibtion').eq(i).after('<span class="distance">距离搜索位置'+ formatDistance($('.distanceMeter').eq(i).html()) +'</span>');
        }
    }
}

// 进入后判断url是否带参数判断截取
var href = location.href;
if(href.split('=').length > 1){
    if(typeof (decodeURI(href.split('=')[1].split('?')[0].split('&')[0]) - 0) != 'number'){
        searchBox.val(decodeURI(href.split('=')[1].split('?')[0].split('&')[0]));
    }
};

// 进入界面后对当前地点价格搜索空条件判断制定已选条件(条件栏)
// function queryCondition(){
//
//
//     // $("a[data-price-key="+priceKey+"]").addClass("selector");
//     // if (priceKey != "*") {
//     //     $('.allCondition p').after('<a class="selectCondition" style="margin-left:5px" href="/'+'_1">'+ priceKey +'<span class="delthis">X</span></a>');
//     // }
//
//     // var price1 = priceStart.val();
//     // var price2 = priceEnd.val();
//     //
//     // if(price1 != '' && price2 != '' && query == ''){
//     //     $('.allCondition p').after('<a class="selectCondition" style="margin-left:5px" href="/' + $('.typetype').html().trim() + $('.allIndex').html().trim()+'_1">'+ price1+'-'+price2 +'<span class="delthis">X</span></a>');
//     // }
//     //
//     // if(query != '' && price1 == '' && price2 == ''){
//     //     $('.allCondition p').after('<a class="selectCondition" style="margin-left:5px" href="/' + $('.typetype').html().trim() + $('.allIndex').html().trim()+'_1">'+ query +'<span class="delthis">X</span></a>');
//     // }
//     //
//     // if(query != '' && price1 != '' && price2 != ''){
//     //     $('.allCondition p').after('<a class="selectCondition" style="margin-left:5px" href="/' + $('.typetype').html().trim() + $('.allIndex1').html().trim()+'_1?price1='+price1+'&price2='+price2+'">'+ query +'<span class="delthis">X</span></a>');
//     // }
//     //
//     // if(query != '' && price1 != '' && price2 != ''){
//     //     $('.allCondition p').after('<a class="selectCondition" style="margin-left:5px" href="/' + $('.typetype').html().trim() + $('.allIndex1').html().trim()+'_1?query='+ query +'">'+ price1 +'-'+ price2 +'<span class="delthis">X</span></a>');
//     // }
//
//     if($('.selectCondition').length > 0 ){
//         $('.delAll').show();
//     }
// }

// queryCondition();

// 面积条件选择输入框监听
measureStart.on('input',function(){
    measure();
});

measureEnd.on('input',function(){
    measure();
});

// 价位输入条件判断
function price(){
    if(typeof (priceStart.val()-0) == typeof (priceEnd.val()-0) && (priceStart.val()-0) < (priceEnd.val()-0)){
        $('.priceTrue',monthCost).css({'display':'inline-block'});
    }else{
        $('.priceTrue',monthCost).hide();
    }
}

// 面积输入条件判断
function measure(){
    if(typeof (measureStart.val()-0) == typeof (measureEnd.val()-0) && (measureStart.val()-0) < (measureEnd.val()-0)){
        $('.areaTrue',houseMeasure).css({'display':'inline-block'});
    }else{
        $('.areaTrue',houseMeasure).hide();
    }
}

// 房源划入画出房东类型说明
usertype.on('mouseenter',function() {
    $(this).siblings('.houseDes1').show();
    $(this).parents('.oneHouse').css({'z-index':10000});
});

usertype.on('mouseleave',function() {
    $(this).siblings('.houseDes1').hide();
    $(this).parents('.oneHouse').css({'z-index':0});
});


// 百度地图API功能(关闭底图可点功能，默认启用)
var map = new BMap.Map("allmap",{enableMapClick:false});

map.enableScrollWheelZoom(true);

map.setMapStyle(room107GenMapStyle({
    styleSubwayWeight: 1
}));

var top_right_navigation = new BMap.NavigationControl({anchor: BMAP_ANCHOR_TOP_RIGHT, type: BMAP_NAVIGATION_CONTROL_ZOOM}); //右上角，仅包含平移和缩放按钮
map.addControl(top_right_navigation);

//复杂的自定义覆盖物
map.centerAndZoom(new BMap.Point(116.3964,39.9093), 15);
function ComplexCustomOverlay(point,img){
    this._point = point;
    this._img = img;
}

ComplexCustomOverlay.prototype = new BMap.Overlay();

ComplexCustomOverlay.prototype.initialize = function(map){
    var point = this._point;
    var img = this._img;
    this._map = map;
    var div = this._div = document.createElement("div");
    div.setAttribute('pointlng',point.lng);
    div.setAttribute('pointlat',point.lat);
    div.style.position = "absolute";
    div.className = 'points'
    div.setAttribute('clicked','false');
    div.style.width = '24px';
    div.style.cursor = 'pointer';
    div.style.height = '31px';
    div.style.background = "url("+ img +") 0px 0px no-repeat"
    div.style.zIndex = BMap.Overlay.getZIndex(this._pointlat);
    div.style.whiteSpace = "nowrap";
    div.style.MozUserSelect = "none";
    // div.style.backgroundSize = "40px";
    div.onmouseover = function(){
        if( this.getAttribute('clicked') == 'false'){
            this.style.background = "url(/lib/images/front/map/lightgreen.png) 0 0 no-repeat";
            this.style.backgroundSize = '24px 35px';
            this.style.zIndex = 1000;
            this.style.width = '26px';
            this.style.height = '35px';
            this.style.marginTop = -4 + 'px';
            // this.style.marginLeft = -3 + 'px';
            // this.style.backgroundSize = "30px 43px";
        }
    }

    div.onmouseout = function(){
        if( this.getAttribute('clicked') == 'false'){
            this.style.background = "url("+ img +") 0 0 no-repeat";
            this.style.width = '24px';
            this.style.zIndex = 0;
            this.style.marginTop = 0;
            this.style.marginLeft = 0;
            this.style.height = '31px';
            // this.style.backgroundSize = "24px 31px";
        }
    }

    function siblings(elem) {
        var a = [];
        var b = elem.parentNode.children;
        for(var i =0;i<b.length;i++) {
            if(b[i] !== elem) a.push(b[i]);
        }
        return a;
    }
    div.onclick = function(){
        var self = this;
        var flag = true;
        $('.leftMiddle',setStyle).css({"background":"#fff"});
        $('.leftMiddle',setStyle).children().children('.houseDescibtion').css({"color":"#939393"});
        $('.leftMiddle',setStyle).children().children('.locationname').css({"color":"#000"});
        $('.leftMiddle',setStyle).children('.dateTime').css({"color":"#cacaca"});
        $('.leftMiddle .sexReq',setStyle).css({"background-color":"rgb(0, 172, 151);"});
        $('.leftMiddle li',setStyle).css({"background":"#fff"});
        $('.leftMiddle .houseReq span',setStyle).css({"color":"#000"});
        $('.leftMiddle',setStyle).children('p').css({"color":"#939393"});
        $('.leftMiddle .locationname',setStyle).css({"color":"#000"});
        $('.usertype',setStyle).css({'background-color':'transparent'});
        $('.leftMiddle',setStyle).children('.distance').css({"color":"#cacaca"});
        $('.leftMiddle',setStyle).children('.houseDescibtion').css({"color":"#939393"});
        var thissilblings = siblings(self);
        thissilblings.map(function(item,i){
            item.setAttribute('clicked','false');
            item.style.background = "url("+ img +") 0 0 no-repeat";
        })
        this.setAttribute('clicked','true');
        this.style.background = "url(/static/image/icon/map/lightgreenshadow.png) 0 0 no-repeat";
        this.style.backgroundSize = '24px 42px';
        this.style.zIndex = 100000;
        this.style.width = '24px';
        this.style.height = '42px';
        this.style.marginTop = -4 + 'px';
        // 地图图标是否被点击表示位
        this.setAttribute('clicked','true');
        // 根据坐标点在左侧的房源列表中找房子并选中
        for (var i = $('.oneHouse').length - 1; i >= 0; i--) {

            var setStyle = $('.setStyle').eq(i);

            if(setStyle.attr('pointlng') == this.getAttribute('pointlng') && setStyle.attr('pointlat') == this.getAttribute('pointlat')){
                $(window).mousewheel();

                if(flag){
                    $('html,body').animate({scrollTop:(setStyle.offset().top - 170)}, 500);
                    flag = false;
                }
                setStyle.children('.leftMiddle').children('.locationLi').css({"background":"#00ac97"});
                setStyle.children('.leftMiddle').children('.desLi').css({"background":"#00ac97"});
                setStyle.children('.leftMiddle').children().children('.locationname').css({"color":"#fff"});
                setStyle.children('.leftMiddle').children().children('.houseDescibtion').css({"color":"#fff"});
                setStyle.children().children().eq(1).css({"background":"#00ac97","color":"#fff"});
                setStyle.children().children().eq(1).children().find('span').css({"color":"#fff"});
                setStyle.children().children().eq(1).children().css({"color":"#fff"});

                setStyle.children('.leftMiddle').css({"background":"#00ac97","color":"#fff"});
                setStyle.children('.leftMiddle').children().css({"color":"#fff"});
                setStyle.children('.leftMiddle').children().find('span').css({"color":"#fff"});

                rightMapFixed();

            }
        }
    }

    map.getPanes().labelPane.appendChild(div);

    return div;

}

ComplexCustomOverlay.prototype.draw = function(){
    var pixel = this._map.pointToOverlayPixel(this._point);
    this._div.style.left = pixel.x + "px";
    this._div.style.top  = pixel.y + "px";
}

//地图放大缩小
$('.big').click(function() {
    $('.BMap_stdMpZoomIn').click();
})

$('.small').click(function() {
    $('.BMap_stdMpZoomOut').click();
});

//添加地图房屋坐标点
function addHousePoint(x,y,jump,img,address){
    // 跳点用到的函数
    if(jump){
        var pt = new BMap.Point(x,y);
        var marker2 = new BMap.Marker(pt,{icon:jumpIcon});  // 创建标注
        map.addOverlay(marker2);
        marker2.addEventListener('mouseover',function(){
            this.setAnimation(null); //跳动的动画
            var label = new BMap.Label(address,{offset:new BMap.Size(-10,-18)});
            marker2.setLabel(label);
        })

        marker2.addEventListener('mouseout',function(){
            this.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
            var label = this.getLabel();
            label.setContent("");//设置标签内容为空
            label.setStyle({borderWidth:"0px"});//设置标签边框宽度为0
        })
        viewHousePoints.push(pt);
        marker2.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
    }else{
        var pt = new BMap.Point(x,y);
        var myCompOverlay = new ComplexCustomOverlay(pt,img);
        map.addOverlay(myCompOverlay);
    }
}

// 搜索地址天假地图跳点
if(locationX.eq(0).html() != ''){
    for (var i = locationX.length - 1; i >= 0; i--) {
        var x = locationX.eq(i).html()-0;
        var y = locationY.eq(i).html()-0;
        var address = locationAdress.eq(i).html();
        addHousePoint(x,y,true,img,address);

    }
}else{
    $('.distance').hide();
}

// 房源列表透明度变化和与右侧地图联动点放大变色效果
$('.oneHouse').hover(
    function(){
        var points = $('.points');
        var lng = $(this).attr('pointlng');
        var lat = $(this).attr('pointlat');
        for (var i = points.length - 1; i >= 0; i--) {
            if(points.eq(i).attr('pointlng') == lng && points.eq(i).attr('pointlat') == lat){
                points.eq(i).mouseover();
            }
        }
    },
    function(){
        var points = $('.points');
        var lng = $(this).attr('pointlng');
        var lat = $(this).attr('pointlat');
        for (var i = points.length - 1; i >= 0; i--) {
            if(points.eq(i).attr('pointlng') == lng && points.eq(i).attr('pointlat') == lat){
                points.eq(i).mouseout();
            }
        }
    }
)

// 房源添加收藏喜欢并设置不能二次添加标示位
$('.oneHouse .favor').hover(
    function(){
        if($(this).attr('flag') == 'false'){
            $(this).attr('src','/static/image/icon/house-search/favored.png');
        }
    },
    function(){
        if($(this).attr('flag') == 'false'){
            $(this).attr('src','/static/image/icon/house-search/do-favor.png');
        }
    });

window.onscroll = function(){
    rightMapFixed();
}

// 界面向上滚动滴入fixed函数
function rightMapFixed(){
    if($(window).scrollTop() > demandSelect.offset().top + 55){
        if($(document).width() > 1180){
            var right = $(document).width() - mapwrap.offset().left - 394;
            mapwrap.css({'right':right,'top':'15px','position':'fixed','z-index':'10'});
            backTop.show();
        }

    }

    if($(window).scrollTop() < demandSelect.offset().top + 55){
        var right = $(document).width() - mapwrap.offset().left - 394;
        mapwrap.css({'right':0,'top':'0','position':'relative','z-index':'10'});
        backTop.hide();
    }
}

// 返回顶部按钮
backTop.on('click',function(){
    $('html,body').animate({scrollTop:0}, 500);
});

var width = $(window).width();
backTop.css({'bottom':'30px','position':'fixed','left':675+(width-1070)/2 + 'px'});

window.onresize = function(){
    var width = $(window).width();
    backTop.css({'bottom':'30px','position':'fixed','left':675+(width-1070)/2 + 'px'});
}

//房屋图片加载后按着短边放大后剧中显示方法
function adjustImgSize(img, boxWidth, boxHeight){
    var tempImg = new Image();
    tempImg.src = img.attr('src');
    var imgWidth=tempImg.width;
    var imgHeight=tempImg.height;

    //比较imgBox的长宽比与img的长宽比大小
    if((boxWidth/boxHeight)<=(imgWidth/imgHeight)){
        //重新设置img的width和height
        img.width((boxHeight*imgWidth)/imgHeight);
        img.height(boxHeight);
        //让图片居中显示
        var margin=(boxWidth-img.width())/2;
        img.css("margin-left",margin);
    }else{
        //重新设置img的width和height
        img.width(boxWidth);
        img.height((boxWidth*imgHeight)/imgWidth);
        //让图片居中显示
        var margin=(boxHeight-img.height())/2;
        img.css("margin-top",margin);
    }
};

// 没效果。。。
// 房源列表图片懒加载
// $(".lazy").lazyload({effect: "fadeIn", threshold : 200});  //lazyload初始化函数
// $('.lazy').load(function(){
//     adjustImgSize($(this), 210, 210);
// });

// 搜索地址按钮加enter键提交
searchBox.keydown(function (e){
    if(e.keyCode === 13){
        searchButton.click();
    }
});

// 房源描述字数限制
for (var i = houseDescibtion.length - 1; i >= 0; i--) {
    var substr = houseDescibtion.eq(i).html();
    if(substr.length > 40){
        var str = substr.substring(0,40) + '...';
        houseDescibtion.eq(i).html(str);
    }
}

// 待签约按钮跳转限制
willSign.on('click',function(){
    if(room107.visibleName == ''){
        $('.signLogin').show();
    }else{
        willSign.attr('href','/contactHouse');
    }
});

// 页面加载统计账户已收藏房源
// $.ajax({
//     type:'get',
//     url:'/house/getInterest',
//     data:{'date':Date.parse(new Date())},
//     dataType:'json'
// }).done(function(data){
//     if(data.status == -1){
//         willSign.on('click',function(){
//             $('.signLogin').show();
//         });
//     }else{
//         willSign.attr('href','/contactHouse');
//         signNum.css({'display':'inline-block'});
//         signNum.html(data.onlineHouses.length+data.offlineHouses.length);
//         for (var i = data.onlineHouses.length - 1; i >= 0; i--) {
//             for (var j = favor.length - 1; j >= 0; j--){
//                 if(favor.eq(j).attr('roomId') == data.onlineHouses[i].houseListItem.roomId && data.onlineHouses[i].houseListItem.rentType == 1){
//                     favor.eq(j).attr('flag','true');
//                     favor.eq(j).attr('src','/static/image/icon/house-search/favored.png');
//                 }else if(favor.eq(j).attr('houseId') == data.onlineHouses[i].houseListItem.id){
//                     favor.eq(j).attr('flag','true');
//                     favor.eq(j).attr('src','/static/image/icon/house-search/favored.png');
//                 }
//             }
//         }
//
//         for (var i = data.offlineHouses.length - 1; i >= 0; i--) {
//             for (var j = favor.length - 1; j >= 0; j--){
//                 if(data.offlineHouses[i].rentType == 1 && favor.eq(j).attr('roomId') == data.offlineHouses[i].roomId){
//                     favor.eq(j).attr('flag','true');
//                     favor.eq(j).attr('src','/static/image/icon/house-search/favored.png');
//                 }else if(data.offlineHouses[i].rentType == 2 && favor.eq(j).attr('houseId') == data.offlineHouses[i].id){
//                     favor.eq(j).attr('flag','true');
//                     favor.eq(j).attr('src','/static/image/icon/house-search/favored.png');
//                 }
//             }
//         }
//     }
// });

// 加收藏
favor.on('click',function(){
    var self = $(this);
    if(self.attr('flag') == 'false'){
        var houseId = self.attr('houseId');
        var param = {'houseId':houseId};
        if(self.attr('roomId') != 'false'){
            param['roomId'] = self.attr('roomId')
        }
        $.ajax({
            type:'post',
            url:'/house/addInterest',
            data:param,
            dataType:'json'
        }).done(function(data){
            if(data.status == -1 && data.errorMsg == '请先登录'){
                $('.signLogin').show()
            }else{
                if(data.status == -1){
                    alert(data.errorMsg);
                }else{
                    $('.favorPrompt').show();
                    setTimeout(function(){
                        $('.favorPrompt').fadeOut();
                    },3000);
                    self.attr('src','/static/image/icon/house-search/favored.png');
                    self.attr('flag','true');
                    var count = signNum.html();
                    signNum.html(count - 0 + 1);
                }
            }
        }).error(function(e){
            console.log(e)
        })
    }
});

// 把点加入地图用一个数组viewHousePoints
for (var i = setStyle.length - 1; i >= 0; i--) {
    viewHousePoints.push(new BMap.Point(setStyle.eq(i).attr('pointlng'),setStyle.eq(i).attr('pointlat')));
    addHousePoint(setStyle.eq(i).attr('pointlng'),setStyle.eq(i).attr('pointlat'),false,img)
}

if($('li',locations).length != 0){
    for (var i = locations.length - 1; i >= 0; i--) {
        var pointlng = $('li',locations).eq(i).attr('pointlng');
        var pointlat = $('li',locations).eq(i).attr('pointlat');
        var address = $('li',locations).eq(i).html();
        viewHousePoints.push(new BMap.Point(pointlng,pointlat));
        addHousePoint(pointlng,pointlat,true,img,address);
    }
}

map.setViewport(viewHousePoints); //让所有地图的坐标显示点都能在可视范围内
