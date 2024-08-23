$(document).ready(function() {
    var table = $('#departmentTable').DataTable({
        processing: true,
        serverSide: true,
        ajax: {
            url: "/department/list",
            type: "GET",
            data: function(d) {
                var order = d.order[0]; // get the first column order
                var column = d.columns[order.column].data; // get the column name
                var dir = order.dir;
                return $.extend({}, d, {
                    search: d.search.value,
                    start: d.start,
                    length: d.length,
                    sortColumn: column,
                    sortDirection: dir
                });
            }
        },
        columns: [
            { data: "departmentId" },
            { data: "departmentName" },
            { data: "hod" },
            {
                data: "departmentId",
                render: function(data, type, row) {
                    return '<a data-department-id="' + data + '" data-total-salary="' + row.totalSalary + '" class="view-employees btn btn-primary btn-sm mb-3">View Employees</a>';
                }
            },
            {
                data: "departmentId",
                render: function(data, type, row) {
                    return `
                        <button class="btn btn-primary btn-sm mb-3" onclick="editDepartment(${data})">
                            <i class="fa fa-edit"></i>
                        </button>
                        <a href="/department/delete/${data}" class="btn btn-danger btn-delete btn-sm mb-3">
                            <i class="fa fa-trash"></i>
                        </a>
                    `;
                }
            }
        ],
    });

    $('#departmentTable tbody').on('click', 'a.view-employees', function(event) {
        event.preventDefault();

        var departmentId = $(this).data('department-id');
        var totalSalary = $(this).data('total-salary'); // Get the total salary

        fetch(`/department/show-employees/${departmentId}`)
            .then(response => response.json())
            .then(data => {
                const tableBody = document.getElementById('employeeTableBody');
                tableBody.innerHTML = '';

                data.forEach(employee => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${employee.employeeId}</td>
                        <td>${employee.employeeName}</td>
                        <td>${employee.employeeEmail}</td>
                        <td>${employee.employeeSalary}</td>
                    `;
                    tableBody.appendChild(row);
                });
                document.getElementById('totalSalary').innerText = totalSalary;
                $('#employeeModal').modal('show');
            })
            .catch(error => console.error('Error fetching employee data:', error));
    });
});

$('#departmentModal').on('hidden.bs.modal', function() {
    clearModal();
});

function clearModal() {
    $('#departmentForm')[0].reset();
    $('#departmentId').val(''); // Clear departmentId
    $('#departmentModalLabel').text('Add Department');
    $('#saveDepartmentBtn').text('Save');
    $('#saveDepartmentBtn').attr('onclick', 'submitForm()');
}

function submitForm() {
    var departmentId = $('#departmentId').val();
    var departmentName = $('#departmentName').val();
    var hod = $('#hod').val();

    if (!departmentName || !hod) {
        Swal.fire({
            icon: 'error',
            title: 'Validation Error',
            text: 'Please fill in all required fields.'
        });
        return;
    }

addData();
}

function addData() {
    var department = {
            departmentId: $('#departmentId').val(),
            departmentName: $('#departmentName').val(),
            hod: $('#hod').val()
    };
   var csrfToken = $('meta[name="_csrf"]').attr('content');
   var csrfHeader = $('meta[name="_csrf_header"]').attr('content');

    $.ajax({
        type: department.departmentId ? 'PUT' : 'POST',
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeader, csrfToken);
         },
        url: department.departmentId ? '/department/update': '/department/save',
        contentType: 'application/json',
        data: JSON.stringify(department),
        success: function (response) {
            $('#departmentModal').modal('hide');
            Swal.fire({
                title: 'Success!',
                text: response.message,
                icon: 'success',
                confirmButtonText: 'OK'
            }).then(() => {
                location.reload();
            });
        },
        error: function (xhr) {
            var errorMessage = xhr.responseJSON ? xhr.responseJSON.message : 'An error occurred';
            Swal.fire({
                title: 'Error',
                text: errorMessage,
                icon: 'error',
                confirmButtonText: 'OK'
            });
        }
    });
}

function editDepartment(departmentId) {
    $.ajax({
        url: '/department/' + departmentId,
        type: 'GET',
        success: function(department) {
            openUpdateDepartmentModal(department);
            $('#departmentModal').modal('show');
        },
        error: function(xhr, status, error) {
            var errorMessage = xhr.responseJSON ? xhr.responseJSON.message : 'An error occurred';
            Swal.fire({
                title: 'Error',
                text: errorMessage,
                icon: 'error',
                confirmButtonText: 'OK'
            });
        }
    });
}

function openUpdateDepartmentModal(department) {
    $('#departmentForm')[0].reset();
    $('#departmentId').val(department.departmentId);
    $('#departmentName').val(department.departmentName);
    $('#hod').val(department.hod);
    $('#departmentModalLabel').text('Update Department');
    $('#saveDepartmentBtn').text('Update');
    $('#saveDepartmentBtn').attr('onclick', 'submitForm()');
}
