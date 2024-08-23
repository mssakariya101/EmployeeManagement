    $(document).ready(function() {
        fetchEmployeeSalaryData();
    });


    function fetchEmployeeSalaryData() {
        $.ajax({
            url: '/department/chart', // Replace with your actual endpoint to fetch employee data
            type: 'GET',
            success: function(data) {
                const departmentNames = data.map(department => department.departmentName);
                const departmentSalaries = data.map(department => department.totalSalary);
                renderSalaryChart(departmentNames, departmentSalaries);
            },
            error: function(error) {
                console.error('Error fetching employee data:', error);
            }
        });
    }

    function renderSalaryChart(labels, data) {
        const ctx = document.getElementById('salaryChart').getContext('2d');
        const salaryChart = new Chart(ctx, {
            type: 'line', // You can change the chart type to 'line', 'pie', etc.
            data: {
                labels: labels,
                datasets: [{
                    label: 'Employee Salaries',
                    data: data,
                    backgroundColor: 'rgba(102, 120, 215, 0.5)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 1
                }]
            },
            options: {
             plugins: {
                        legend: {
                            display: true,
                            labels: {
                                color: 'rgb(102, 99, 132)',
                                fontColor: "red"

                            }
                        }
                    },
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }
