    /**addEventListener(event,function,boolean)
       event:描述事件名称的字符串
       function:描述了事件触发后执行的函数
       boolean:可选。布尔值，指定事件是否 在捕获(true)或冒泡阶段(false)执行*/

    document.addEventListener("deviceready", onDeviceReady, true);

    function onDeviceReady(){
    //获取输入框和按钮对象

        var text1 = document.getElementById("name");
        var text2 = document.getElementById("Number");
        var text3 = document.getElementById("Age");
        var btn =document.getElementById("Enter");
        var getScreenbtn =document.getElementById("GetScreen");
        var getFacebookId =document.getElementById("FacebookId");
        var getGoogleId =document.getElementById("GoolgoeId");

        var screen = 'getscreenshot';
        var facebook = 'getFacebookId';
        var google = 'getGoogleId';

        getScreenbtn.addEventListener('click', function(){onNativeClick(screen)});
        getFacebookId.addEventListener('click', function(){onNativeClick(facebook)})
        getGoogleId.addEventListener('click', function(){onNativeClick(google)})

        function onNativeClick(val){
             var Name =document.getElementById("name").value;
             var success = function(message) {alert("Success" + message);};
             var error = function(message) {alert("Oopsie! " + message);};

             var personl = {name:Name};

             document.getElementById("msg").value = val;

             dataTransportPlugins.createEvent(personl, success, error);
        }
        //给对象加上监听
        //click很像java內建的method
        btn.addEventListener('click', onClick);
        function onClick(){

        //获取输入框值
            var Name =document.getElementById("name").value;
            var Number =document.getElementById("Number").value;
            var Age =document.getElementById("Age").value;
            var success = function(message) {
                alert("Success" + message);
                document.getElementById("msg").value = message;
                alert(document.getElementById("msg").value);
            };
            var error = function(message) { alert("Oopsie! " + message); };

            var arr = ["welcome_01","welcome_02","welcome_03","welcome_04","welcome_05"];

            document.getElementById("msg").value = 'dataTransport';

            var personl = {ImgType:Name, Animstyle:Number, tourImg:arr};
            //用插件调用值
            /*dataTransportPlugins.createEvent(Name, Number, Age, success, error);*/
            dataTransportPlugins.createEvent(personl, success, error);

        }
    }
    function success(str){
            return data;
    }
    //在html被调用的插件
        var dataTransportPlugins = {

            //在html被调用的方法
            createEvent: function(personl, successCallback, errorCallback) {

                cordova.exec(

                    //成功的回调
                    successCallback,

                    //失败的回调
                    errorCallback,

                    //java类插件名称
                    'DataTransportPlugin',

                    //执行插件的动作  DataTransportPlugin的Action名子
                    document.getElementById("msg").value,

                    //数据组这里以json形式
                    [{
                        "personl": personl,
                    }]
                );
            }
        }