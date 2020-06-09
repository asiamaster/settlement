<script>
    $(function() {
        $('#btn-certain-pay').click(payCertainClickHandler);
    });

    /** 支付页面确定按钮点击事件处理器 */
    function payCertainClickHandler() {
        let rows = $('#table-settle-order-list').bootstrapTable('getSelections');
        if (null == rows || rows.length === 0) {
            showWarning('请至少选中一条数据');
            return;
        }
        let idList = [];
        for (let row of rows) {
            idList.push(row.id);
        }
        let url = "/settleOrder/forwardPay.html?ids="+idList.join(",");
        bs4pop.dialog({
            id:'dialog-pay',
            content:url,
            title:'结算收费',
            isIframe:true,
            width: '95%',
            height: 600,
            btns:[
                {label: '取消',className: 'btn-secondary'},
                {label: '确定',className: 'btn-primary',onClick:dialogCertainClickHandler}
            ]
        });
    }

    /** 支付弹框确定按钮点击事件处理器 */
    function dialogCertainClickHandler(e, $iframe) {
        bui.loading.show('数据验证中，请稍候。。。');
        let win = $iframe[0].contentWindow;
        if (!win.validatePayForm()) {
            bui.loading.hide();
            return false;
        }
        bui.loading.hide();
        bui.loading.show('努力提交中，请稍候。。。');
        $.ajax({
            type:"POST",
            url:"/settleOrder/pay.action",
            dataType:"json",
            data:$iframe.contents().find('#form-pay').serialize(),
            success:function(result) {
                bui.loading.hide();
                if (result.code === '200') {
                    settleResultHandler(result.data);
                } else {
                    showError(result.message);
                }
            },
            error:function(){
                bui.loading.hide();
                showError("系统异常,请稍后重试");
            }
        });
    }

    /** 加载客户单据处理器 */
    function loadCustomerOrdersHandler(cusId) {
        $('#settle-order-list').removeClass("d-none");
        $('#table-settle-order-list').bootstrapTable("refreshOptions", {url:"/settleOrder/listPayOrders.action?customerId="+cusId});
    }
</script>
