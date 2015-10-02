/**
 * Created by weiliyang on 9/1/15.
 */
Chart.types.Bar.extend({
    name: "Bar2Y",
    getScale: function (data) {
        var startPoint = this.options.scaleFontSize;
        var endPoint = this.chart.height - (this.options.scaleFontSize * 1.5) - 5;
        return Chart.helpers.calculateScaleRange(
            data,
            endPoint - startPoint,
            this.options.scaleFontSize,
            this.options.scaleBeginAtZero,
            this.options.scaleIntegersOnly);
    },
    initialize: function (data) {
        var y2datasetLabels = [];
        var y2data = [];
        var y1data = [];
        data.datasets.forEach(function (dataset, i) {
            if (dataset.y2axis == true) {
                y2datasetLabels.push(dataset.label);
                y2data = y2data.concat(dataset.data);
            } else {
                y1data = y1data.concat(dataset.data);
            }
        });

        // use the helper function to get the scale for both datasets
        var y1Scale = this.getScale(y1data);
        this.y2Scale = this.getScale(y2data);
        var normalizingFactor = y1Scale.max / this.y2Scale.max;

        // update y2 datasets
        data.datasets.forEach(function (dataset) {
            if (y2datasetLabels.indexOf(dataset.label) !== -1) {
                dataset.data.forEach(function (e, j) {
                    dataset.data[j] = e * normalizingFactor;
                })
            }
        })

        // denormalize tooltip for y2 datasets
        this.options.multiTooltipTemplate = function (d) {
            if (y2datasetLabels.indexOf(d.datasetLabel) !== -1)
                return Math.round(d.value / normalizingFactor, 6);
            else
                return d.value;
        }

        Chart.types.Bar.prototype.initialize.apply(this, arguments);
    },
    draw: function () {
        this.scale.xScalePaddingRight = this.scale.xScalePaddingLeft;
        Chart.types.Bar.prototype.draw.apply(this, arguments);

        this.chart.ctx.textAlign = 'left';
        this.chart.ctx.textBaseline = "middle";
        this.chart.ctx.fillStyle = "#666";
        var yStep = (this.scale.endPoint - this.scale.startPoint) / this.y2Scale.steps
        for (var i = 0, y = this.scale.endPoint, label = this.y2Scale.min;
             i <= this.y2Scale.steps;
             i++) {
            this.chart.ctx.fillText(label, this.chart.width - this.scale.xScalePaddingRight + 10, y);
            y -= yStep;
            label += this.y2Scale.stepValue
        }
    }
});

function Vaseline() {
    var self = this;
    self.charts = [];
    self.productSelectionListeners = [];
    self.colors = ['#99CCFF', '#FFCC00', '#99CC99', '#FF9966', '#FFCC99', '#3399FF', '#003366'];

    self.init = function () {
        Chart.defaults.global.responsive = true;

        $(".leftnav .anchor-report-total-fee").bind('click', function (e) {
            self.totalFeeReport(".report-canvas", ".report-title");
            e.preventDefault();
        });
        $(".leftnav .anchor-report-volumn").bind('click', function (e) {
            self.volumnReport(".report-canvas", ".report-title");
            e.preventDefault();
        });
    };

    self.volumnReport = function (context, startDateSelector, endDateSelector) {
        $.getJSON("rest/report/total-volumn", {
            startDateTime: $(startDateSelector).val(),
            endDateTime: $(endDateSelector).val(),
            dateTimeFormat: "yyyy-MM-dd"
        }, function (response) {
            var chart = self.charts[context];
            if (chart != null) {
                chart.destroy();
            }
            chart = new Chart(context).Bar(response, {});
            self.charts[context] = chart;
        });
    };

    self.totalFeeReport = function (context, startDateSelector, endDateSelector) {
        $.getJSON("rest/report/total-fee", {
            startDateTime: $(startDateSelector).val(),
            endDateTime: $(endDateSelector).val(),
            dateTimeFormat: "yyyy-MM-dd"
        }, function (response) {
            var chart = self.charts[context];
            if (chart != null) {
                chart.destroy();
            }
            chart = new Chart(context).Bar(response, {});
            self.charts[context] = chart;
        });
    };

    self.chartJsReport = function (jsonUrl, data, context, chartOptions, chartType, callback) {
        $.getJSON(jsonUrl, data, function (response) {
            var chart = self.charts[context];
            if (chart != null) {
                chart.destroy();
            }
            self.fillColorsInDatasets(response.datasets);
            chart = new Chart(context).Bar(response, chartOptions);
            self.charts[context] = chart;

            callback(chart, response.datasets);
        });
    };

    self.fillColorsInDatasets = function (datasets) {
        var l = self.colors.length;
        for (var i = 0; i < datasets.length; ++i) {
            var ds = datasets[i];
            ds.fillColor = self.colors[i % l];
        }
    };

    self.fetchProducts = function (tableBodySelector, page) {
        var tableBody = $(tableBodySelector);
        tableBody.empty();

        $.getJSON("/rest/report/products/" + page, function (response) {
            $.each(response, function (k, v) {
                var row = $('<tr class="product"></tr>');
                row.append($('<td class="picture"><img src="' + v.picturePath + '"/></td>'));
                row.append($('<td class="title">' + v.title + '</td>'));
                row.append($('<td class="price">' + v.price + '</td>'));

                tableBody.append(row);
            });
        });
    };

    self.callPaginationApi = function (api, page, callback) {
        $.getJSON(api + "/" + page, function (response) {
            callback(response);
        });
    };

    self.fetchConsumers = function (page, callback) {
        $.getJSON("/rest/report/consumers/" + page, function (response) {
            callback(response);
        });
    };

    self.fetchProductPurchasedByConsumer = function (consumerId, page, callback) {
        $.getJSON("/rest/report/product-purchase/product/" + consumerId + "/" + page, function (response) {
            callback(response);
        });
    };

    self.fetchProductPurchaseTimeline = function (consumerId, numberIid, callback) {
        $.getJSON("/rest/report/product-purchase/timeline/" + consumerId + "/" + numberIid, function (dataset) {
            callback(dataset);
        });
    };

    self.addProductSelectionListener = function (listener) {
        if (listener != null && listener != undefined) {
            self.productSelectionListeners.push(listener);
        }
    };

    self.onProductSelected = function (productNumIid, productTitle) {
        for (var i = 0; i < self.productSelectionListeners.length; ++i) {
            var func = self.productSelectionListeners[i];
            func(productNumIid, productTitle);
        }
    };

    self.ComboNetwork = new (function () {
        var self = this;
        self.network = null;
        self.cachedUserObjects = [];
        self.visContainerId = 'product-combo-network-topo';

        self.init = function () {
            self.jQueryTopoWrapper = $(".product-combo-network-topo-wrapper");
            self.jQueryTopo = $("#product-combo-network-topo");
            self.jQueryComboNodes = $(".product-combo-selected-node");
            self.jQueryComboNodesTable = $(".product-combo-selected-node table");
            self.jQueryComboNodesSummaryTimes = $(".product-combo-selected-node .summary .times");
        };

        self.cacheUserObjects = function (groupNodeNumberIid, userObjects) {
            if (groupNodeNumberIid == null || groupNodeNumberIid == undefined) {
                return;
            }

            self.cachedUserObjects[groupNodeNumberIid] = userObjects;
        };

        self.cacheUserObjectsOfGroupNodes = function (nodes) {
            self.cachedUserObjects = [];
            for (var i = 0; i < nodes.length; ++i) {
                var node = nodes[i];
                self.cacheUserObjects(node.id, node.userObjects);
            }
        };

        self.closeProductSelectorDialog = function () {
            var dlg = $(".product-selector-dialog");
            dlg.hide();
            dlg.removeClass("slide-down-anim");
        };

        self.collapseNetwork = function () {
            if (!self.jQueryTopoWrapper.hasClass("show-connected-nodes")) {
                self.jQueryTopoWrapper.addClass("show-connected-nodes");
                self.network.setSize(self.jQueryTopoWrapper.outerWidth() + 'px', '80%');
            }

            if (!self.jQueryComboNodes.hasClass("show-connected-nodes")) {
                self.jQueryComboNodes.addClass("show-connected-nodes");
            }
        };
        self.expandNetwork = function () {
            if (self.jQueryTopoWrapper.hasClass("show-connected-nodes")) {
                self.jQueryTopoWrapper.removeClass("show-connected-nodes");
                if (self.network != null && self.network != undefined) {
                    try {
                        self.network.setSize(self.jQueryTopoWrapper.outerWidth() + 'px', '80%');
                    } catch (err) {
                        console.log(err);
                    }
                }
            }
            self.jQueryTopo.show();

            if (self.jQueryComboNodes.hasClass("show-connected-nodes")) {
                self.jQueryComboNodes.removeClass("show-connected-nodes");
            }
        };

        self.buildupNetwork = function (productNumberIid) {
            var jsonUrl = "/rest/report/product-purchase-combo/";
            if (productNumberIid != null) {
                jsonUrl = jsonUrl + productNumberIid + "/";
            }
            $.getJSON(jsonUrl, function (response) {
                console.log(response.nodes.length + " nodes.");
                console.log(response.edges.length + " edges.");
                self.cacheUserObjectsOfGroupNodes(response.nodes);
                if (self.network != null) {
                    self.network.destroy();
                }

                self.expandNetwork();
                var options = {
                    clickToUse: true,
                    autoResize: true,
                    height: '80%',
                    width: self.jQueryTopo.parent().outerWidth() + 'px',
                    physics: {
                        stabilization: {enabled: true, iterations: 15000, fit: true}
                    },
                    interaction: {hover: true,},
                    nodes: {
                        shape: 'dot',
                        color: {
                            hover: {background: '#fcf141'},
                            highlight: {background: '#ffffff'}
                        }
                    },
                    edges: {
                        color: {hover: '#fcf141'}
                    },
                    groups: {
                        useDefaultGroups: false,
                        group1: {color: {background: 'red'}, borderWidth: 3}
                    }
                };
                var container = document.getElementById(self.visContainerId);
                self.network = new vis.Network(container, response, options);
                self.network.on("selectNode", function (event) {
                    var selectedNodeId = event.nodes[0];
                    console.log(selectedNodeId);
                    var userObjects = self.cachedUserObjects[selectedNodeId];
                    if (userObjects != null && userObjects != undefined) {
                        self.collapseNetwork();
                        self.jQueryComboNodesTable.empty();
                        for (var j = 0; j < userObjects.length; ++j) {
                            var uo = userObjects[j];
                            self.jQueryComboNodesSummaryTimes.text(uo.value);
                            var tr = $("<tr></tr>");
                            tr.append($('<td class="picture"><img src="' + uo.picture + '" /></td>'))
                            tr.append($('<td class="title">' + uo.label + '</td>'));
                            self.jQueryComboNodesTable.append(tr);
                        }
                    }
                });
            });
        };
    });

    self.initDateTimePickers = function () {
        jQuery('.datetimepicker').datetimepicker({
            lang: 'zh',
            i18n: {
                zh: {
                    months: [
                        '一月', '二月', '三月', '四月',
                        '五月', '六月', '七月', '八月',
                        '九月', '十月', '十一月', '十二月',
                    ],
                    dayOfWeek: [
                        "周日", "周一", "周二", "周三",
                        "周四", "周五", "周六",
                    ]
                }
            },
            timepicker: false,
            format: 'Y-m-d'
        });
    };
}