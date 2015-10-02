<#import "../../_widget/adhoc-time-ranges-builder.ftl" as widget>
<div class="single-product-purchase">
    <div class="report-title">商品销售趋势分析</div>
    <@widget.adhocTimeRangesBuilder wrapper_id="single-product-adhoc-time-ranges-builder" />
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
        var context = $(".single-product-purchase .report-canvas").get(0).getContext("2d");
        vaseline.chartJsReport('/rest/report/product-purchase-time-range/payment/2885036401|2892864413|2901471491|2901510891|2901544137/yyyy-MM-dd/2015-08-21|2015-08-26|2015-09-01|2015-09-06|2015-09-11|2015-09-16',
                {}, context, {
                    datasetFill : false,
                    bezierCurve : false,
                    responsive: true,
                    showTooltips: true,
                    datasetStrokeWidth: 6,
                    multiTooltipTemplate: "<%= value %>",
                    legendTemplate : "<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<datasets.length; i++){%><li><img class=\"icon\" src=\"<%=datasets[i].picture%>\"></img><div class=\"cube\" style=\"background-color:<%=datasets[i].fillColor%>\"></div><%if(datasets[i].label){%><%=datasets[i].label%><%}%></li><%}%></ul>"
                }, "bar", function(chart, datasets) {
                    var legends = $(".report-canvas-wrapper .legends");
                    legends.empty();
                    var ul = $("<ul></ul>");
                    ul.addClass("bar-legend");
                    legends.append(ul);
                    $.each(datasets, function(k, ds) {
                        var li = $("<li></li>");
                        ul.append(li);
                        //li.append($('<img class="icon" src="' + ds.picture + '"/>'));
                        li.append($('<div class="cube" style="background-color:' + ds.fillColor + '"></div>'));
                        li.append(document.createTextNode(ds.label));
                    });
                });
        /*$(".datetime-form").submit(function(event) {
            vaseline.totalFeeReport(context, "#datetime-start-total-fee", "#datetime-end-total-fee");
            event.preventDefault();
        });
        vaseline.totalFeeReport(context, "#datetime-start-total-fee", "#datetime-end-total-fee");*/
    });
</script>