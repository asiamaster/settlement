<script>
    //时间范围
    lay('.laydatetime').each(function() {
        laydate.render({
            elem : this
            ,trigger : 'click'
            ,range: true
        });
    });

    function openInsertHandler() {
        let dia = bs4pop.dialog({
            id: 'addModal',
            title: '新增定金',//对话框title
            content: '/earnestOrder/add.html', //对话框内容，可以是 string、element，$object
            width: 900,//宽度
            height: 700,//高度
            isIframe : true,//默认是页面层，非iframe
        });
        // dia.modal('show');
        $('#addModal').modal('handleUpdate')
    }


    $('#getCustomers').on('click', function(){
        $('#customerTable').modal('show')
    })


    $('#choicePayway').on('click', function(){
        bs4pop.dialog({
            id: '',//'#xxx'，对话框ID，
            title: '结算收费',//对话框title
            content: '', //对话框内容，可以是 string、element，$object
            className: '', //自定义样式
            width: 900,//宽度
            height: '500',//高度
            target: 'body',//在什么dom内创建dialog
            isIframe : true,//默认是页面层，非iframe

            show: false,//是否在一开始时就显示对话框
            btns: [], //footer按钮 [{label: 'Button',    className: 'btn-primary',onClick(e){}}]

            onShowStart(){},//dialog开始打开时回调
            onShowEnd(){},//dialog打开完毕时回调
            onHideStart(){},//dialog开始关闭时回调
            onHideEnd(){},//dialog关闭完成时回调
            onClose(){},//关闭按钮点击时回调
            onDragStart(){},//拖动开始时回调
            onDragEnd(){},//拖动结束时回调
            onDrag(){}//拖动中回调
        }).show()
    })

    // bs4pop.alert('当前共缴费 '+ 5 +' 笔业务，'+ 2 + '笔成功，票据正在打印中...',undefined, function(){  })
</script>

<!--
http://127.0.0.1/earnestOrder/view.html 查看
http://127.0.0.1/earnestOrder/add.html 新增
http://127.0.0.1/earnestOrder/update.html 修改
-->
