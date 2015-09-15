<div class="centerwell consumer-more">
    <h1 class="report-title">买家分析</h1>
    <h2>${Request.consumer.consumerNick}</h2>
    <div class="product-select">
        <input type="button" class="product-select-button" /><input type="text" disabled class="product-title" />
        <script type="text/javascript">
            $(document).ready(function() {
                var container = document.getElementById('consumer-more-timeline');
                var options = {};
                var timeline = null;

                vaseline.addProductSelectionListener(function(productNumIid, productTitle) {
                    $(".product-select .product-title").val(productTitle);
                    $(".product-selector-dialog").hide();
                    vaseline.fetchProductPurchaseTimeline(${RequestParameters.consumerId}, productNumIid, function(dataset) {
                        var items = new vis.DataSet(dataset);
                        if (timeline != null) {
                            timeline.destroy();
                        }
                        timeline = new vis.Timeline(container, items, options);
                    });
                });
            });
        </script>
        <div class="product-selector-dialog hide">
            <#include "../../_widget/product-selector.ftl">
        </div>
    </div>
    <div id="consumer-more-timeline"></div>
    <div class="legend">单品购买时间轴</div>
</div>

<script type="text/javascript">
    $(document).ready(function(){
        $(".product-select-button").bind('click', function() {
            var dlg = $(".product-selector-dialog");
            if (dlg.is(":visible")) {
                dlg.hide();
            } else {
                dlg.show();
            }
        });
    });
</script>