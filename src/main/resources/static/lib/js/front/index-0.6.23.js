function adjustImgSize(e, i, t) {
    var h = new Image;
    h.src = e.attr("src"), h.onload = function () {
        var n = h.width, o = h.height;
        if (i / t <= n / o) {
            e.width(t * n / o), e.height(t);
            var a = (i - e.width()) / 2;
            e.css("margin-left", a)
        } else {
            e.width(i), e.height(i * o / n);
            var a = (t - e.height()) / 2;
            e.css("margin-top", a)
        }
    }
}

$(".lazy").lazyload({effect: "fadeIn"}), $(".lazy").load(function () {
    for (var e = $(".onenewImg").length - 1; e >= 0; e--) adjustImgSize($(".onenewImg").eq(e), 200, 200)
});
var search = $("#search"), searchLabel = $(".searchLabel"), clickable = $(".clickable"), middle = $(".middle"),
    searchBox = $(".searchBox"), leftFrog = $(".leftFrog"), rightFrog = $(".rightFrog"), bgIndex = $(".bg-index"),
    sign = $(".signLogin"), nullDiv = $(".nullDiv");

$(".userNeed a").eq(1).on("click", function () {
    location.href = "/rent/map?city=" + city
});

$(".userNeed a").eq(2).on("click", function () {
    location.href = "/rent/search?city=" + city
});

var width = $(window).width(), height = document.documentElement.clientHeight, bgHeight = .6675 * width;
middle.height($(window).height()), leftFrog.css({top: "50%", "margin-top": "-150px"}), rightFrog.css({
    top: "50%",
    "margin-top": "-150px"
}), searchBox.css({
    left: "50%",
    "margin-left": "-283px",
    top: "50%",
    "margin-top": "-132px"
}).show(), bgIndex.css({top: (height - 883 < 0 ? height - 883 : "0") + "px"}), nullDiv.css({
    height: bgHeight + height - 883 + "px",
    "min-height": $(window).height()
}), window.onresize = function () {
    var e = document.documentElement.clientHeight, i = bgIndex.width(), t = .6675 * i;
    nullDiv.css({height: t + e - 883 + "px", "min-height": e}), middle.height(e), leftFrog.css({
        top: "50%",
        "margin-top": "-150px"
    }), bgIndex.css({top: (e - 883 < 0 ? e - 883 : "0") + "px", height: t + "px"}), rightFrog.css({
        top: "50%",
        "margin-top": "-150px"
    }), searchBox.css({left: "50%", top: "50%"}), searchBox.show()
}, search.keydown(function (e) {
    if (13 === e.keyCode) {
        var i = search.val();
        "可在此搜索区、地址、地铁／公交线路" == i ? i = "" : location.href = "/rent/search?city=" + city + "&keyword=" + i
    }
}), searchLabel.click(function () {
    var e = search.val();
    "可在此搜索区、地址、地铁／公交线路" == e ? e = "" : location.href = "/rent/search?city=" + city + "&keyword=" + e
}), clickable.click(function () {
    "/news/list" == location.href
});