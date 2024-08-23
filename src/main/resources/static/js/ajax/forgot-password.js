$(document).ready(function() {
    $('#forgotPasswordForm').on('submit', function(e) {
        e.preventDefault();
        var email = $('#email').val();

        $.ajax({
            url: "/forgot-password",
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ email: email }),
            success: function(response) {
                if (response.status) {
                    $('#resetPasswordModal').modal('show');
                } else {
                   Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: response.message,
                    })
                }
            },
            error: function(xhr, status, error) {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'An error occurred. Please try again.',
                })
            }
        });
    });
});

$('#resetPasswordModal').on('hidden.bs.modal', function() {
    clearModal();
});
function clearModal() {
    $('#forgotPasswordForm')[0].reset();
    $('#newPassword').val('');
    $('#confirmPassword').val('');
}

function submitNewPassword() {
    var email = $('#email').val();
    var newPassword = $('#newPassword').val();
    var confirmPassword = $('#confirmPassword').val();

    if (newPassword !== confirmPassword) {
        Swal.fire({
            icon: 'error',
            title: 'Oops...',
            text: 'Passwords do not match!',
        })
        return;
    }

    $.ajax({
        url: "/reset-password",
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ email:email,newPassword: newPassword }),
        success: function(response) {
            Swal.fire({
                icon: 'success',
                title: 'Success',
                text: response.message,
            })
            $('#resetPasswordModal').modal('hide');
        },
        error: function(xhr, status, error) {
            Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'An error occurred. Please try again.',
            })
        }
    });
}