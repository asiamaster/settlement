<script>
    /** 页面初始化函数 */
    $(function() {
        //初始化查询选项
        queryTypeChangeHandler();
        $('input[name="queryType"]').change(queryTypeChangeHandler);
        $('#btn-swipe-card').click(swipeCardClickHandler);
        $('#btn-clear').click(clearClickHandler);
        $('#keyword').keydown(function(e) {
            if (e.keyCode === 13) {
                enterClickHandler();
            }
        });
    });

    /** 查询单选框change事件处理器 */
    function queryTypeChangeHandler() {
        let queryType = $('input[name="queryType"]:checked').val();
        switch (queryType) {
            case "1":
                $('#keyword').val("").prop("placeholder", "请输入客户姓名");
                $('#btn-swipe-card').parent().addClass("d-none");
                $('#btn-query').unbind("click");
                $('#btn-query').click(queryByNameHandler);
                break;
            case "2":
                $('#keyword').val("").prop("placeholder", "请输入证件号或刷卡");
                $('#btn-swipe-card').parent().removeClass("d-none");
                $('#btn-query').unbind("click");
                $('#btn-query').click(queryByCertificateHandler);
                break;
            default:
                $('#keyword').val("").prop("placeholder", "请输入客户姓名");
                $('#btn-swipe-card').parent().addClass("d-none");
                $('#btn-query').unbind("click");
                $('#btn-query').click(queryByNameHandler);
                break;
        }
    }

    /** 根据客户姓名查询处理器 */
    function queryByNameHandler() {
        let keyword = $('#keyword').val();
        if (keyword === undefined || $.trim(keyword) === '') {
            return;
        }
        let params = {};
        params.name = $.trim(keyword);
        requestCustomerHandler(params);
    }

    /** 根据证件号查询处理器 */
    function queryByCertificateHandler() {
        let keyword = $('#keyword').val();
        if (keyword === undefined || $.trim(keyword) === '') {
            return;
        }
        let params = {};
        params.certificateNumber = $.trim(keyword);
        requestCustomerHandler(params);
    }

    /** 请求客户数据处理器 */
    function requestCustomerHandler(params) {
        $.ajax({
            url:"/customer/list.action",
            type:"POST",
            dataType:"json",
            data:params,
            success:function(result) {
                if (result.code === '200') {
                    requestCustomerCompleteHandler(result.data);
                } else {
                    showError(result.message);
                }
            },
            error:function() {
                showError("系统异常,请稍后重试");
            }
        });
    }

    /** 请求客户完成处理器 */
    function requestCustomerCompleteHandler(arr) {
        if (arr.length === 0) {
            showInfo("未查询到客户记录");
            return;
        }
        if (arr.length === 1) {
            certainCustomerHandler(arr[0]);
            return;
        }
        //$('#table-customer-list > tbody').html(template('template-customer-list', {customers : arr}));
        $('#dialog-customer-list').modal('show');
        //bs4pop.dialog({content:$('#customer-list').html(), title:'选择客户'});
    }

    /** 确定唯一客户处理器 */
    function certainCustomerHandler(cus) {
        $('#customer-info').removeClass("d-none");
        $('#customer-info').html(template('template-customer-info', cus));
        loadCustomerOrdersHandler(cus.id);
    }

    /** 加载客户单据处理器 */
    function loadCustomerOrdersHandler(cusId) {
        //TODO 待完成
    }

    /** 选择客户完成处理器 */
    function chooseCustomerCompleteHandler() {
        //TODO 待完成
    }

    /** 清除按钮点击事件处理器 */
    function clearClickHandler() {
        $('#keyword').val("");
    }

    /** 刷卡按钮点击事件处理器 */
    function swipeCardClickHandler() {
        //TODO 待完成
        console.log("11111111");
    }

    /** 关键字文本框回车事件处理器 */
    function enterClickHandler() {
        let queryType = $('input[name="queryType"]:checked').val();
        switch (queryType) {
            case "1":
                queryByNameHandler();
                break
            case "2":
                queryByCertificateHandler();
                break;
        }
    }

    /** 错误消息提示框 */
    function showError(message) {
        bs4pop.alert(message, {type : "error"});
    }

    /** 提示消息弹出框 */
    function showInfo(message) {
        bs4pop.alert(message, {});
    }
</script>
<script id="template-customer-info" type="text/html">
    <div class="col-1">{{name}}</div>
    <div class="col-1">{{cellphone}}</div>
    <div class="col-2">{{certificateNumber}}</div>
    <div class="col-2">{{certificateAddr}}</div>
</script>
<script id="template-customer-list" type="text/html">
    {{each customers cus index}}
    <tr>
        <td></td>
        <td>{{cus.name}}</td>
        <td>{{cus.certificateNumber}}</td>
        <td>{{cus.certificateAddr}}</td>
    </tr>
    {{/each}}
</script>