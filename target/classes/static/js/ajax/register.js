  $(document).ready(function() {
        $.validator.addMethod("checklower", function(value) {
            return /[a-z]/.test(value);
        });

        $.validator.addMethod("checkupper", function(value) {
            return /[A-Z]/.test(value);
        });

        $.validator.addMethod("checkdigit", function(value) {
            return /[0-9]/.test(value);
        });

        $.validator.addMethod("checkspecial", function(value) {
            return /[@$!%*?&]/.test(value);
        });

        $("#registerForm").validate({
            rules: {
                firstName: {
                    required: true
                },
                lastName: {
                    required: true
                },
                username: {
                    required: true,
                    minlength: 6,
                    maxlength: 20
                },
                email: {
                    required: true,
                    email: true
                },
                password: {
                    required: true,
                    minlength: 8,
                    maxlength: 20,
                    checklower: true,
                    checkupper: true,
                    checkdigit: true,
                    checkspecial: true
                },
                confirmPassword: {
                    required: true,
                    equalTo: "#password"
                }
            },
            messages: {
                firstName: {
                    required: "Please enter your first name"
                },
                lastName: {
                    required: "Please enter your last name"
                },
                username: {
                    required: "Please enter a username",
                    minlength: "Username must be at least 6 characters",
                    maxlength: "Username cannot exceed 20 characters"
                },
                email: {
                    required: "Please enter your email",
                    email: "Please enter a valid email address"
                },
                password: {
                    required: "Please enter a password",
                    minlength: "Password must be at least 8 characters long",
                    maxlength: "Password cannot exceed 20 characters",
                    checklower: "Password must contain at least one lowercase letter",
                    checkupper: "Password must contain at least one uppercase letter",
                    checkdigit: "Password must contain at least one digit",
                    checkspecial: "Password must contain at least one special character (@$!%*?&)"
                },
                confirmPassword: {
                    required: "Please confirm your password",
                    equalTo: "Passwords do not match"
                }
            },
            submitHandler: function(form) {
                validateAndSubmit();
            }
        });
    });

    function validateAndSubmit() {
        const data = {
            firstName: $('#firstName').val(),
            lastName: $('#lastName').val(),
            username: $('#username').val(),
            email: $('#email').val(),
            password: $('#password').val(),
            confirmPassword: $('#confirmPassword').val()
        };

        $.ajax({
            url: '/auth/register-user',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(data),
            success: function(response) {
                Swal.fire({
                    title: response.status ? 'Success' : 'Error',
                    text: response.message,
                    icon: response.status ? 'success' : 'error',
                    confirmButtonText: 'OK'
                }).then((result) => {
                    if (result.isConfirmed && response.status) {
                        window.location.href = '/login';
                    }
                });
            },
            error: function(xhr, status, error) {
                let errorMessage = xhr.status + ': ' + xhr.statusText;
                if (xhr.responseText) {
                    errorMessage += '\n' + xhr.responseText;
                }
                Swal.fire({
                    title: 'Error!',
                    text: 'Registration failed: ' + errorMessage,
                    icon: 'error',
                    confirmButtonText: 'OK'
                });
            }
        });
    }