<#import "../../_widget/adhoc-time-ranges-builder.ftl" as widget>
<#import "../../_widget/multiple-product-selector.ftl" as multipleProductSelectorWidget>
<div class="single-product-purchase">
    <div class="report-title">商品销售趋势分析</div>
    <div class="description shadow-border">
        <pre>#商品销售趋势分析#允许您选择商品，比较其在不同时间区段的销售情况，从而检验不同时间段的商品销售策略的实施效果。</pre>
    </div>
    <div class="shadow-border conditions">
        <@widget.adhocTimeRangesBuilder wrapper_id="single-product-adhoc-time-ranges-builder" wrapper_css_classes="shadow-border" multiple=true/>

        <@multipleProductSelectorWidget.multipleProductSelector wrapper_id="single-product-multiple-product-selector"
            css_classes="multiple-product-selector" />

        <input type="submit" value="查询" class="submit" />
    </div>
    <div class="report-canvas-wrapper">
        <div>
            <canvas class="chart report-canvas"></canvas>
        </div>
        <div class="legends"></div>
    </div>
</div>
<script type="text/javascript">
    vaseline.initDateTimePickers();
    $(document).ready(function() {
        var self = this;
        var context = $(".single-product-purchase .report-canvas").get(0).getContext("2d");

        self.buildReport = function() {
            var timeRanges = vaseline.getTimeRanges("#single-product-adhoc-time-ranges-builder");
            var numberIids = builder.getSelectedProductsNumberIid().join("|");
            if (timeRanges != '') {
                vaseline.chartJsReport('/rest/report/product-purchase-time-range/payment/' + numberIids + '/yyyy-MM-dd/' + timeRanges,
                        {}, context, {
                            datasetFill: false,
                            bezierCurve: false,
                            responsive: true,
                            showTooltips: true,
                            datasetStrokeWidth: 6,
                            multiTooltipTemplate: "<%= value %>"
                        }, "bar", function (chart, datasets) {
                            var legends = $(".report-canvas-wrapper .legends");
                            legends.empty();
                            var ul = $("<ul></ul>");
                            ul.addClass("bar-legend");
                            legends.append(ul);
                            $.each(datasets, function (k, ds) {
                                var li = $("<li></li>");
                                ul.append(li);
                                //li.append($('<img class="icon" src="' + ds.picture + '"/>'));
                                li.append($('<div class="cube" style="background-color:' + ds.fillColor + '"></div>'));
                                li.append(document.createTextNode(ds.label));
                            });
                        });
            }
        }

        self.buildReport();

        $(".submit").click(function(event) {
            builder.closeDialog();
            self.buildReport();
        });
    });
</script>