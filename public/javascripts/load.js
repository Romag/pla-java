$(function() {
    getPersons();
    $(".update").click(getPersons);


    function getPersons() {
        var data_current_rows = parseInt($("#personTable").attr("data-current-rows"));

        $.get("/personsJson", {"rowsInTable":data_current_rows}, function(data) {
            data.forEach(function (element) {
                var id = element.id;
                var name = element.name;
                var age = element.age;
                var markup = "<tr><td>"+id+"</td><td>"+name+"</td><td>"+age+"</td></tr>";

                $("#personTableBody").append(markup);
            })

            //Reset number of rows after appending
            $("#personTable").attr("data-current-rows", data_current_rows + data.length);

        })
    }
})