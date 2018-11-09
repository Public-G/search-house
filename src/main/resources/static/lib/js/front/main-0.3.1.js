/**
 * Global.
 */
var room107 = {
    username: null,
    name: null,
    email: null,
    auth : 0,
    faviconUrl: '',
    userType : 0,
    regRole : 0,
    enableScroll : true,
    onLoginRegClick : null,
    beforeLoginRefreshArr: [],
    ticket:'',
    //TODO: this needs a better solution
    loginHandlers: {},
    unbindLogin: function(callback){
        var handler = this.loginHandlers[callback];
        console.log(callback, handler);
        $(document).unbind('room107.login', handler);
    },
    bindLogin : function(callback){
        if(typeof callback !== 'function'){
            return;
        }
        var handler = function(e, options){
            callback(options);
        };
        this.loginHandlers[callback] = handler;
        $(document).bind('room107.login', handler);
    },
    // oyyd: this function doesn't word as expected
    isLandlord : function(type) {
        type = type || this.userType;
        return (type & 2) !== 0;
    },
    isRegLandlord : function() {
        return this.regRole === 2;
    },
    compacityTip : "",
    beforeLoginRefresh: function(cb){
        if(typeof cb === 'function'){
            this.beforeLoginRefreshArr.push(cb);
        }
    },
    unbindBeforeLoginRefresh: function(cb){
        var index = this.beforeLoginRefreshArr.indexOf(cb);
        if(~index){
            this.beforeLoginRefreshArr.splice(index, 1);
        }
    },
    execBeforeLoginRefresh: function(){
        var stop = false;

        for(var index=0; index<this.beforeLoginRefreshArr.length;index++){
            log(index);
            var func = this.beforeLoginRefreshArr[index];
            log(func);
            stop = stop || func();
        }
        return stop;
    },
    setName: function(name, username){
        refreshLoginStatus(name, username, room107.faviconUrl);
    },
    setFavicon: function(faviconUrl){
        refreshLoginStatus(room107.name, room107.username, faviconUrl);
        room107.faviconUrl = faviconUrl;
    },
    toggleUserMenu: function(show){
        if(show){
            $('#usernameMenu').show();
        }else{
            $('#usernameMenu').hide();
        }
    },
    lessThanIE9: false,
    lessEqualThanIE9: false,
    beforeCloseLightbox: null,
};

function _ie(minVersion, maxVersion, callWhenIE, callWhenElse) {
    try {
        var v = navigator.appVersion.split(";")[1].replace(/[ ]/g, "");
        var i = v.indexOf("MSIE");
        if (i >= 0) {
            v = parseInt(v.substring(i + 4));
            log('IE version: ' + v);
            if (v >= minVersion && v <= maxVersion) {
                callWhenIE && callWhenIE(v);
                return;
            }
        }
    } catch(e) {
    }
    callWhenElse && callWhenElse();
}

/**
 * @return -1: < min or NaN, 0: in bound, 1: > max,
 */
function inBound(value, bound) {
    bound = bound.split('-');
    bound[0] = parseInt(bound[0]);
    bound[1] = parseInt(bound[1]);
    var v = parseInt(value);
    if (isNaN(v))
        return -1;
    if ((!isNaN(bound[0]) && v < bound[0]))
        return -1;
    if ((!isNaN(bound[1]) && v > bound[1])) {
        return 1;
    }
    return 0;
}

// --------------- UIUE ---------------- //
function select(buttons) {
    buttons.removeClass('buttonUnselected').addClass('buttonSelected');
}

function unselect(buttons) {
    buttons.removeClass('buttonSelected').addClass('buttonUnselected');
}

function swtichSelect(button) {
    if (button.hasClass('buttonSelected')) {
        unselect(button);
    } else {
        select(button);
    }
}

function smoothAnimate(obj, css, option) {
    var o = {
        'fail' : function() {
            $(this).css(css);
        }
    };
    merge(option, o);
    obj.stop().animate(css, o);
}

function shake(element, shakeClass, times) {
    var i = 0, t = false, o = element.attr('class') + ' ', c = '', shakeClass = shakeClass || 'shakeClass', times = times || 2;
    if (t)
        return;
    t = setInterval(function() {
        i++;
        c = i % 2 ? o + shakeClass : o;
        element.attr('class', c);
        if (i == 2 * times) {
            clearInterval(t);
            element.removeClass(shakeClass);
        }
    }, 200);
}

function timing(time, stepCallback, endCallback) {
    stepCallback(time);
    var id = setInterval(function() {
        stepCallback(--time);
        if (time <= 0) {// end
            clearInterval(id);
            endCallback && endCallback();
        }
    }, 1000);
    return id;
}

var Popup = {
    poping : 'poping',
    onPopupClose : [], // callbacks

    defaultOption : {
        'popupPosition' : 'right', // left, right, top, bottom
        'popupOffset' : 15,
        'tipArrowClass' : 'tipArrowLeft',
        'text' : null
    },

    create : function(popup, inputContainer, option, input) {
        popup = popup || $('<div class="popup" />');
        inputContainer = $(inputContainer);
        input = input ? $(input) : inputContainer.find('input');
        if (input.hasClass(Popup.poping))
            return false;
        input.addClass(Popup.poping);
        merge(Popup.defaultOption, option);
        if (option.text) {
            popup.append($('<p>').text(option.text));
        }
        popup.css(option).addClass('popup').append('<div class="tipArrow"></div>').appendTo(inputContainer.parent());
        var r = {
            'popup' : popup,
            'inputContainer' : inputContainer,
            'input' : input,
            'tipArrow' : popup.find('.tipArrow'),
            'close' : function() {
                $(document).click();
            }
        };
        // auto close
        Popup.autoClose(popup, input, function() {
            input.removeClass(Popup.poping);
        });
        // tip arrow
        if (option.tipArrowClass) {
            r.tipArrow.addClass(option.tipArrowClass);
            Popup.initTipArrow(r.tipArrow);
        }
        // display
        var p = option.popupPosition, h = popup.outerHeight(), cp = inputContainer.position(), cw = inputContainer.outerWidth(), ch = inputContainer.outerHeight();
        option.popupOffset = option.popupOffset ? option.popupOffset : 0;
        if (p === 'right') {
            popup.css({
                'top' : cp.top - (h - ch) / 2,
                'left' : cp.left + cw + option.popupOffset
            });
        } else if (p === 'top') {
            popup.css({
                'top' : cp.top - option.popupOffset,
                'left' : cp.left + parseInt((option.left ? option.left : 0))
            });
        } else if (p === 'bottom') {
            popup.css({
                'top' : cp.top + ch + option.popupOffset,
                'left' : cp.left + parseInt((option.left ? option.left : 0))
            });
        }
        return r;
    },
    isPopingUp : function(input) {
        return $(input).hasClass(Popup.poping);
    },
    initTipArrow : function(tipArrow) {
        if (tipArrow.hasClass('tipArrowLeft') || tipArrow.hasClass('tipArrowRight')) {
            tipArrow.css('top', (tipArrow.parent().outerHeight() - tipArrow.outerHeight()) / 2);
        } else if (tipArrow.hasClass('tipArrowUp') || tipArrow.hasClass('tipArrowDown')) {
            tipArrow.css('left', (tipArrow.parent().outerWidth() - tipArrow.outerWidth()) / 2);
        }
    },
    autoClose : function(popup, input, onClose) {
        function docClick(e) {
            var d = popup[0], target = e.target;
            if (d !== target && input[0] !== target && !$.contains(d, target) && !$.contains(input[0], target)) {
                if (onClose) {
                    onClose();
                }
                popup.remove();
                $(document).unbind('click', docClick);
                $.each(Popup.onPopupClose, function(index, f) {
                    f();
                });
            }
        };
        $(document).bind('click', docClick);
    }
};

var Lightbox = {
    _keydown : function(e) {
        if (e.which == 27) {// ESC
            Lightbox.close();
        }
    },
    _overflow : null,
    /**
     * @param {Object} option by default: {
     *      showClose: false,
     *      showLoadingAlways: false,
     *      showLoading: false,
     *      controlled: false,
     *      callback: null
     * }
     */
    open : function(url, urlParam, callback, option) {
        room107.enableScroll = false;
        // init
        option = option || {};
        var c = $('#lightboxContent'), loading = $('#lightboxBg .loading');
        $('#lightboxClose').hide();
        loading.hide();
        $('#lightbox').show();
        this._overflow = $('body').css('overflow');
        $('body').bind('keydown', this._keydown).css('overflow', 'hidden');

        urlParam = urlParam || {};
        urlParam['embedded'] = true;
        // adjust behavior by the callee
        if (option['controlled'] === true && typeof option['callback'] === 'function'){
            var lightboxEle = $('#lightbox');
            option.callback(lightboxEle);
        }else{
            $('#lightboxBg').css('opacity',0.8);
            c.empty().css('opacity', 0).load(url, urlParam, function(data) {
                if (!option['showLoadingAlways']) {
                    loading.hide();
                } else {// close lazy
                    // setTimeout(function() {
                    // loading.hide();
                    // }, 5000);
                }
                c.find('input').each(function() {
                    if ($(this).is(':visible') && !$(this).val()) {
                        $(this).focus();
                        return false;
                    }
                });
                try {
                    callback && callback(data);
                } catch(e) {
                    log(e);
                }
                $(function() {
                    c.css('opacity', 1);
                });
            });
        }
        // handle option
        if (option['showClose']) {
            $('#lightboxClose').show();
        }
        if (option['showLoading']) {
            loading.show();
        }
    },
    close : function() {
        if(typeof room107.beforeCloseLightbox === 'function'){
            room107.beforeCloseLightbox();
        }
        room107.enableScroll = true;
        $('#lightbox').hide();
        $('#lightboxContent').empty();
        $('body').unbind('keydown', this._keydown).css('overflow', this._overflow || 'auto');
        if(window.jiathis_cancel){
            try{
                jiathis_cancel();
            }catch(e){}
        }
    }
};

/**
 * Call onValid() if all inputs are non-empty when type enter.
 */
function validateInput(inputs, onValid) {
    inputs.keydown(function(e) {
        if (e.which == 13) {
            var valid = true;
            inputs.each(function() {
                if ($(this).val && !$(this).val()) {
                    $(this).focus();
                    return valid = false;
                }
            });
            if (valid && onValid) {
                onValid();
            }
        }
    });
}

/**
 * Handler place holder for all browsers.
 */
var PlaceHolder = {
    className : 'placeholder',

    init : function(inputs) {
        if ('placeholder' in document.createElement('input')) {
            log('placeholder supported by browser');
            return;
        }
        inputs = inputs || $('input[type!=password],textarea');
        inputs = $(inputs);
        inputs.not('[type=password]').each(function() {
            var p = $(this).attr('placeholder');
            if (!p)
                return true;
            $(this).focus(function() {
                if ($(this).val() == p) {
                    $(this).val('').removeClass(PlaceHolder.className);
                }
            }).blur(function() {
                PlaceHolder._init($(this));
            });
            PlaceHolder._init($(this));
        });
    },
    _init : function(input) {
        if (input.val() == '') {
            input.val(input.attr('placeholder')).addClass(PlaceHolder.className);
        }
    }
};

var Input = {
    selectedClass : 'buttonSelected',
    unselectedClass : 'buttonUnselected',
    validateErrorClass : 'validateError',
    _selectGroup : function(selector, radio, onClick) {
        Input.unselect(selector);
        selector.click(function() {
            if (radio) {
                Input.unselect(selector, $(this));
            }
            $(this).toggleClass(Input.selectedClass + ' ' + Input.unselectedClass);
            onClick && onClick();
        });
        return this;
    },
    select : function(selector) {
        $(selector).removeClass(Input.unselectedClass).addClass(Input.selectedClass);
        return this;
    },
    unselect : function(selector, exclude) {
        if (exclude) {
            selector = selector.not(exclude);
        }
        selector.each(function() {
            $(this).removeClass(Input.selectedClass).addClass(Input.unselectedClass);
        });
        return this;
    },
    isSelected : function(selector) {
        return $(selector).hasClass(Input.selectedClass);
    },
    radioGroup : function(selector, onClick) {
        return Input._selectGroup($(selector), true, onClick);
    },
    checkboxGroup : function(selector, onClick) {
        return Input._selectGroup($(selector), false, onClick);
    },
    bindNumberInput : function(selector) {
        $(selector).keydown(function(e) {
            var d = e.which == 38 ? 1 : (e.which == 40 ? -1 : 0);
            Input.increaseNumberInput($(this), d);
        });
        return this;
    },
    increaseNumberInput : function(selector, delta) {
        if (delta === 0)
            return;
        selector = $(selector);
        var v = parseInt(selector.val());
        if (isNaN(v))
            return;
        v += delta;
        var n = selector.attr('numberInput');
        var i = inBound(v, n);
        if (i < 0) {
            v = n.split('-')[0];
        } else if (i > 0) {
            v = n.split('-')[1];
        }
        selector.val(v);
        return this;
    },
    bindValidate : function(selector) {
        if (!selector)
            selector = $(document);
        else {
            selector = $(selector);
        }
        function init() {
            $(this).removeClass(Input.validateErrorClass);
        }


        selector.find('[validate]').click(init).focus(init).blur(function(e) {
            if ($(this).attr('readonly') == null) {
                var v = $(this).val();
                if (v !== '' && v !== $(this).attr('placeholder')) {
                    Input.validate($(this), false);
                }
            }
        }).bind("input propertychange", function() {
            Input.validate($(this), false);
        });
        return this;
    },
    /**
     * int, length, select
     */
    validate : function(input, force) {
        function int() {
            i = parseInt(input.val());
            if (isNaN(i)) {
                return false;
            }
            input.val(i);
            return true;
        }

        input = $(input);
        var v = input.attr('validate');
        if (!v)
            return true;
        if (!force && input.val() === '') {
            return true;
        }
        var result = true;
        if (v.indexOf('int') == 0) {
            if (!int(input)) {// check int
                result = false;
            } else if (v.length > 3) {//check bound
                if (inBound(input.val(), v.substring(3)) != 0)
                    result = false;
            }
        } else if (v.indexOf('length') == 0) {
            if (inBound(input.val().trim().length, v.substring(6)) != 0)
                result = false;
        } else if (v.indexOf('select') == 0) {
            if (input.find('.' + Input.selectedClass).length == 0)
                result = false;
        }
        if (result) {
            input.removeClass(Input.validateErrorClass);
        } else {
            input.addClass(Input.validateErrorClass);
        }
        return result;
    },
    validateAll : function(selector) {
        var result = true;
        $(selector).find('[validate]').not('[immutable],:hidden,.disabled,[multi-choice-validate]').each(function() {
            if (!Input.validate($(this), true)) {
                result = false;
            }
        });
        $(selector).find('[multi-choice-container]').each(function() {
            tmpResult = false;
            $(this).find('[multi-choice-validate]').each(function() {
                if (Input.validate($(this), true)) {
                    tmpResult = true;
                }
            });
            if (!tmpResult) {
                result = false;
            }
        });
        return result;
    }
};

var UI = {
    showLogin : function() {
        room107.onLoginRegClick = null;
        Lightbox.open('/user/login', appendDebug({}));
    },
    scroll : function(top, time, callback) {
        if (time) {
            var scrollOver = false;
            $("body,html").animate({
                scrollTop : top
            }, time, function() {
                if (!scrollOver) {
                    scrollOver = true;
                    callback && callback();
                }
            });
        } else {
            $("body,html").scrollTop(top);
            callback && callback();
        }
    },
    /**
     *
     * @param direction 1: to right, 2: to left
     */
    slide : function(direction, container, show, hide, callback) {
        var h = Math.max(show.height(), hide.height());
        container.height(Math.max(container.height(), h));
        var left = container.width();
        if (direction == 2)
            left = -left;
        hide.length && hide.stop().animate({
            left : left
        });
        show.css('left', -left).stop().animate({
            left : 0
        }, function() {
            callback && callback();
        });
    }
};

var Auth = {
    isLoggedIn : function() {
        return room107.auth > 0;
    },
    assertLogin : function(callback) {
        if (this.isLoggedIn()) {
            return true;
        }
        room107.bindLogin(callback);
        UI.showLogin();
        return false;
    }
};

var Flow107 = {
    /**
     */
    create : function(container, option) {
        option = merge({
            itemSelector : '.item',
            space : 15,
            callback : null
        }, option);
        var r = {
            flow : function() {
                if (container.data('flowing')) {
                    container.data('reflow', true);
                    return;
                }
                container.data('flowing', true);
                var items = container.find(option.itemSelector);
                var canFlow = true;
                items.each(function() {
                    if (!$(this).height()) {
                        return canFlow = false;
                    }
                });
                if (!canFlow) {
                    container.data('flowing', false);
                    return;
                }
                var s = option.space, bottoms = [];
                var containerW = container.width(), containerH = 0;
                items.each(function() {
                    if (!$(this).height()) {
                        return;
                    }
                    var x = 0, y = 0, w = $(this).outerWidth(true);
                    if (!bottoms.length) {// first 1
                        bottoms.push($(this));
                    } else {
                        var sumW = 0, minTop = -1, minCol = 0;
                        for (var i = 0; i < bottoms.length; i++) {
                            sumW += bottoms[i].outerWidth(true);
                            var top = bottoms[i].data('y') + bottoms[i].height();
                            if (minTop < 0 || top < minTop) {
                                minTop = top;
                                minCol = i;
                            }
                        };
                        sumW += (bottoms.length - 1) * s;
                        if (sumW + w + s <= containerW) {
                            bottoms.push($(this));
                            x = sumW + s;
                        } else {// more than 1 row
                            x = bottoms[minCol].data('x');
                            y = minTop + s;
                            bottoms[minCol] = $(this);
                        }
                    }
                    $(this).css({
                        position : 'absolute',
                        left : x,
                        top : y,
                        margin : 0
                    }).data('x', x).data('y', y);
                    var h = $(this).height() + y;
                    if (h > containerH) {
                        containerH = h;
                    }
                });
                // container
                container.height(containerH);
                option.callback && option.callback();
                container.data('flowing', false);
                // reflow
                if (container.data('reflow')) {
                    container.data('reflow', false);
                    this.layout();
                }
            }
        };
        return r;
    }
};