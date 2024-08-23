function validateForm() {
        var departmentName = document.getElementById("departmentName").value.trim();
        var hod = document.getElementById("hod").value.trim();
        if (departmentName === "") {
            Swal.fire({
                title: 'Validation Error',
                text: 'Department Name is required',
                icon: 'warning',
                confirmButtonText: 'OK'
            });
            return false;
        }
         if(departmentName.length<2)
        {
         Swal.fire({
                title: 'Validation Error',
                text: 'Department Must be greater than 2',
                icon: 'warning',
                confirmButtonText: 'OK'
            });
            return false;
        }
        if (hod === "") {
            Swal.fire({
                title: 'Validation Error',
                text: 'HOD is required',
                icon: 'warning',
                confirmButtonText: 'OK'
            });
            return false;
        }
        return true;
    }