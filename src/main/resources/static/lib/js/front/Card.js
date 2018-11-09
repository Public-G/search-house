// NOTE: user Card/index.jsx if possible
var Card = {
    defaultOptions : {
        type: 'HORIZON'
    },
    create : function(cardContainer, options) {
        cardContainer = $(cardContainer || ".cardContainer");
        var card = {};
        var navigation = null;
        card.options = $.extend(true, {}, this.defaultOptions, options);
        var slideDistance = 0;
        var verticalDistance = 0;
        var currentCardId = null;
        var navigateLock = false;
        /**
         * Slide duration in millisecond.
         */
        card.slideDuration = 500;
        card.slideLockDuration = 700;
        /**
         * Layout cards. Called after page loaded.
         */
        var activeItem = function(ele){
            ele.each(function(){
                var currentEle = $(this);
                currentEle.find('span').css({
                    'background-image': 'url("'+currentEle.attr('data-pic-hover')+'")'
                });
                currentEle.find('p').css({
                    'color': '#00ac97'
                });
            });
        };
        var inactiveItem = function(ele){
            ele.each(function(){
                var currentEle = $(this);
                currentEle.find('span').css({
                    'background-image': 'url("'+currentEle.attr('data-pic')+'")'
                });
                currentEle.find('p').css({
                    'color': '#939393'
                });
            });
        };

        card.layout = function() {
            //specificly set card index
            cardContainer.find('.card').each(function(index){
                $(this).attr('data-card-index', index);
            });
            if(this.options.decoration){
                var navs = [];
                cardContainer.find('.card').each(function(){
                    var card = $(this);
                    if(card.attr('data-is-head')!=='true'){
                        return;
                    }
                    navs.push({
                        'title': card.attr('data-title'),
                        'pic': card.attr('data-pic'),
                        'picHover': card.attr('data-pic-hover'),
                        'target': card.attr('id')
                    });
                });
                this.createNavigation(navs);
            }
            var cardWidth = $(".card").width();
            var cardHeight = $('.card').height();
            slideDistance = cardWidth + ($("body").width() - cardWidth) / 2;
            verticalDistance = cardHeight + ($('body').height() - cardHeight) / 2;
            var card = cardContainer.find(".card").eq(0);
            onCardShown(card);
            card.show();
            $("body").css("overflow-x", "hidden");

            if(navigation){
                currentCardId = cardContainer.find('.card:visible').attr('id');
                activeItem(navigation.find('[data-id="'+currentCardId+'"]'));
            }
        };

        card.createNavigation = function(navs){
            // init
            navigation = $(document.body).find('[role="side-nav"]');
            if(navigation.length<1){
                navigation = $('<div role="side-nav"></div>');
                $(document.body).css('position', 'relative').append(navigation);
                navigation.css({
                    'position': 'absolute',
                    'top': '150px',
                    'left': (($(document.body).width() - cardContainer.width())/2 - 70) + 'px'
                });
                $(window).resize(function(){
                    navigation.css({
                        'left': (($(document.body).width() - cardContainer.width())/2 - 70) + 'px'
                    });
                });
            }
            for(var i=0; i<navs.length; i++){
                var nav = navs[i];
                var item = $('<div role="item" data-index="'+i+'" data-pic="'+nav.pic
                    +'" data-pic-hover="'+nav.picHover+'" data-id="'+nav.target+'"></div>').css({
                    'cursor': 'pointer'
                });
                item.click(function(event, option){
                    if(!canSlide()){
                        return;
                    }
                    navigation.find('[role="item"]').each(function(){
                        inactiveItem($(this));
                    });
                    var target = $(event.currentTarget);
                    activeItem(target);
                    if(currentCardId !== target.attr('data-id')){
                        var currentIndex = parseInt($('[data-id="'+currentCardId+'"]').attr('data-index'));
                        if(currentCardId){
                            if(currentIndex > parseInt(target.attr('data-index'))){
                                card.slideDown('#'+target.attr('data-id'), option);
                            }else{
                                card.slideTop('#'+target.attr('data-id'), option);
                            }
                        }
                        currentCardId = target.attr('data-id');
                    }
                });
                item.hover(function(){
                    $(this).css({
                        'opacity': '0.7'
                    });
                }, function(){
                    $(this).css({
                        'opacity': '1'
                    });
                });
                var icon = $('<span></span>').css({
                    'margin-left': '8px',
                    'display': 'inline-block',
                    'width': '30px',
                    'height': '30px',
                    'background-image': 'url("'+nav.pic+'")'
                });
                item.append(icon);
                var title = $('<p>'+nav['title']+'</p>').css({
                    'font-size': '12px',
                    'margin-bottom': '14px'
                });
                item.append(title);
                navigation.append(item);
            }
        };

        function onCardShown(card) {
            var f = card.attr('onCardShow');
            f && eval(f + '()');
        }

        function canSlide(){
            if(!navigation || navigateLock){
                return false;
            }
            navigateLock = true;
            setTimeout(function(){
                navigateLock = false;
            }, card.slideLockDuration);
            return true;
        }
        /**
         * @param option  {'beforeSlide', 'afterSlide'}
         */
        function slide(direction, target, option) {
            // WARN: You should not use both vertical slide
            // and horizonal slide in one page.
            var isVertical = (direction==='top' || direction==='bottom');
            option = option || {};
            if ( typeof target === "function") {
                option.afterSlide = target;
                target = null;
            } else if (target) {
                target = $(target);
                console.log(target);
            }
            // callback
            option.beforeSlide && option.beforeSlide();
            $(document).trigger('beforeSlide', target);
            var cards = cardContainer.find('.card');
            var visible = cardContainer.find(".card:visible");
            var cardIndex = parseInt(visible.attr('data-card-index'));
            target = target || ((direction==='left' || direction==='top') ? cards.eq(cardIndex+1) : cards.eq(cardIndex-1));
            if(!isVertical){
                var isLeft = (direction==='left');
                visible.animate({
                    "left" : (isLeft) ? -slideDistance : slideDistance
                }, card.slideDuration, function() {
                    $(this).hide();
                    // callback
                    option.afterSlide && option.afterSlide();
                });
                if(target.attr('data-is-head') === 'true'){
                    target.css({
                        'left': (isLeft) ? slideDistance : -slideDistance
                    }).show().animate({
                        "left" : 0
                    }, card.slideDuration, function(){
                        navigation && navigation.css({
                            'display': 'block'
                        });
                    });
                }else{
                    navigation && navigation.css({
                        'display': 'none'
                    });
                    target.css({
                        'left': (isLeft) ? slideDistance : -slideDistance
                    }).show().animate({
                        "left" : 0
                    }, card.slideDuration);
                }
            }else{
                cardContainer.css('overflow', 'hidden');
                $('.card .banner').hide();
                var isTop = (direction==='top');
                visible.animate({
                    "top" : (isTop) ? -verticalDistance : verticalDistance
                }, card.slideDuration, function() {
                    $(this).hide();
                    option.afterSlide && option.afterSlide();
                });
                target.css({
                    'top': (isTop) ? verticalDistance : -verticalDistance
                }).show().animate({
                    "top" : parseInt(visible.parents('.cardContainer').css('padding-top'))
                }, card.slideDuration, function(){
                    cardContainer.css('overflow', 'visible');
                    $('.card .banner').fadeIn();
                });
            }
            onCardShown(target);
        }

        card.show = function(target) {
            $('.card').hide();
            target = $(target);
            target.show();
            if(navigation){
                currentCardId = cardContainer.find('.card:visible').attr('id');
                inactiveItem(navigation.find('[data-id]'));
                activeItem(navigation.find('[data-id="'+currentCardId+'"]'));
            }
            onCardShown(target);
        };

        card.slideLeft = function(target, option) {
            slide('left', target, option);
        };

        card.slideRight = function(target, option) {
            slide('right', target, option);
        };

        card.slideTop = function(target, option){
            slide('top', target, option);
        };

        card.slideDown = function(target, option){
            slide('bottom', target, option);
        };

        card.navigateNext = function(){
            var nextNavi = navigation.find('[data-id="'+currentCardId+'"]').next();
            if(nextNavi.length > 0){
                nextNavi.trigger('click');
            }
        };

        card.navigatePrev = function(){
            var nextNavi = navigation.find('[data-id="'+currentCardId+'"]').prev();
            if(nextNavi.length > 0){
                nextNavi.trigger('click');
            }
        };

        return card;
    }
};