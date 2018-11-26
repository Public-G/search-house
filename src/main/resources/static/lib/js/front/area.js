function getRegionList(city, isAsync) {
    var parentId;
    var regions = [];

    $.ajax({
        url : "/lib/json/area.json",
        type : "GET",
        async : isAsync,
        success : function (area) {
            $.each(area, function (index, item) {
                if (item.level !== 1) {
                    return true; // continue
                }
                if (item.name === city) {
                    parentId = item.id;
                    return true; // continue
                }
            });

            $.each(area, function (index, item) {
                if (item.level !== 2) {
                    return true; // continue
                }
                if (item.parentId === parentId) {
                    regions.push(item);
                }
            });
        }
    });

    return regions;
}