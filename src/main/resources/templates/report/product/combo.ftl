<div class="product-combo">
    <div class="report-title">商品购买组合网络图谱</div>
    <div class="product-select">
        <input type="button" class="product-select-button" /><input type="text" disabled class="product-title" placeholder="点击图标选择商品进行特定商品购买组合分析"/>
        <script type="text/javascript">
            $(document).ready(function() {
                vaseline.addProductSelectionListener(function(productNumIid, productTitle) {
                    $(".product-select .product-title").val(productTitle);
                    vaseline.ComboNetwork.closeProductSelectorDialog();
                    vaseline.ComboNetwork.buildupNetwork(productNumIid);
                });

                $(".product-select-button").bind('click', function() {
                    var dlg = $(".product-selector-dialog");
                    if (dlg.hasClass("slide-down-anim")) {
                        vaseline.ComboNetwork.closeProductSelectorDialog();
                    } else {
                        dlg.show();
                        dlg.addClass("slide-down-anim");
                    }
                });
            });
        </script>
        <div class="product-selector-dialog hide">
        <#include "../../_widget/product-selector.ftl">
        </div>
    </div>
    <div class="product-combo-report">
        <div class="product-combo-selected-node">
            <div class="summary">
                <span>累计购买<label class="times"></label>次</span>
            </div>
            <table cellpadding="0" cellspacing="0">
            </table>
        </div>
        <div class="product-combo-network-topo-wrapper">
            <div id="product-combo-network-topo" class="hide"></div>
        </div>
    </div>
    <script type="text/javascript">vaseline.ComboNetwork.init();</script>
</div>