$(function () {
    makeEditable({
            ajaxUrl: "ajax/profile/meals/",
            filterForm: $("#filter"),
            datatableApi: $("#datatable").DataTable({
                "paging": false,
                "info": true,
                "columns": [
                    {
                        "data": "dateTime"
                    },
                    {
                        "data": "description"
                    },
                    {
                        "data": "calories"
                    },
                    {
                        "defaultContent": "Edit",
                        "orderable": false
                    },
                    {
                        "defaultContent": "Delete",
                        "orderable": false
                    }
                ],
                "order": [
                    [
                        0,
                        "dsc"
                    ]
                ]
            })
        }
    );
});


function updateTable() {
    $.ajax({
        type: "GET",
        url: context.ajaxUrl + 'filter',
        data: context.filterForm.serialize()
    }).done(function (data) {
        context.datatableApi.clear().rows.add(data).draw();
    });
}

function clearFilter() {
    context.filterForm.find(":input").val("");
    updateTable();
}