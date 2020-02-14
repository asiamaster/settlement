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
            width:700,
            height:400,
            btns:[
                {label: '确定',className: 'btn-primary',onClick:function(){}},
                {label: '取消',className: 'btn-secondary'}
            ]
        });
    }

    /** 加载客户单据处理器 */
    function loadCustomerOrdersHandler(cusId) {
        $('#table-settle-order-list').bootstrapTable("refreshOptions", {url:"/settleOrder/listPayOrders.action?customerId="+cusId});
    }
    //时间范围
    lay('.laydatetime').each(function() {
        laydate.render({
            elem : this
            ,trigger : 'click'
            ,range: true
        });
    });
    // bs4pop.alert('当前共缴费 '+ 5 +' 笔业务，'+ 2 + '笔成功，票据正在打印中...',undefined, function(){  })
</script>
