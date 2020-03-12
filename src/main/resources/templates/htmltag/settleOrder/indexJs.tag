<script>
    $(function() {
        //初始化业务类型选择框
        queryBusinessTypeHandler();
        $('.chk-app-all').change(function() {
            $('.chk-app-item').prop("checked", this.checked);
            queryBusinessTypeHandler();
        });

        $('.chk-app-item').change(function() {
            if ($('.chk-app-item:checked').length === $('.chk-app-item').length){
                $('.chk-app-all').prop("checked", true);
            } else {
                $('.chk-app-all').prop("checked", false);
            }
            queryBusinessTypeHandler();
        });
        $('.chk-type-all').on("change", function() {
            $('.chk-type-item').prop("checked", this.checked);
        });

        $('.chk-type-item').on("change", function() {
            if ($('.chk-type-item:checked').length === $('.chk-type-item').length){
                $('.chk-type-all').prop("checked", true);
            } else {
                $('.chk-type-all').prop("checked", false);
            }
        });
        //时间范围
        lay('.laydatetime').each(function () {
            laydate.render({
                elem: this
                , trigger: 'click'
                , type: 'datetime'
            });
        });

        $('#btn-reprint').click(reprintClickHandler);

        $(window).resize(function () {
            $('#grid').bootstrapTable('resetView')
        });
        queryDataHandler();
    });

    /**
     * 查询处理
     */
    function queryDataHandler() {
        $('#grid').bootstrapTable('refreshOptions', {url: '/settleOrder/listPage.action'});
    }

    /**
     * table参数组装
     * 可修改queryParams向服务器发送其余的参数
     * @param params
     */
    function queryParams(params) {
        let temp = {
            rows: params.limit,   //页面大小
            page: ((params.offset / params.limit) + 1) || 1, //页码
            sort: params.sort,
            order: params.order
        }
        return $.extend(temp, bui.util.bindGridMeta2Form('grid', 'queryForm'));
    }

    /** 查询业务类型 */
    function queryBusinessTypeHandler() {
        let arr = $('.chk-app-item:checked');
        if (arr.length === 0) {
            $('#div-business-type').empty();
            return;
        }
        let appIds = [];
        for (let temp of arr) {
            appIds.push($(temp).val());
        }
        $.ajax({
            url:"/applicationConfig/listBusinessType.action",
            type:"POST",
            dataType:"json",
            async:false,
            data:{
                "appIds":appIds.join(",")
            },
            success:function(result) {
                if (result.code === '200') {
                    $('#div-business-type').html(template("template-business-type", {businessTypeList:result.data}));
                } else {
                    //showError(result.message);
                }
            },
            error:function() {
                showError("系统异常,请稍后重试");
            }
        });
    }

    /** 补打按钮点击事件处理器 */
    function reprintClickHandler() {
        let rows = $('#grid').bootstrapTable('getSelections');
        if (null == rows || rows.length === 0) {
            showWarning('请至少选中一条数据');
            return;
        }
        let row = rows[0];
        if (!row.printEnable) {
            showWarning("已处理记录才能补打票据");
            return;
        }
        let message = '是否确认补打 '+row.businessCode+' 票据?';
        bs4pop.confirm(message, {}, function(sure) {
            if (sure) {
                bui.loading.show("票据打印中,请稍后。。。");
                printHandler(row.appId, row.businessType, row.businessCode, 2);
                bui.loading.hide();
            }
        });
    }
</script>

<script id="template-business-type" type="text/html">
    {{each businessTypeList type index}}
        <div class="form-group col-auto">
            <div class="form-check form-check-inline">
                <input class="form-check-input chk-type-item" type="checkbox" name="businessTypeList[{{index}}]" value="{{type.code}}" checked>
                <label class="form-check-label">{{type.val}}</label>
            </div>
        </div>
    {{/each}}
</script>