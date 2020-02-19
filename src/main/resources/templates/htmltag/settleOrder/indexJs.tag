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
</script>