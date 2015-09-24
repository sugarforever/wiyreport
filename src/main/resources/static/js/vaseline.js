/**
 * Created by weiliyang on 9/1/15.
 */
function Vaseline() {
    var self = this;
    self.charts = [];
    self.productSelectionListeners = [];

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

        self.init = function() {
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

        self.collapseNetwork = function() {
            if (!self.jQueryTopoWrapper.hasClass("show-connected-nodes")) {
                self.jQueryTopoWrapper.addClass("show-connected-nodes");
                self.network.setSize(self.jQueryTopoWrapper.outerWidth() + 'px', '80%');
            }

            if (!self.jQueryComboNodes.hasClass("show-connected-nodes")) {
                self.jQueryComboNodes.addClass("show-connected-nodes");
            }
        };
        self.expandNetwork = function() {
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
                            highlight: {background: '#ffffff'}}
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
}