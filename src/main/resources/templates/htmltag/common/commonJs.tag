<script>
    /** 刷新表格处理器 */
    function refreshTableHandler() {
        $('#table-settle-order-list').bootstrapTable("refresh");
    }

    /** 业务编号格式器 */
    function businessCodeFormatter(value, row, index) {
        return '<a href="javascript:;" onclick="showBusinessDetailHandler('+row.type+','+row.appId+','+row.businessType+',\''+row.orderCode+'\');return false;">'+value+'</a>'
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
                    callbackObj.printPreview(JSON.stringify(result.data.item), result.data.name, 0);
                }
            },
            error:function() {

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