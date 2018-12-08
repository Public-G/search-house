webpackJsonp([4], [function (n, o) {

    var regMobile = /^1[3|4|5|7|8][0-9]{9}$/; //手机号验证规则

    loginbutton.on('click', function () {
        $('.signLogin').show();
    });

    $(".closedButton").on("click", function () {
        $(".signLogin").hide();
    });

    $('.logout1').on('click', function () {
        $.post('/sys/logout', function (data) {
            if (data.code == 0) {
                window.location.reload();
            }
        });
    });

    $(".sendverify").on('click', function () {
        var mobile = $("input[name='mobile']").val();
        if (regMobile.test(mobile)) {
            $.get('/captcha/sms', {
                mobile: mobile
            }, function (data) {
                if(data.code == 1) {
                    layer.msg(data.msg, function () {});
                } else {
                    layer.msg("发送成功，请注意查看", {time: 1000});
                    $.cookie("timer", 60);
                    timekeeping();
                }
            });
        } else {
            layer.msg("请输入正确的手机号", function () {});
            return;
        }
    });

    $(".submitlogin").on('click', function () {
        var mobile = $("input[name='mobile']").val();
        var smsCaptcha = $("input[name='smsCaptcha']").val();
        if (!regMobile.test(mobile)) {
            layer.msg("请输入正确的手机号", function () {});
            return;
        }

        if (smsCaptcha.trim().length != 6) {
            layer.msg("请输入正确的验证码", function () {});
            return;
        }

        $.post('/user/auth/login', {
            mobile: mobile
            ,smsCaptcha: smsCaptcha
        }, function (data) {
            if (data.code === 0) {
                layer.msg(data.msg, {icon: 1, offset: '500px', time: 300}, function () {
                    window.location.reload();
                });
            } else {
                layer.msg(data.msg, {icon: 2, anim: 6, offset: '500px', time: 1000});
            }
        });
    });

    $("li.logintype a").on('click', function () {
        layer.msg("敬请期待", {time: 1000});
    });

    // cookie存在倒计时(验证码使用)
    if($.cookie("timer")!=undefined && $.cookie("timer")!='NaN' && $.cookie("timer")!='null'){
        timekeeping();
    }



}]);


function timekeeping(){
    var smsCodeBtn = $('.sendverify');
    smsCodeBtn.attr("disabled", true);
    var interval=setInterval(function(){ //每秒读取一次cookie
        var timer = $.cookie("timer"); //从cookie 中读取剩余倒计时
        smsCodeBtn.text('请等待' + timer + '秒');
        timer--;
        if(timer == 0){
            clearInterval(interval);  //清除定时器
            $.cookie("timer", timer, { expires: -1}); //删除cookie
            smsCodeBtn.attr("disabled", false);
            smsCodeBtn.text('重新发送');
        } else {
            $.cookie("timer", timer); //重新写入剩余倒计时
        }
    },1000);
}

//# sourceMappingURL=../maps/userLoginJs-0.6.23.js.map
