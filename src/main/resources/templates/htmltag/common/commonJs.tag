<script>
    /** 刷新表格处理器 */
    function refreshTableHandler() {
        $('#table-settle-order-list').bootstrapTable("refresh");
    }

    /** 业务编号格式器 */
    function businessCodeFormatter(value, row, index) {
        if (row.reverse === ${@com.dili.settlement.enums.ReverseEnum.YES.getCode()}) {
            return;
        }
        return '<a href="javascript:;" onclick="showBusinessDetailHandler('+row.type+','+row.appId+','+row.businessType+',\''+row.orderCode+'\');return false;">'+value+'</a>';
    }

    /** 查看业务详情处理器 */
    function showBusinessDetailHandler(settleType, appId, businessType, orderCode) {
        let url = "/settleOrder/showDetail.html?settleType="+settleType+"&appId="+appId+"&businessType="+businessType+"&orderCode="+orderCode;
        bs4pop.dialog({content:url, title:'业务详情',isIframe:true,width:'80%',height:'95%',btns:[{label: '取消',className: 'btn-secondary'}]});
    }

    /** 票据打印处理器 */
    function printHandler(settleType, appId, businessType, orderCode, reprint) {
        if(typeof(callbackObj) === "undefined"){
            return;
        }
        window.printFinish = function() {}
        $.ajax({
            type:"POST",
            url:"/settleOrder/loadPrintData.action",
            dataType:"json",
            data:{
                "settleType":settleType,
                "appId":appId,
                "businessType":businessType,
                "orderCode":orderCode,
                "reprint":reprint
            },
            success:function(result) {
                if (result.code === '200') {
                    callbackObj.boothPrintPreview(JSON.stringify(result.data.item), result.data.name, 0);
                }
            },
            error:function() {

            }
        });
    }

    /** 园区刷卡处理 */
    function cardReader(callback) {
        setTimeout(function(){
            var result = callbackObj.readCardNumber();
            if(result === undefined || $.trim(result) === ""){
                return;
            }
            var info = eval('(' + result + ')');
            if(typeof(info)=="undefined") {
                showInfo("请检查读取园区卡的设备是否已连接");
            } else if(info.success){
                callback(info.data);
            } else {
                showInfo(info.message);
            }
        }, 20);
    }

    /** 身份证刷卡处理 */
    function idCardReader(callback) {
        setTimeout(function(){
            var result = callbackObj.readIDCard();
            if(result === undefined || $.trim(result) === ""){
                return;
            }
            var info = eval('(' + result + ')');
            if(typeof(info)=="undefined") {
                showInfo("请检查读取身份证的设备是否已连接");
            } else {
                callback(info.IDCardNo);
            }
        }, 20);
    }

    /** 密码设备处理 */
    function passwordReader(callback) {
        setTimeout(function(){
            var result = callbackObj.readPasswordKeyboard();
            if(result === undefined || $.trim(result) === ""){
                return;
            }
            var info = eval('(' + result + ')');
            if(typeof(info)=="undefined") {
                showInfo("请检查读取身份证的设备是否已连接");
            } else if(info.success){
                callback(info.data);
            } else {
                showInfo(info.message);
            }
        }, 20);
    }

    /** 查询账户信息 */
    function queryAccountHandler(cardNo, callback) {
        $.ajax({
            url:"/accountQuery/simpleInfo.action?cardNo=" + cardNo,
            type:"GET",
            dataType:"json",
            success:function (ret) {
                if (ret.success) {
                    callback(ret.data);
                } else {
                    showError(ret.message);
                }
            },
            error:function () {
                showError("查询账户信息失败,请稍后重试");
            }
        });
    }

    /** 错误消息提示框 */
    function showError(message) {
        bs4pop.alert(message, {type : "error"});
    }

    /** 提示消息弹出框 */
    function showInfo(message) {
        bs4pop.alert(message, {type : "info"});
    }

    /** 警示消息框 */
    function showWarning(message) {
        bs4pop.alert(message, {type : "warning"});
    }
</script>