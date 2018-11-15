$(function() {
    $(".update").click(getPersons);


    function getPersons() {
        var data_current_rows = parseInt($("#personTable").attr("data-current-rows"));

        $.get("/personsJson", {"rowsInTable":data_current_rows}, function(data) {

            $("#personTableBody").append(data['content']);

            $("#personTable").attr("data-current-rows", data_current_rows + data['count']);
        })
    }
})