<div class="product-combo">
    <h1 class="report-title">商品购买组合网络图谱</h1>
    <div class="product-select">
        <input type="button" class="product-select-button" /><input type="text" disabled class="product-title" placeholder="点击图标选择商品进行特定商品购买组合分析"/>
        <script type="text/javascript">
            function closeProductSelectorDialog() {
                var dlg = $(".product-selector-dialog");
                dlg.hide();
                dlg.removeClass("slide-down-anim");
            }
            $(document).ready(function() {
                vaseline.addProductSelectionListener(function(productNumIid, productTitle) {
                    $(".product-select .product-title").val(productTitle);
                    closeProductSelectorDialog();
                    buildupNetwork(productNumIid);
                });

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
        <div class="product-selector-dialog hide">
        <#include "../../_widget/product-selector.ftl">
        </div>
    </div>
    <div id="product-combo-network-topo"></div>
    <script type="text/javascript">
        var network = null;
        $(document).ready(function() {
            buildupNetwork(null);
        });
        function buildupNetwork(productNumberIid) {
            var jsonUrl = "/rest/report/product-purchase-combo/";
            if (productNumberIid != null) {
                jsonUrl = jsonUrl + productNumberIid + "/";
            }
            $.getJSON(jsonUrl, function(response) {
                if (network != null) {
                    network.destroy();
                }
                var container = document.getElementById('product-combo-network-topo');
                var options = {
                    interaction: {
                        hover: true,
                    },
                    nodes: {
                        shape: 'dot',
                        color: {
                            hover: {
                                background: '#fcf141'
                            }
                        }
                    },
                    edges: {
                        color: {
                            hover: '#fcf141'
                        }
                    }
                };
                network = new vis.Network(container, response, options);
            });
        }
    </script>
</div>