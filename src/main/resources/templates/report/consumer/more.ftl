<div class="consumer-more">
    <h1 class="report-title">买家分析</h1>
    <h2><label>买家昵称:&nbsp;</label>${Request.consumer.consumerNick}</h2>
    <div class="product-select">
        <input type="button" class="product-select-button" /><input type="text" disabled class="product-title" placeholder="点击图标选择商品进行购买行为分析"/>
        <script type="text/javascript">
            function closeProductSelectorDialog() {
                var dlg = $(".product-selector-dialog");
                dlg.hide();
                dlg.removeClass("slide-down-anim");
            }
            $(document).ready(function() {
                var container = document.getElementById('consumer-more-timeline');
                var options = {};
                var timeline = null;
                var legend = $(".consumer-more .legend");
                vaseline.addProductSelectionListener(function(productNumIid, productTitle) {
                    $(".product-select .product-title").val(productTitle);
                    closeProductSelectorDialog();
                    vaseline.fetchProductPurchaseTimeline(${RequestParameters.consumerId}, productNumIid, function(dataset) {
                        var items = new vis.DataSet(dataset);
                        if (timeline != null) {
                            timeline.destroy();
                        }
                        timeline = new vis.Timeline(container, items, options);
                        legend.show();
                    });
                });
            });
        </script>
        <div class="product-selector-dialog hide">
            <#include "../../_widget/purchased-product-selector.ftl">
        </div>
    </div>
    <div id="consumer-more-timeline"></div>
    <div class="legend hide">单品购买时间轴</div>
</div>

<script type="text/javascript">
    $(document).ready(function(){
        $(".product-select-button").bind('click', function() {
            var dlg = $(".product-selector-dialog");
            if (dlg.hasClass("slide-down-anim")) {
                closeProductSelectorDialog();
            } else {
                dlg.show();
                dlg.addClass("slide-down-anim");
            }
        });
    });
</script>