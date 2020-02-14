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
        //选择客户列表 确定按钮事件
        $('#btn-choose-customer').click(function () {
            let arr = $('input[name="customerRadio"]:checked');
            if (arr.length === 0) {
                showInfo("请先选择客户");
                return;
            }
            let row = $(arr[0]).closest('tr');
            chooseCustomerCompleteHandler(row);
        });
        $('#dialog-customer-list').on("dblclick", 'table tr', function() {
            let row = $(this).closest('tr');
            chooseCustomerCompleteHandler(row);
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
        $('#dialog-customer-list .modal-body').html(template('template-customer-list', {customers : arr}));
        $('#dialog-customer-list').modal('show');
    }

    /** 选择客户完成处理器 */
    function chooseCustomerCompleteHandler(row) {
        let cus = {};
        cus.id = row.attr("bind-id");
        cus.name = row.attr("bind-name");
        cus.cellphone = row.attr("bind-cellphone");
        cus.certificateNumber = row.attr("bind-certificate");
        cus.certificateAddr = row.attr("bind-addr");
        $('#dialog-customer-list').modal('hide');
        certainCustomerHandler(cus);
    }

    /** 确定唯一客户处理器 */
    function certainCustomerHandler(cus) {
        $('#customer-info').removeClass("d-none");
        $('#customer-info').html(template('template-customer-info', cus));
        loadCustomerOrdersHandler(cus.id);
    }

    /** 清除按钮点击事件处理器 */
    function clearClickHandler() {
        $('#keyword').val("");
    }

    /** 刷卡按钮点击事件处理器 */
    function swipeCardClickHandler() {
        $("#keyword").val("");
        if(typeof(callbackObj) === "undefined"){
            return;
        }
        setTimeout(function(){
            var card = callbackObj.readIDCard();
            if(card === undefined || $.trim(card) === ""){
                return;
            }
            var info = eval('(' + card + ')');
            if(typeof(info)=="undefined"){
                showInfo("请检查读取身份证的设备是否已连接");
            }else{
                $("#keyword").val(info.IDCardNo);
            }
        },50);
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

    /** 业务编号格式器 */
    function businessCodeFormatter(value, row, index) {
        return '<a href="javascript:;" onclick="showBusinessDetailHandler('+row.businessType+','+row.businessCode+');return false;">'+value+'</a>'
    }

    /** 查看业务详情处理器 */
    function showBusinessDetailHandler(businessType, businessCode) {
        let url = "/urlConfig/showBusinessDetail.html?businessType="+businessType+"&businessCode="+businessCode;
        bs4pop.dialog({content:url, title:'业务详情',isIframe:true,width:700,height:500,btns:[{label: '取消',className: 'btn-secondary'}]});
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
<script id="template-customer-info" type="text/html">
    <div class="col-1">{{name}}</div>
    <div class="col-1">{{cellphone}}</div>
    <div class="col-2">{{certificateNumber}}</div>
    <div class="col-2">{{certificateAddr}}</div>
</script>
<script id="template-customer-list" type="text/html">
    <table id="table-customer-list" class="table table-bordered table-hover">
        <thead>
            <tr>
                <th class="text-center"></th>
                <th class="text-center">客户名称</th>
                <th class="text-center">证件号</th>
                <th class="text-center">地址信息</th>
            </tr>
        </thead>
        <tbody>
            {{each customers cus index}}
                <tr bind-id="{{cus.id}}" bind-name="{{cus.name}}" bind-cellphone="{{cus.cellphone}}" bind-certificate="{{cus.certificateNumber}}" bind-addr="{{cus.certificateAddr}}">
                    <td class="text-center"><input type="radio" name="customerRadio" value="{{cus.id}}"/></td>
                    <td class="text-center">{{cus.name}}</td>
                    <td class="text-center">{{cus.certificateNumber}}</td>
                    <td class="text-center">{{cus.certificateAddr}}</td>
                </tr>
            {{/each}}
        </tbody>
    </table>
</script>