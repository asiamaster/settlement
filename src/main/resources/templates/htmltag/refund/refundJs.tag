<script>
    $(function() {
        //初始化
        wayRadioChangeHandler();
        $('input[name="way"]').change(wayRadioChangeHandler);
    });

    /** 支付方式值改变事件处理器 */
    function wayRadioChangeHandler() {
        let way = $('input[name="way"]:checked').val();
        if (way && way.length > 0) {
            $('#div-special').load("/settleOrder/forwardRefundSpecial.html?way="+way+"&ids="+$('#ids').val());
        }
    }
</script>