$('#excelButton').click(function() {
    downloadFile('xlsx');
});

$('#pdfButton').click(function() {
    downloadFile('pdf');
});


function parseDateString(dateString) {
        const monthNames = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

        const parts = dateString.split('-');
        const day = parseInt(parts[0]);
        const month = monthNames.indexOf(parts[1]);
        const year = parseInt(parts[2], 10);

        return new Date(year, month, day);
    }

function downloadFile(filetype){
        var employees = [];

        document.querySelectorAll('#employeeTable tbody tr').forEach(function(row) {
            var employee = {
                employeeId: row.cells[0].innerText,
                employeeName: row.cells[1].innerText,
                employeeEmail: row.cells[2].innerText,
                employeeAddress: row.cells[3].innerText,
                joiningDate: parseDateString(row.cells[4].innerText),
                employeeSalary: row.cells[5].innerText,
                dob:parseDateString(row.cells[6].innerText),
//                profileUrl: row.cells[8].querySelector('img').src
            };
            employees.push(employee);
        });

        fetch('/download/'+filetype, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(employees)
        })
        .then(response => response.blob())
        .then(blob => {
            var url = window.URL.createObjectURL(blob);
            var a = document.createElement('a');
            a.href = url;
            a.download = 'employees.'+filetype;
            document.body.appendChild(a);
            a.click();
            a.remove();
        })
        .catch(error => console.error('Error:', error));
    }



