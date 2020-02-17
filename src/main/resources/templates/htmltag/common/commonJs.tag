<script>
    /** 刷新表格处理器 */
    function refreshTableHandler() {
        $('#table-settle-order-list').bootstrapTable("refresh");
    }

    /** 业务编号格式器 */
    function businessCodeFormatter(value, row, index) {
        return '<a href="javascript:;" onclick="showBusinessDetailHandler('+row.businessType+','+row.businessCode+');return false;">'+value+'</a>'
    }

    /** 查看业务详情处理器 */
    function showBusinessDetailHandler(businessType, businessCode) {
        let url = "/urlConfig/showBusinessDetail.html?businessType="+businessType+"&businessCode="+businessCode;
        bs4pop.dialog({content:url, title:'业务详情',isIframe:true,width:700,height:500,btns:[{label: '取消',className: 'btn-secondary'}]});
    }

    /** 票据打印处理器 */
    function printHandler(settleOrder, reprint) {
        /*if(typeof(callbackObj) === "undefined"){
            return;
        }*/
        $.ajax({
            type:"POST",
            url:"/settleOrder/loadPrintData.action",
            dataType:"json",
            data:{
                "businessType":settleOrder.businessType,
                "businessCode":settleOrder.businessCode,
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
        bs4pop.alert(message, {type : "warning"})
    }
</script>