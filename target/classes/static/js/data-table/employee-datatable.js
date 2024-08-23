$(document).ready(function() {
    // Initialize main employee DataTable
    var mainTable = $('#employeeTable').DataTable({
        processing: true,
        serverSide: true,
        ajax: {
            url: '/employee/list', // URL to your server-side endpoint
            type: 'GET',
            data: function(d) {
                var order = d.order[0];
                var column = d.columns[order.column].data;
                var dir = order.dir;
                return $.extend({}, d, {
                       search: d.search.value,
                       start: d.start,
                       length: d.length,
                       sortColumn: column,
                       sortDirection: dir
                   }, $('#filterForm').serializeObject());
            }
        },
        columns: [
            { data: "employeeId" },
            { data: "employeeName" },
            { data: "employeeEmail" },
            { data: "employeeAddress" },
            {
                data: "joiningDate",
                render: function(data) {
                    return moment(data).format('DD-MM-YYYY HH:mm:ss');
                }
            },
            { data: "employeeSalary" },
            {
                data: "dob",
                render: function(data) {
                    return moment(data).format('DD-MM-YYYY');
                }
            },
            {
                data: "employeeId",
                render: function(data) {
                    return '<a data-employee-id="' + data + '" class="view-departments btn btn-primary btn-sm mb-3">View Department</a>';
                }
            },
            {
                data: "profileUrl",
                render: function(data, type, row) {
                    return '<img src="' + row.profileUrl + '" alt="' + row.profileUrl + '" height="100" width="161" />';
                }
            },

            {
                data: "employeeId",
                render: function(data) {
                    return '<a href="/employee/update/' + data + '">' +
                           '<button class="btn btn-primary btn-sm mb-3"><i class="fa fa-edit"></i></button></a>' +
                           '<a href="/employee/delete/' + data + '" class="btn-delete">' +
                           '<button class="btn btn-danger btn-sm mb-3"><i class="fa fa-trash"></i></button></a>';
                }
            }
        ],
        order: [[0, "asc"]]
    });

    // Handle form submission with AJAX to filter data
    $('#filterForm').submit(function(event) {
        event.preventDefault();
        mainTable.ajax.reload();
        $('#filterModal').modal('hide');
    });

    // Handle click on "View Departments" link in DataTable
    $('#employeeTable tbody').on('click', 'a.view-departments', function(event) {
        event.preventDefault();
        var employeeId = $(this).data('employee-id');
        fetch(`/employee/show-departments/${employeeId}`)
            .then(response => response.json())
            .then(data => {
                var tableBody = document.getElementById('departmentTableBody');
                tableBody.innerHTML = '';
                data.forEach(department => {
                    var row = tableBody.insertRow();
                    row.innerHTML = `<td>${department.departmentId}</td><td>${department.departmentName}</td><td>${department.hod}</td>`;
                });
                $('#departmentModal').modal('show');
            })
            .catch(error => console.error('Error fetching department data:', error));
    });

    // Initialize datepicker for filter modal
    $('.datepicker').datepicker({
        format: 'dd-mm-yyyy',
        autoclose: true

    });
});

// Helper function to serialize form data to object
$.fn.serializeObject = function() {
    var obj = {};
    var a = this.serializeArray();
    $.each(a, function() {
        if (obj[this.name]) {
            if (!obj[this.name].push) {
                obj[this.name] = [obj[this.name]];
            }
            obj[this.name].push(this.value || '');
        } else {
            obj[this.name] = this.value || '';
        }
    });
    return obj;
};
