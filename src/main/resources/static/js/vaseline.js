/**
 * Created by weiliyang on 9/1/15.
 */
function Vaseline() {
    var self = this;
    self.charts = [];

    self.init = function() {
        Chart.defaults.global.responsive = true;

        $(".leftnav .anchor-report-total-fee").bind('click', function(e) {
            self.totalFeeReport(".report-canvas", ".report-title");
            e.preventDefault();
        });
        $(".leftnav .anchor-report-volumn").bind('click', function(e) {
            self.volumnReport(".report-canvas", ".report-title");
            e.preventDefault();
        });
    };

    self.volumnReport = function(context, startDateSelector, endDateSelector) {
        $.getJSON("rest/report/total-volumn", {
            startDateTime: $(startDateSelector).val(),
            endDateTime: $(endDateSelector).val(),
            dateTimeFormat: "yyyy-MM-dd"
        }, function(response) {
            var chart = self.charts[context];
            if (chart != null) {
                chart.destroy();
            }
            chart = new Chart(context).Bar(response, {});
            self.charts[context] = chart;
        });
    };

    self.totalFeeReport = function(context, startDateSelector, endDateSelector) {
        $.getJSON("rest/report/total-fee", {
            startDateTime: $(startDateSelector).val(),
            endDateTime: $(endDateSelector).val(),
            dateTimeFormat: "yyyy-MM-dd"
        }, function(response) {
            var chart = self.charts[context];
            if (chart != null) {
                chart.destroy();
            }
            chart = new Chart(context).Bar(response, {});
            self.charts[context] = chart;
        });
    }

    self.fetchConsumersMatrix = function(page) {
        var consumersMatrixTableBody = $(".centerwell .consumer-matrix tbody");;
        consumersMatrixTableBody.empty();

        $.getJSON("/rest/report/consumers/" + page, function(response) {
            $.each(response, function(k, v) {
                var row = $('<tr class="consumer"></tr>');
                row.append($('<td class="nick">' + v.consumerNick + '</td>'));
                row.append($('<td class="count-of-bills">' + v.countOfBills + '</td>'));
                row.append($('<td class="first-paid">' + v.strFirstPaid + '</td>'));
                row.append($('<td class="latest-paid">' + v.strLatestPaid + '</td>'));

                consumersMatrixTableBody.append(row);
            });
        });
    }

    self.fetchProducts = function(tableBodySelector, page) {
        var tableBody = $(tableBodySelector);
        tableBody.empty();

        $.getJSON("/rest/report/products/" + page, function(response) {
            $.each(response, function(k, v) {
                var row = $('<tr class="product"></tr>');
                row.append($('<td class="picture"><img src="' + v.picturePath + '"/></td>'));
                row.append($('<td class="title">' + v.title + '</td>'));
                row.append($('<td class="price">' + v.price + '</td>'));

                tableBody.append(row);
            });
        });
    }
}
