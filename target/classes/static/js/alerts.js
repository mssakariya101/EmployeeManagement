$(document).on('click', '.btn-delete', function (e) {
    e.preventDefault();
    var url = $(this).attr('href');
    var csrfToken = $('meta[name="_csrf"]').attr('content');
    var csrfHeader = $('meta[name="_csrf_header"]').attr('content');

    Swal.fire({
        title: 'Are you sure?',
        text: "You won't be able to revert this!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Yes, delete it!'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: url,
                type: 'DELETE',
                 beforeSend: function (xhr) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                 },
                success: function (response) {
                    Swal.fire({
                        title: response.status ? 'Success' : 'Error',
                        text: response.message,
                        icon: response.status ? 'success' : 'error',
                        confirmButtonText: 'OK'
                    }).then(() => {
                        location.reload();
                    });
                },
                error: function (xhr, status, error) {
                    var response = JSON.parse(xhr.responseText);
                    var errorMessage = response.message || 'An unexpected error occurred.';

                    Swal.fire({
                        title: 'Error',
                        text: errorMessage,
                        icon: 'error',
                        confirmButtonText: 'OK'
                    });
                }
            });
        }
    });
});

$(document).on('click', '.btn-logout', function (e) {
    e.preventDefault();
    var url = $(this).attr('href');
    var csrfToken = $('meta[name="_csrf"]').attr('content');
    var csrfHeader = $('meta[name="_csrf_header"]').attr('content');

    Swal.fire({
        title: 'Logout',
        text: "Are you sure you want to sign out!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sign out'
    }).then((result) => {
        if (result.isConfirmed) {
            $.ajax({
                url: url,
                type: "POST",
                 beforeSend: function (xhr) {
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                },
                success: function () {
                    Swal.fire({
                        title: 'Success',
                        text: "Logout Successful",
                        icon: 'success',
                        confirmButtonText: 'OK'
                    })
                    .then(() => {
                        location.reload();
                    });
                },
                error: function () {
                    Swal.fire({
                        title: 'Error',
                        text: "Logout failed",
                        icon: 'error',
                        confirmButtonText: 'OK'
                    });
                }
            });
        }
    });
});
