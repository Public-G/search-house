window.global = window.global || {};
var _context = {
    isTouch : 'ontouchstart' in document
};
var _image = {
    thumbSearch: function(imageId) {
        return this.thumb(imageId, 'imageView2/3/w/210');
    },
    thumbDetail: function(imageId) {
        return this.thumb(imageId, 'imageView2/2/h/460');
    },
    //personal favicon
    thumbPersonFavicon: function(imageId) {
        //return this.thumb(imageId, 'imageView2/4/w/190/h190');
        return this.thumb(imageId, 'imageMogr2/thumbnail/!190x190r');
    },
    // manage
    thumbManageCover: function(imageId) {
        return this.thumb(imageId, 'imageView2/5/w/184/h/184');
    },
    thumbManageUpload: function(imageId) {
        return this.thumb(imageId, 'imageView2/5/w/130/h/130');
    },
    // mobile
    thumbDetailMobile: function(imageId) {
        return this.thumb(imageId, 'imageView2/2/w/460');
    },
    thumbWeixin: function(imageId) {
        return this.thumbManageUpload(imageId);
    },
    thumb: function(imageId, param) {
        if(imageId.indexOf('://') > 0) {
            return imageId + '?' + param;
        }else { // local
            return '/static/user/house/' + imageId;
        }
    }
};

function log(message) {
    if ( typeof logDisabled != 'undefined' && logDisabled)
        return;
    var debug = getParams()['debug'] != null;
    if ((debug && console) || location.host.indexOf('localhost') >= 0) {
        console.log(message);
    }
}

function evalFunction(f) {
    if (f) {
        try {
            return eval(f + '()');
        } catch(e) {
        }
    }
}

function randomId() {
    return 'r' + new Date().getTime();
}

Date.prototype.format = function(format) {
    /*
     * eg:format="YYYY-MM-dd hh:mm:ss";
     */
    var o = {
        "M+" :this.getMonth() + 1, // month
        "d+" :this.getDate(), // day
        "h+" :this.getHours(), // hour
        "m+" :this.getMinutes(), // minute
        "s+" :this.getSeconds(), // second
        "q+" :Math.floor((this.getMonth() + 3) / 3), // quarter
        "S" :this.getMilliseconds()
        // millisecond
    };

    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "")
            .substr(4 - RegExp.$1.length));
    }

    for ( var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
                : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
};

function abbreviate(s, limit) {
    if (!s || s.length <= 3 || s.length <= limit)
        return s;
    return s.substring(0, limit - 3) + '...';
}

/**
 *@param s string
 *@return 'abc' for given 'abcDefGhi'
 */
function getLowerPrefix(s) {
    for (var i = 0; i < s.length; i++) {
        var c = s.charAt(i);
        if (!(c >= 'a' && c <= 'z')) {
            return s.substring(0, i);
        }
    }
    return s;
}

/**
 * @return parameters form URL
 */
var _params = null;
function getParams() {
    if (_params)
        return _params;
    _params = new Object();
    var pairs = location.search.substring(1).split('&');
    for (var i = 0; i < pairs.length; i++) {
        var pos = pairs[i].indexOf('=');
        if (pos == -1) {
            _params[pairs[i]] = '';
        } else {
            var argname = pairs[i].substring(0, pos);
            var value = pairs[i].substring(pos + 1);
            _params[argname] = decodeURIComponent(value);
        }
    }
    return _params;
}

/**
 * Append debug value when in debug model.
 */
function appendDebug(param) {
    param = param || {};
    var debugValue = getParams()['debug'];
    if (debugValue) {
        param['debug'] = debugValue;
    } else if ('' == debugValue) {
        param['debug'] = '0';
    }
    return param;
}

function _appendParam(url, key, value) {
    if (!url)
        return url;
    if (url.indexOf('?') >= 0) {
        url += '&';
    } else {
        url += '?';
    }
    return url + key + '=' + value;
}

function _json(data) {
    if (!data)
        return data;
    try {
        return JSON.parse(data);
    } catch(e) {
        log('JSON failed: ' + e);
        return eval('(' + data + ')');
    }
}

function _date(t) {
    if (t) {
        if ( typeof t === 'string') {
            t = new Date(t);
        }
        return t.getFullYear() + '-' + (t.getMonth() + 1) + '-' + t.getDate();
    }
    return '';
}

function _time(t) {
    if (t) {
        if ( typeof t === 'string') {
            t = new Date(t);
        }
        return _date(t) + ' ' + t.getHours() + ':' + t.getMinutes() + ':' + t.getSeconds();
    }
    return '';
}

function merge(from, to) {
    to = to || {};
    if (from) {
        for (var p in from) {
            if ( typeof to[p] === 'undefined')
                to[p] = from[p];
        }
    }
    return to;
}

// prototype
Array.prototype.indexOf = function(val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val)
            return i;
    }
    return -1;
};
Array.prototype.remove = function(val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
};
/**
 * @return value by given position in an array.
 */
Array.prototype.get = function(i, loop) {
    if (!this.length)
        return null;
    if (loop) {
        i = (i + this.length) % this.length;
    } else if (i < 0 || i >= this.length) {
        return null;
    }
    return this[i];
};

// UI
/**
 * @param e mouse or touch event
 * @return {x,y}
 */
function getPosition(e) {
    var x, y;
    if (_context.isTouch && e.originalEvent && e.originalEvent.touches) {
        var touch = e.originalEvent.touches.item(0);
        x = touch.pageX;
        y = touch.pageY;
    } else {
        x = e.pageX;
        y = e.pageY;
    }
    return {
        x : x,
        y : y
    };
}

var room107GenMapStyle = function(option){
    return {
        styleJson : [{
            "featureType" : "land",
            "elementType" : "all",
            "stylers" : {
                "color" : "#f6f6f6"
            }
        }, {
            "featureType" : "water",
            "elementType" : "all",
            "stylers" : {
                "color" : "#81c7d4"
            }
        }, {
            "featureType" : "green",
            "elementType" : "all",
            "stylers" : {
                "color" : "#e0f0d9"
            }
        }, {
            "featureType" : "manmade",
            "elementType" : "geometry",
            "stylers" : {
                "color" : "#000000",
                "visibility" : "off"
            }
        }, {
            "featureType" : "building",
            "elementType" : "all",
            "stylers" : {
                "visibility" : "off"
            }
        }, {
            "featureType" : "boundary",
            "elementType" : "all",
            "stylers" : {}
        }, {
            "featureType" : "highway",
            "elementType" : "geometry",
            "stylers" : {
                "color" : "#ffffff"
            }
        }, {
            "featureType" : "highway",
            "elementType" : "labels.text.fill",
            "stylers" : {
                "color" : "#00ac97"
            }
        }, {
            "featureType" : "highway",
            "elementType" : "labels.text.stroke",
            "stylers" : {
                "color" : "#f6f6f6"
            }
        }, {
            "featureType" : "arterial",
            "elementType" : "geometry",
            "stylers" : {
                "color" : "#ffffff",
                "weight" : "0.5"
            }
        }, {
            "featureType" : "arterial",
            "elementType" : "labels.text.stroke",
            "stylers" : {
                "color" : "#f6f6f6"
            }
        }, {
            "featureType" : "local",
            "elementType" : "geometry",
            "stylers" : {
                "color" : "#ffffff",
                "weight" : "0.7"
            }
        }, {
            "featureType" : "local",
            "elementType" : "labels.text.fill",
            "stylers" : {
                "color" : "#cccccc"
            }
        }, {
            "featureType" : "local",
            "elementType" : "labels.text.stroke",
            "stylers" : {
                "color" : "#f6f6f6"
            }
        }, {
            "featureType" : "subway",
            "elementType" : "geometry",
            "stylers" : {
                "weight" : option.styleSubwayWeight,
                "saturation" : 67
            }
        }, {
            "featureType" : "poi",
            "elementType" : "labels.text.fill",
            "stylers" : {
                "color" : "#666666"
            }
        }, {
            "featureType" : "railway",
            "elementType" : "all",
            "stylers" : {
                "visibility" : "off"
            }
        }, {
            "featureType" : "arterial",
            "elementType" : "labels.text.fill",
            "stylers" : {
                "color" : "#999999"
            }
        }]
    };
};

var _Map = {
    maps : {},
    defaultZoomLevel : 11,
    point : function(name) {
        if (!name) {
            return new BMap.Point(116.404, 39.915);
        }
        return new BMap.Point(116.403875, 39.915168);
    },
    /**
     * @param container '.map'
     * @return BMap.Map
     */
    create : function(container, option) {
        option = merge({
            autoShow : true,
            styleSubwayWeight : 2.1
        }, option);
        var id = randomId();
        container.find('.mapContent').attr('id', id);
        var m = new BMap.Map(id);
        // zoom
        m.enableScrollWheelZoom();
        container.find('.zoomin').click(function() {
            m.zoomIn();
        });
        container.find('.zoomout').click(function() {
            m.zoomOut();
        });
        if (option.autoShow) {
            m.centerAndZoom(this.point(), this.defaultZoomLevel);
        }
        this.maps[id] = m;
        // inner values
        m.room107 = {
            markers : [],
            addMarker : function(marker) {
                m.addOverlay(marker);
                this.markers.push(marker);
            },
            clear : function() {
                m.clearOverlays();
                this.markers = [];
            },
            style : function() {
                var s = room107GenMapStyle(option);
                m.setMapStyle(s);
            }
        };
        // callback
        if (option.onLoad) {
            m.addEventListener("tilesloaded", option.onLoad);
        }
        /*
         * hack style
         */
        var styled1 = false;
        m.addEventListener("tilesloaded", function() {
            if (!styled1) {
                styled1 = true;
                log('tilesloaded: m.room107.style()');
                m.room107.style();
            }
        });
        // style
        m.room107.style();
        return m;
    }
};


//slide button widget
function SlideButton(imgUrl, info) {
    this.info = info;
    this.node = $("<img>");
    this.node.addClass("button");
    this.node.attr("src", imgUrl);
}

SlideButton.prototype = {
    setIndex: function(buttonTrigger, index) {
        this.node.click(function() {
            buttonTrigger.click(index);
        });
    }
};

function SlideButtonTrigger(node) {
    this.node = node;
    this.infoNode = $("<span>");
    this.infoNode.addClass("contactInfo");
    this.node.append(this.infoNode);
    this.buttonWidth = 36;
    this.infoWidth = 192;
    this.buttons = [];
    this.animateTime = 700;
    this.index = -1;
    this.node.css("width", this.infoWidth + "px");
}

SlideButtonTrigger.prototype = {
    addButton: function(button) {
        button.setIndex(this, this.buttons.length);
        this.buttons.push(button);
        this.node.append(button.node);
        this.node.css("width", (this.buttonWidth * this.buttons.length + this.infoWidth) + "px");
    },
    click: function(index) {
        if (this.index == index) return ;
        this.infoNode.css("opacity", 0);
        for (var i = 0; i <= index; i++) {
            var style = {
                left: (i * this.buttonWidth) + "px"
            };
            this.buttons[i].node.animate(style, this.animateTime);
        }
        for (var i = index + 1; i < this.buttons.length; i++) {
            var style = {
                left: (i * this.buttonWidth + this.infoWidth) + "px"
            };
            this.buttons[i].node.animate(style, this.animateTime);
        }
        this.infoNode.text(this.buttons[index].info);
        var style = {
            left: ((index + 1) * this.buttonWidth) + "px"
        };
        this.infoNode.animate(style, this.animateTime, function() {
            $(this).css("opacity", 1);
        });
        this.index = index;
    }
};