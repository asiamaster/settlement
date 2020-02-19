<script>
    $(function() {
        $('.chk-type-all').change(function() {
            $('.chk-type-item').prop("checked", this.checked);
        });

        $('.chk-type-item').change(function() {
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

    /** 补打按钮点击事件处理器 */
    function reprintClickHandler() {
        let rows = $('#grid').bootstrapTable('getSelections');
        if (null == rows || rows.length === 0) {
            showWarning('请至少选中一条数据');
            return;
        }
        let row = rows[0];
        let message = '是否确认补打 '+row.businessCode+' 票据?';
        bs4pop.confirm(message, {}, function(sure) {
            if (sure) {
                bui.loading.show("票据打印中,请稍后。。。");
                printHandler(row.businessType, row.businessCode, 2);
                bui.loading.hide();
            }
        });
    }
</script>