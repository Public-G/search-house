webpackJsonp([4],[function(n,o){$(".submitlogin").on("click",function(){var n=$("input[name='username']").val(),o=$("input[name='password']").val();""!=n&&""!=o?$.ajax({type:"post",url:"/user/login/submit",data:{username:n,password:o,errorCode:""},dataType:"json"}).done(function(n){n.user.type>1?window.open("/house/landlord/?buy/manage","_self"):history.go(0),$(".warning").hide(),$(".submitlogin").css({"margin-top":"30px"})}).error(function(n){$(".warning").show(),$(".submitlogin").css({"margin-top":0})}):($(".warning").show(),$(".submitlogin").css({"margin-top":0}))}),$(".register").on("click",function(){window.location.href="/user/auth/register"}),$(".lostpassword").on("click",function(){$(".lostpasswordpage").show(),$(".lightLogin").hide()}),$("input[name='username']").keydown(function(n){13===n.keyCode&&$(".submitlogin").click()}),$("input[name='password']").keydown(function(n){13===n.keyCode&&$(".submitlogin").click()});var e=$(".sendverify"),r=!0;e.on("click",function(){var n=/^[0-9]+.?[0-9]*$/;n.test($(".telnumber").val())?r&&(e.html("发送中..."),$.ajax({url:"/user/verifyCode",type:"get",dataType:"json",data:{telephone:$(".telnumber").val()}}).done(function(){r=!1;var n=60,o=setInterval(function(){e.html(n--+"s"),e.css({background:"#ccc"}),n==-1&&(r=!0,clearInterval(o),e.html("发送验证码"),e.css({background:"#EBAC40"}))},1e3)})):$(".errorpromp").html("请填写正确的手机号").show()}),$(".sendnewdata").on("click",function(){var n=$(".telnumber").val(),o=$(".verifycode").val(),e=$(".setnewpwd").val(),r=/^[\@A-Za-z0-9\!\#\$\%\^\&\*\.\~]{6,30}$/;""!=n&&""!=o&&""!=e&&r.test(e)?$.ajax({url:"/user/rest",type:"post",data:{username:n,verifyCode:o,password:e},dataType:"json"}).done(function(o){o.status==-1?"verifyCode"==o.invalidInputs[0]?$(".errorpromp").html("验证码错误").show():"username"==o.invalidInputs[0]&&$(".errorpromp").html("用户名不存在").show():$.ajax({type:"post",url:"/user/login/submit",data:{username:n,password:e,errorCode:""},dataType:"json"}).done(function(n){history.go(0)}).error(function(n){$(".warning").show(),$(".submitlogin").css({"margin-top":0})})}).error(function(n){$(".errorpromp").html("验证码错误").show()}):r.test(e)||$(".errorpromp").html("密码格式6-30位").show()}),$(".closedButton").on("click",function(){$(".signLogin").hide(),$(".lostpasswordpage").hide(),$(".lightLogin").show()})}]);
//# sourceMappingURL=../maps/userLoginJs-0.6.23.js.map