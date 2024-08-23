$(document).ready(function () {
    $.validator.addMethod('imageFormat', function(value, element) {
        // If no file is selected, consider it valid
        if (element.files.length === 0) {
            return true;
        }

        // Check if the file type matches one of the valid image types
        var fileType = element.files[0].type;
        var validTypes = /^image\/(jpeg|jpg|png)$/.test(fileType);

        return validTypes;
    },);

    $.validator.addMethod('imageSize', function(value, element) {
        // If no file is selected, consider it valid
        if (element.files.length === 0) {
            return true;
        }
        // Check if the file size is less than or equal to 100 KB
        var validSize = element.files[0].size <= 102400 && element.files[0].size > 0; // 100kb in bytes

        return validSize;
    },);
        // Initialize jQuery validation plugin
    $('#employeeForm').validate({
        rules: {
            employeeName: {
                required: true,
                minlength: 2
            },
            employeeEmail: {
                required: true,
                email: true

            },
            employeeSalary: {
                required: true,
                number: true
            },
            joiningDate: {
                required: true
            },
            dob: {
                required: true
            },
             employeeAddress: {
                required: true,
                minlength: 10
            },
            departments: {
                required: true
            },
            profile: {
                required: true,
                imageFormat:true,
                imageSize:true

            },
            profileUpdate: {
                required:false,
                imageFormat:true,
                imageSize:true
            }
        },
        messages: {
            employeeName: {
                required: "Please enter the employee's name",
                minlength: "The name must be at least 2 characters long"
            },
            employeeEmail: {
                required: "Please enter the employee's email",
                email: "Please enter a valid email address"
            },
            employeeSalary: {
                required: "Please enter the employee's salary",
                number: "Please enter a valid number"
            },
            joiningDate: {
                required: "Please select a joining date"
            },
            dob: {
                required: "Please select the birth date"
            },
            employeeAddress: {
                required: "Please enter the employee address",
                minlength: "Address must be at least 10 characters long"
            },
            departments: {
                required: "Please select at least one department"
            },
            profile: {
                required: "Please select a profile picture",
                imageFormat: "Invalid file format. Please upload a JPEG or Or JPG or PNG file.",
                imageSize: "Image size must be less than 100KB"
            },
             profileUpdate: {
                    imageFormat: "Invalid file format. Please upload a JPEG or JPG or PNG file.",
                    imageSize: "Image size must be less than 100KB"
                }
        },
        submitHandler: function (form) {
            var formData = new FormData(form);
            var url = $('#employeeId').val() ? '/employee/update' : '/employee/save';
            var type = $('#employeeId').val() ? 'PUT' : 'POST';
            var csrfToken = $('meta[name="_csrf"]').attr('content');
            var csrfHeader = $('meta[name="_csrf_header"]').attr('content');

            $.ajax({
                url: url,
                type: type,
                beforeSend: function (xhr) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                },
                enctype: 'multipart/form-data',
                data: formData,
                processData: false,
                contentType: false,
                success: function (response) {
                    Swal.fire({
                        title: response.status ? 'Success' : 'Error',
                        text: response.message,
                        icon: response.status ? 'success' : 'error',
                        confirmButtonText: 'OK'
                    }).then((result) => {
                        if (result.isConfirmed && response.status) {
                            window.location.href = '/';
                        }
                    });
                },
                error: function (xhr, status, error) {
                    // Handle error response
                    let errorMessage = xhr.responseJSON ? xhr.responseJSON.message : "An error occurred";
                    Swal.fire({
                        title: 'Error!',
                        text: errorMessage,
                        icon: 'error',
                        confirmButtonText: 'OK'
                    });
                }
            });
        }
    });
});

$('.selectpicker').selectpicker();

$('#joiningDatePicker').datetimepicker({
    format: 'DD-MM-YYYY HH:mm:ss'
});

$('#dobPicker').datetimepicker({
     format: 'DD-MM-YYYY'
});

