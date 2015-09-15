<div class="consumer-selector">
    <div class="title">选择买家</div>
    <ul></ul>
</div>
<script type="text/javascript">
    var consumerSelectorUl = $(".consumer-selector ul");
    vaseline.fetchConsumers(0, function(consumers) {
        consumerSelectorUl.empty();
        $.each(consumers, function(k, consumer) {
            consumerSelectorUl.append('<li><input type="checkbox" name="consumer" class="consumer"/><span>' + consumer.consumerNick + '</span></li>');
        });
    });
</script>
